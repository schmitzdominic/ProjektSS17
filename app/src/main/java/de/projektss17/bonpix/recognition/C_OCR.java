package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import de.projektss17.bonpix.auswerter.Default;

/**
 * Created by Domi on 14.04.2017.
 */

public class C_OCR {

    private Context context;
    private Resources res;
    private C_Laden laden;
    private String ladenName, adresse, recognizedText;
    private String[] produkte, preise;
    private Default ladenInstanz;

    public C_OCR(Context context){
        this.context = context;
        this.res = this.context.getResources();
        this.laden = new C_Laden(this.context);
    }

    public void recognize(Bitmap bitmap){

        this.recognizedText = this.recognizer(bitmap);

        // Um welchen Laden handelt es sich
        this.ladenName = this.laden.getLaden(this.recognizedText);

        // Instanz des Ladens bilden
        if(!this.ladenName.equals("NOT SUPPORTED")){
            this.ladenInstanz = laden.getInstanceOf(this.ladenName);
        } else {
            this.ladenInstanz = laden.getInstanceOf("Default");
        }

        // Attribute setzen
        this.produkte = this.ladenInstanz.getProducts(this.recognizedText);
        this.preise = this.ladenInstanz.getProducts(this.recognizedText);
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
     * Gibt alle Produkte zurück
     * @return
     */
    public String[] getProdukte(){
        return this.produkte;
    }

    /**
     * Gibt alle Preise zurück
     * @return
     */
    public String[] getPreise(){
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
