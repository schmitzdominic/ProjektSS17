package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.List;

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

    private void setArticles(Bitmap bitmap){

        Log.e("### SET ARTICLES", "Converting to Grayscale and get only price section...");
        Bitmap cropedBitmap = this.picChanger.getOnlyPrices(bitmap, this.getPointList());
        //bitmap = this.picChanger.getOnlyPrices(this.picChanger.convertBitmapGrayscale(bitmap), this.getPointList());

        Log.e("### SET ARTICLES", "Cut the Picture on " + (cropedBitmap.getWidth()/3)*2 + "...");
        Bitmap halfLeft = this.picChanger.cutBitmapHorizontal(cropedBitmap, (cropedBitmap.getWidth()/3)*2)[0];
        Log.e("######## NEW SIZE","x=" + halfLeft.getWidth() + " y=" + halfLeft.getHeight());

        Bitmap halfRight = this.picChanger.cutBitmapHorizontal(cropedBitmap, (cropedBitmap.getWidth()/3)*2)[1];

        ArrayList<String> articleList = new ArrayList<>();
        ArrayList<String> priceList = new ArrayList<>();

        Log.e("### SET ARTICLES", "Now get all the prices...");
        this.recognizedText = this.recognizer(halfRight);
        priceList = this.ladenInstanz.getPrices(this.recognizedText);

        Log.e("### SET ARTICLES", "Now get all the articles...");
        this.recognizedText = this.recognizer(halfLeft);
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
