package de.projektss17.bonpix.recognition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Domi on 10.05.2017.
 */

public class C_PicChanger {

    public Bitmap convertBitmapGrayscale(Bitmap bitmap){

        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;

    }

    public Bitmap convertBitmapBlackAndWhite(Bitmap bitmap){
        Bitmap bwBitmap = Bitmap.createBitmap( bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565 );
        float[] hsv = new float[ 3 ];
        for( int col = 0; col < bitmap.getWidth(); col++ ) {
            for( int row = 0; row < bitmap.getHeight(); row++ ) {
                Color.colorToHSV( bitmap.getPixel( col, row ), hsv );
                if( hsv[ 2 ] > 0.5f ) {
                    bwBitmap.setPixel( col, row, Color.WHITE );
                } else {
                    bwBitmap.setPixel( col, row, Color.BLACK );
                }
            }
        }
        return bwBitmap;

    }

    public Bitmap[] cutBitmapHorizontal(Bitmap bitmap, int coordinate){

        Bitmap[] bitmaps = new Bitmap[2];

        bitmaps[0] = this.cropBitmap(bitmap, 0, 0, coordinate, bitmap.getHeight());
        bitmaps[1] = this.cropBitmap(bitmap, coordinate, 0, bitmap.getWidth() - coordinate, bitmap.getHeight());

        return bitmaps;
    }

    public ArrayList<Bitmap> getLineArray(Bitmap bitmap, int height){

        final int     bitHeight = bitmap.getHeight(),
                        bitWidth = bitmap.getWidth();

        ArrayList<Bitmap> list = new ArrayList<>();

        int iterations = bitHeight / height;

        for(int i = 0; i < iterations; i++){
            int y = (int)(double)(i*height);
            int h = (int)(double)((bitHeight - (bitHeight-height)));

            if(!((y+h) >= bitHeight)){
                list.add(Bitmap.createBitmap(bitmap, 0, y, bitWidth, h));
            }

        }

        return list;

    }

    public ArrayList<Bitmap> getLines(Bitmap bitmap, ArrayList<Integer> linesList){

        final int     bitHeight = bitmap.getHeight(),
                bitWidth = bitmap.getWidth();

        ArrayList<Bitmap> list = new ArrayList<>();

        int pixLeft = bitHeight;
        int count = 1;

        for(int x : linesList){

            int y = (int)(double)((bitHeight-pixLeft)*0.5);
            int h = (int)(double)((( bitHeight - (bitHeight-x))*3));

            if(pixLeft == bitHeight){
                list.add(Bitmap.createBitmap(bitmap, 0, 0, bitWidth, h));
                pixLeft -= list.get(0).getHeight();
            } else {
                list.add(Bitmap.createBitmap(bitmap, 0, y, bitWidth, h));
                pixLeft -= list.get(count).getHeight();
                count++;
            }
        }

        return list;

    }


    public ArrayList<Bitmap> getColumArray(Bitmap bitmap, int width){

        final int     bitHeight = bitmap.getHeight(),
                bitWidth = bitmap.getWidth();

        ArrayList<Bitmap> list = new ArrayList<>();

        int iterations = bitWidth / width;

        for(int i = 0; i < iterations; i++){
            list.add(Bitmap.createBitmap(bitmap, (i*width), 0, bitWidth - (bitWidth-width), bitHeight));
        }

        return list;

    }

    public ArrayList<Integer> countBlackPixels(Bitmap bitmap){
        final int width = bitmap.getWidth(),
                height = bitmap.getHeight();

        bitmap = this.convertBitmapBlackAndWhite(bitmap);

        int count = 0;

        String lastState = " ";

        ArrayList<Integer> lineHeight = new ArrayList<>();

        Log.e("### TRYING TO DETECT","######");

        for(int i = 0; i < height; i++){

            int pixel = bitmap.getPixel(0,i);

            if(pixel == Color.WHITE) {
                lastState = "WHITE";
            }

            if(lastState.equals("WHITE")){
                if(count != 0){
                    if(!(count < 8)){
                        lineHeight.add(count);
                    }
                    count = 0;
                } else {
                    lastState = "BLACK";
                }
            } else {
                count++;
                lastState = "BLACK";
            }
        }

        int ergebnis = 0;

        for(int x : lineHeight){
            Log.e("### ZEILENHÖHE", x + "");
            ergebnis += x;
        }

        if(lineHeight.size() != 0) {
            ergebnis = ergebnis / lineHeight.size();
            Log.e("### SCHNITT", ergebnis + "");
        }

        Log.e("### LÄNGE", lineHeight.size() + "");

        return lineHeight;
    }

    public Bitmap getRect(Bitmap bitmap){
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 10, 100, bitmap.getWidth() - 10, bitmap.getHeight() - (bitmap.getHeight()-100));

        return croppedBitmap;
    }

    public Bitmap getOnlyPrices(Bitmap bitmap, ArrayList<Point> pointArray){

        Log.e("BITMAP SIZE", "" + bitmap.getWidth() + " " + bitmap.getHeight());

        int x = 0, yMin = 0, yMax = 0,
                height = 0;

        Point oRight = null;

        for(Point point : pointArray){

            if(x < point.x){
                x = point.x;
                oRight = point;
            }
        }

        yMin = oRight.y;

        for(Point point : pointArray){
            if(point.x <= oRight.x+20 && point.x >= oRight.x-20){

                if(yMin > point.y){
                    yMin = point.y;
                }

                if(yMax < point.y){
                    yMax = point.y;
                }
            }
        }

        height = yMax - yMin;

        if((yMin+height) <= (bitmap.getHeight()-20)){
            height = height+20;
        }

        Log.e("##### ORIGINAL BITMAP", "x=" + bitmap.getWidth() + " y=" + bitmap.getHeight());
        Log.e("##### CONVERTED BITMAP", "x=" + 0 + " y=" + yMin + " width=" + bitmap.getWidth() + " height=" + height);

        return cropBitmap(bitmap, 0, yMin, bitmap.getWidth(), height);

    }

    public Bitmap cropBitmap(Bitmap bitmap, int x, int y, int width, int height){

        return Bitmap.createBitmap(bitmap, x, y, width, height);

    }

}
