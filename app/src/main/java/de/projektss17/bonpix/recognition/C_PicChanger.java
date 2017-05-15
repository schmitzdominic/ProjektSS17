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

public class C_PicChanger {

    /**
     * Konvertiert ein Bitmap zu einem Grayscale Bild
     * @param bitmap Original Bitmap
     * @return Grayscale Bitmap
     */
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

    /**
     * Konvertiert ein Bitmap in ein Schwarz-Weiß spektrum
     * WARING! Sehr langsam!!!
     * @param bitmap Original Bitmap
     * @return Schwarz-Weiß Bitmap
     */
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

    /**
     * Schneidet ein Bitmap Horizontal an einer bestimmten Koordinate
     * @param bitmap Original Bitmap
     * @param coordinate Koordinate
     * @return Array mit 2 Bitmaps, [0] links, [1] rechts
     */
    public Bitmap[] cutBitmapHorizontal(Bitmap bitmap, int coordinate){

        Bitmap[] bitmaps = new Bitmap[2];

        bitmaps[0] = this.cropBitmap(bitmap, 0, 0, coordinate, bitmap.getHeight());
        bitmaps[1] = this.cropBitmap(bitmap, coordinate, 0, bitmap.getWidth() - coordinate, bitmap.getHeight());

        return bitmaps;
    }

    /**
     * Gibt eine Liste mit Linien (Bitmaps) zurück
     * @param bitmap Original Bitmap
     * @param height Höhe der Linien
     * @return Liste mit Bitmaps (Linien)
     */
    public ArrayList<Bitmap> getLineList(final Bitmap bitmap, int height){

        final int     bitHeight = bitmap.getHeight();

        ArrayList<Bitmap> list = new ArrayList<>();

        int iterations = bitHeight / height;

        for(int i = 0; i < iterations; i++){

            if(i == 0){
                list.add(this.cropBitmap(bitmap, 0, i*height, bitmap.getWidth(), bitHeight - (bitHeight-height)));
            } else if (i == iterations - 1){
                list.add(this.cropBitmap(bitmap, 0, (int)(double)(i*height*0.95), bitmap.getWidth(), (int)(double)((bitHeight - (bitHeight-height))*1.05)));
            } else {
                list.add(this.cropBitmap(bitmap, 0, (int)(double)(i*height*0.9), bitmap.getWidth(), (int)(double)((bitHeight - (bitHeight-height))*1.3)));
            }
        }

        return list;

    }

    /**
     * Gibt eine Liste mit Streifen in der angegebenen Breite zurück
     * @param bitmap Bitmap
     * @param width Breite eines Streifens
     * @return ArrayList mit Streifen
     */
    public ArrayList<Bitmap> getColumList(Bitmap bitmap, int width){

        ArrayList<Bitmap> list = new ArrayList<>();
        final int bitHeight = bitmap.getHeight(),
                  bitWidth = bitmap.getWidth();

        int iterations = bitWidth / width;

        for(int i = 0; i < iterations; i++){
            list.add(Bitmap.createBitmap(bitmap, (i*width), 0, bitWidth - (bitWidth-width), bitHeight));
        }

        return list;
    }

    /**
     * Zählt die Schwarzen Pixel in einem Streifen
     * WICHTIG! Bitte nur 1px breite Streifen verwenden!
     * @param bitmap Bitmap
     * @return Liste mit der Breite jeder Zeile
     */
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

    /**
     * Gibt ein Bitmap das nur den Bereich der Artikel enthält zurück
     * Um ein PointArray zu bekommen, bitte vorher den Recognizer in der C_OCR ausführen!
     * @param bitmap Bitmap
     * @param pointArray Liste mit Bildpunkten
     * @return Bitmap des Artikelbereichs
     */
    public Bitmap getOnlyArticleArea(Bitmap bitmap, ArrayList<Point> pointArray){

        int x = 0,
                yMin = 0,
                yMax = 0,
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

        return cropBitmap(bitmap, 0, yMin, bitmap.getWidth(), height);

    }

    /**
     * Schneidet ein Bitmap entsprechend der übergebenen Werte zurecht
     * @param bitmap Original Bitmap
     * @param x Wo auf der x Koordinate soll das Bild anfangen
     * @param y Wo auf der y Koordinate soll das Bild anfangen
     * @param width Wie breit soll das Bild werden
     * @param height Wie hoch soll das Bild werden
     * @return Zugeschnittenes Bild
     */
    public Bitmap cropBitmap(Bitmap bitmap, int x, int y, int width, int height){

        return Bitmap.createBitmap(bitmap, x, y, width, height);

    }

}
