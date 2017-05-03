package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;

import de.projektss17.bonpix.auswerter.Default;

/**
 * Created by Domi on 14.04.2017.
 */

public class C_OCR {

    private Context context;
    private C_Laden laden;
    private String ladenName, adresse, recognizedText;
    private ArrayList<String> produkte, preise;
    private Resources res;
    private Default ladenInstanz;

    public C_OCR(Context context){
        this.context = context;
        this.res = this.context.getResources();
        this.laden = new C_Laden(this.context);
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
        this.produkte = this.ladenInstanz.getProducts(this.recognizedText);
        this.preise = this.ladenInstanz.getPrices(this.recognizedText);
        this.adresse = this.ladenInstanz.getAdresse(this.recognizedText);
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
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        return "";
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
}
