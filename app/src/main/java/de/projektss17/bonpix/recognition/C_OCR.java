package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.projektss17.bonpix.auswerter.Default;
import de.projektss17.bonpix.daten.C_Artikel;

/**
 * Created by Domi on 14.04.2017.
 */

public class C_OCR {

    private Context context;
    private C_MatchLaden laden;
    private String ladenName, adresse, recognizedText;
    private ArrayList<String> produkte, preise;
    private Resources res;
    private Default ladenInstanz;
    private ArrayList<Point> pointList;
    private ArrayList<C_Artikel> articles;
    private C_PicChanger picChanger;
    public static final String TESS_DATA = "/tessdata";
    private static final String DATA_PATH = Environment.getExternalStorageDirectory() + "/BonPix/";


    public C_OCR(Context context){
        this.context = context;
        this.res = this.context.getResources();
        this.laden = new C_MatchLaden(this.context);
        this.pointList = new ArrayList<>();
        this.articles = new ArrayList<>();
        this.picChanger = new C_PicChanger();
    }

    /**
     * Ließt anhand von OCR alle Zeichen aus und schreibt diese
     * in die Instanzvariablen.
     * @param bitmap Bild das ausgewertet werden soll
     */
    public void recognize(Bitmap bitmap){

        this.recognizedText = this.recognizer(bitmap);
        this.recognize(bitmap, this.laden.getLaden(this.recognizedText));

    }

    /**
     * Ließt anhand von OCR alle Zeichen aus und schreibt diese
     * in die Instanzvariablen
     * @param bitmap Bild das ausgewertet werden soll
     * @param ladenName Der Laden um den es sich handelt (Bitte auswerter.xml beachten!)
     */
    public void recognize(Bitmap bitmap, String ladenName){

        // Wenn der Text noch nicht ausgelesen wurde
        if(this.recognizedText == null){
            this.recognizedText = this.recognizer(bitmap);
        }

        // Nur um sicher zu gehen das der Name auch supportet wird
        if(laden.getAuswerterClass(ladenName) == null){
            ladenName = "NOT SUPPORTED";
        }

        // Instanz des Ladens bilden
        if(!ladenName.equals("NOT SUPPORTED")){
            this.ladenInstanz = laden.getInstanceOf(ladenName);
        } else {
            this.ladenInstanz = laden.getInstanceOf("Default");
        }

        // Attribute setzen
        this.ladenName = ladenName;
        this.adresse = this.ladenInstanz.getAdresse(this.recognizedText);
        this.setArticles(bitmap);

    }

    /**
     * Holt über OCR die Infos aus der Bitmap heraus
     * @param bitmap Bild
     * @return String mit Informationen
     */
    private String recognizer(Bitmap bitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this.context).build();
        if (!textRecognizer.isOperational()) {
            Log.e("ERROR", "Detector dependencies are not yet available");
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);
                for(Point point : item.getCornerPoints()){
                    //Log.e("## POINT", ""+point.toString());
                    this.pointList.add(point);
                }
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        return "";
    }

    private String recognizerTesseract(Bitmap bitmap){

        prepareTessDAta();

        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(DATA_PATH,"deu");
        tessBaseAPI.setImage(bitmap);
        tessBaseAPI.setVariable("load_system_dawg","0");
        tessBaseAPI.setVariable("language_model_penalty_non_dict_word", "0");
        tessBaseAPI.setVariable("language_model_penalty_non_freq_dict_word", "0");
        String result = "No Result";
        result = tessBaseAPI.getUTF8Text();
        tessBaseAPI.end();

        return result;
    }

    private void prepareTessDAta(){

        try{
            File dir = new File(DATA_PATH + TESS_DATA);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String fileList[] = new String[0];
            try {
                fileList = res.getAssets().list("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(String fileName : fileList){
                String pathToDataFile = DATA_PATH+TESS_DATA+"/"+fileName;
                if(!(new File(pathToDataFile)).exists()){
                    InputStream in = res.getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte [] buff = new byte[1024];
                    int len ;
                    while((len = in.read(buff)) > 0){
                        out.write(buff,0,len);
                    }
                    in.close();
                    out.close();
                }
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    private void setArticles(Bitmap bitmap){

        Log.e("### SET ARTICLES", "Converting to Grayscale and get only price section...");
        //Bitmap cropedBitmap = this.picChanger.getOnlyPrices(bitmap, this.getPointList());
        bitmap = this.picChanger.getOnlyPrices(this.picChanger.convertBitmapGrayscale(bitmap), this.getPointList());

        Log.e("### SET ARTICLES", "Cut the Picture on " + (bitmap.getWidth()/3)*2 + "...");
        Bitmap halfLeft = this.picChanger.cutBitmapHorizontal(bitmap, (bitmap.getWidth()/3)*2)[0];
        Log.e("######## NEW SIZE","x=" + halfLeft.getWidth() + " y=" + halfLeft.getHeight());

        Bitmap halfRight = this.picChanger.cutBitmapHorizontal(bitmap, (bitmap.getWidth()/3)*2)[1];

        ArrayList<String> articleList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();

        Log.e("### SET ARTICLES", "Now get all the prices...");
        this.recognizedText = this.recognizer(halfRight);
        priceList = this.ladenInstanz.getPrices(this.recognizedText);

        Log.e("### SET ARTICLES", "Now get all the articles...");
        this.recognizedText = this.recognizerTesseract(halfLeft);
        articleList = this.ladenInstanz.getProducts(this.recognizedText);

        Log.e("##### ERGEBNIS", "Artikel=" + articleList.size() + " Preise="+ priceList.size());

        if(articleList.size() == priceList.size()){
            int count = 0;
            for(String article : articleList){
                this.articles.add(new C_Artikel(article, Double.parseDouble(priceList.get(count).replace(",","."))));
                count++;
            }
        }


    }


    /**
     * Gibt alle gefunden Artikel zurück
     * @return
     */
    public ArrayList<C_Artikel> getArticles(){
        return this.articles;
    }


    /**
     * Gibt den aktuell verwendeten Ladennamen zurück
     * @return
     */
    public String getLadenName(){
        return this.ladenName;
    }

    /**
     * Gibt alle Produkte zurück
     * @return
     */
    public ArrayList<String> getProdukte(){
        return this.produkte;
    }

    /**
     * Gibt alle Preise zurück
     * @return
     */
    public ArrayList<String> getPreise(){
        return this.preise;
    }

    /**
     * Gibt die Adresse des Bons zurück
     * @return
     */
    public String getAdresse(){
        return this.adresse;
    }

    /**
     * Gibt den komplett erkannten Text zurück
     * @return
     */
    public String getRecognizedText(){
        return this.recognizedText;
    }

    /**
     * Gibt eine Liste mit allen Block Koordinaten (Uhrzeigersinn) zurück
     */
    public ArrayList<Point> getPointList(){
        return this.pointList;
    }
}
