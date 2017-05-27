package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.S;
import de.projektss17.bonpix.auswerter.Default;
import de.projektss17.bonpix.daten.C_Artikel;
import de.projektss17.bonpix.exceptions.E_NoBonFoundException;

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
    public boolean recognize(Bitmap bitmap){

        this.recognizedText = this.recognizer(bitmap);
        return this.recognize(bitmap, this.laden.getLaden(this.recognizedText));

    }

    /**
     * Ließt anhand von OCR alle Zeichen aus und schreibt diese
     * in die Instanzvariablen
     * @param bitmap Bild das ausgewertet werden soll
     * @param ladenName Der Laden um den es sich handelt (Bitte auswerter.xml beachten!)
     */
    public boolean recognize(Bitmap bitmap, String ladenName){

        if(articles != null){
            this.articles.clear();
        }

        if(preise != null){
            this.preise.clear();
        }

        // Wenn der Text noch nicht ausgelesen wurde
        if(this.recognizedText == null){
            this.recognizedText = this.recognizer(bitmap);
        }

        this.ladenName = ladenName;

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

        if(this.recognizedText != null && !this.recognizedText.equals("") && !this.recognizedText.isEmpty()){
            this.adresse = this.ladenInstanz.getAdresse(this.recognizedText);
            Log.e("RECOART","######################## " + this.ladenInstanz.getRecognizeArt());
            if(this.ladenInstanz.getRecognizeArt() == 1){
                return this.setArticlesArt1(bitmap);
            } else {
                return this.setArticlesArt2(bitmap);
            }

        } else {
            S.outLong((AppCompatActivity)(this.context), this.res.getString(R.string.c_ocr_kassenzettel_nicht_erkannt));
            return false;
        }
    }

    /**
     * Holt über OCR die Infos aus der Bitmap heraus
     * @param bitmap Bild
     * @return String mit Informationen
     */
    private String recognizer(Bitmap bitmap) {
        this.pointList.clear();
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this.context).build();
        if (!textRecognizer.isOperational()) {
            Log.e("ERROR", "Detector dependencies are not yet available");
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame); // TODO Absturz wenn Frame kein Text enthält.

            StringBuilder stringBuilder = new StringBuilder();

            if(items.size() != 0){
                for (int i = 0; i < items.size(); ++i) {
                    TextBlock item = items.valueAt(i);
                    if(item.getCornerPoints() != null){
                        for(Point point : item.getCornerPoints()){
                            if(point != null){
                                this.pointList.add(point);
                            }

                        }
                    }
                    stringBuilder.append(item.getValue());
                    stringBuilder.append("\n");
                }
            }

            if(stringBuilder.length() != 0){
                return stringBuilder.toString();
            } else {
                return "";
            }

        }
        return "";
    }

    /**
     * Setzt die Artikel, sofern das Möglich ist
     * @param bitmap Original Bitmap
     * @return möglich - true, Nicht möglich - false
     */
    private boolean setArticlesArt1(Bitmap bitmap){

        try{
            ArrayList<String> articleList = new ArrayList<>(), priceList = new ArrayList<>();
            ArrayList<Bitmap> articleStripes = new ArrayList<>();
            Bitmap cropedBitmap, halfLeft, halfRight;

            // Es wird nur die Artikel/Preisregion herausgeschnitten
            if(this.getPointList().size() != 0){
                cropedBitmap = this.picChanger.getOnlyArticleArea(bitmap, this.getPointList());
            } else {
                throw new E_NoBonFoundException(this.context, "## C_OCR - SET ARTICLES", "ERROR: POINTLIST=" + this.getPointList().size());
            }

            // Bitmap wird in 2 hälften 2/3 geschitten
            if(cropedBitmap != null){
                halfLeft = this.picChanger.cutBitmapHorizontal(cropedBitmap, (cropedBitmap.getWidth()/3)*2)[0];
                halfRight = this.picChanger.cutBitmapHorizontal(cropedBitmap, (cropedBitmap.getWidth()/3)*2)[1];
            } else {
                throw new E_NoBonFoundException(this.context, "## C_OCR - SET ARTICLES", "ERROR: CROPETBITMAP=NULL");
            }

            // Artikelseite
            this.recognizedText = this.recognizer(halfLeft);
            if(this.recognizedText != null && !this.recognizedText.equals("") && !this.recognizedText.isEmpty()){
                articleList = this.ladenInstanz.getProducts(this.recognizedText);
                if(articleList.size() != 0){
                    articleStripes = this.picChanger.getLineList(halfLeft, halfLeft.getHeight()/articleList.size());
                } else {
                    throw new E_NoBonFoundException(this.context, "## C_OCR - SET ARTICLES", "ERROR: ARTICLESTRIPES=" + articleStripes.size());
                }
            } else {
                throw new E_NoBonFoundException(this.context, "## C_OCR - SET ARTICLES", "ERROR: RECOGNIZEDTEXT=NULL OR \"\"");
            }

            // Preisseite
            this.recognizedText = this.recognizer(halfRight);
            if(this.recognizedText != null && !this.recognizedText.equals("") && !this.recognizedText.isEmpty()){
                priceList = this.ladenInstanz.getPrices(this.recognizedText);
            } else {
                throw new E_NoBonFoundException(this.context, "## C_OCR - SET ARTICLES", "ERROR: RECOGNIZEDTEXT=NULL OR \"\"");
            }

            Log.e("##### ERGEBNIS", "Artikel=" + articleList.size() + " Preise="+ priceList.size());

            // Wenn die Anzahl gleich ist, dann trag alle Artikel ein / Sonst nicht
            if(articleList.size() == priceList.size()){
                int count = 0;
                for(Bitmap article : articleStripes){

                    this.recognizedText = this.recognizer(article);
                    this.produkte = this.ladenInstanz.getProducts(this.recognizedText);

                    if(this.recognizedText != null && !this.recognizedText.equals("") && !this.recognizedText.isEmpty() && this.produkte.size() != 0){
                        this.articles.add(new C_Artikel(this.ladenInstanz.getProducts(this.recognizedText).get(0), Double.parseDouble(priceList.get(count).replace(",","."))));
                    } else {
                        this.articles.add(new C_Artikel(articleList.get(count), Double.parseDouble(priceList.get(count).replace(",","."))));
                    }
                    count++;
                }
                return true;
            } else {
                throw new E_NoBonFoundException(this.context,  "## C_OCR - SET ARTICLES", "ARTICLE SIZE DON´T MATCH PRICE SIZE");
            }

        } catch (E_NoBonFoundException e){
            return false;
        }
    }

    private boolean setArticlesArt2(Bitmap bitmap){
        try {
            ArrayList<Bitmap> articleStripes = new ArrayList<>();
            Bitmap cropedBitmap;

            // Es wird nur die Artikel/Preisregion herausgeschnitten
            if (this.getPointList().size() != 0) {
                cropedBitmap = this.picChanger.getOnlyArticleArea(bitmap, this.getPointList());
            } else {
                throw new E_NoBonFoundException(this.context, "## C_OCR - SET ARTICLES", "ERROR: POINTLIST=" + this.getPointList().size());
            }

            this.recognizedText = this.recognizer(cropedBitmap);

            int lines = (this.ladenInstanz.getProducts(this.recognizedText).size() < this.ladenInstanz.getPrices(this.recognizedText).size()) ?
                    this.ladenInstanz.getProducts(this.recognizedText).size() :
                    this.ladenInstanz.getPrices(this.recognizedText).size();

            articleStripes = this.picChanger.getLineList(cropedBitmap, (int) ((cropedBitmap.getHeight() / lines) * 1.1));

            Log.e("STRIPES COUNT", articleStripes.size() + "");
            if (articleStripes.size() == 0) {
                return false;
            }

            for (Bitmap bit : articleStripes) {
                this.recognizedText = this.recognizer(bit);
                Log.e("OUT LINE", recognizedText);
                Log.e("PRDUKTE", "ARTIKEL=" + this.ladenInstanz.getProducts(this.recognizedText).size() + " PREIS=" + this.ladenInstanz.getPrices(this.recognizedText).size());

                if (this.ladenInstanz.getProducts(this.recognizedText).size() == this.ladenInstanz.getPrices(this.recognizedText).size()) {
                    if (this.ladenInstanz.getProducts(this.recognizedText).size() != 0) {
                        this.articles.add(new C_Artikel(this.ladenInstanz.getProducts(this.recognizedText).get(0), Double.parseDouble(this.ladenInstanz.getPrices(this.recognizedText).get(0).replace(",","."))));
                        Log.e("MATCH", this.ladenInstanz.getProducts(this.recognizedText).get(0) + " - " + this.ladenInstanz.getPrices(this.recognizedText).get(0));
                    }

                }
            }
            return true;

        } catch (E_NoBonFoundException e){
            return false;
        }
    }


    /**
     * Gibt alle gefunden Artikel zurück
     * @return Alle Artikel
     */
    public ArrayList<C_Artikel> getArticles(){
        return this.articles;
    }


    /**
     * Gibt den aktuell verwendeten Ladennamen zurück
     * @return Ladenname
     */
    public String getLadenName(){
        return this.ladenName;
    }

    /**
     * Gibt alle Produkte zurück
     * @return Alle Produkte
     */
    public ArrayList<String> getProdukte(){
        return this.produkte;
    }

    /**
     * Gibt alle Preise zurück
     * @return Alle Preise
     */
    public ArrayList<String> getPreise(){
        return this.preise;
    }

    /**
     * Gibt die Adresse des Bons zurück
     * @return Adresse
     */
    public String getAdresse(){
        return this.adresse;
    }

    /**
     * Gibt den komplett erkannten Text zurück
     * @return Der zuletzt erkannte Text
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
