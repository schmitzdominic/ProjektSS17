package de.projektss17.bonpix.recognition;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Domi on 05.04.2017.
 * Diese Klasse enthält Regeln
 */

public class C_Rules {

    /**
     * Setzt ein gewisses Standardformat
     * @param txt
     * @return String
     */
    public String formater(String txt){
        txt = txt.replaceAll(" ", "");
        return txt;
    }

    /**
     * Versucht nur die Preise auszulesen.
     * TODO noch nicht fertig!
     * @param txt Kompletter String
     * @return nur die Preise
     */
    public String getPrices(String txt){

        String retString = "",
                dumString = "";
        int count = 0;

        for(int i = 0; i < txt.length(); i++){

            if(this.isDigit(txt.charAt(i))){
                if(count == 0){
                    dumString += txt.charAt(i);
                    continue;
                } else if(count > 0 && count < 3){
                    dumString += txt.charAt(i);
                    count++;
                    continue;
                } else if(count >= 3){
                    count++;
                    dumString = "";
                    continue;
                } else {
                    dumString = "";
                    count = 0;
                    continue;
                }

            } else if(this.isSeparate(txt.charAt(i))){

                if(count == 0){
                    if(txt.charAt(i) == '.'){
                        dumString += ',';
                    } else {
                        dumString += txt.charAt(i);
                    }
                    count++;
                    continue;
                } else {
                    count = 0;
                    dumString = "";
                    continue;
                }
            } else {
                if(count == 3){
                    retString += dumString + "\n";
                }
                dumString = "";
                count = 0;
                continue;
            }
        }

        return retString;
    }

    /**
     * Liest die Postleitzahl aus einem String aus
     * @param txt
     * @return Postleitzahl String
     */
    public String getPLZ(String txt){

        int count = 0;
        String zahl = "";
        boolean block = false;

        for(int i = 0; i < txt.length(); i++){

            if(isDigit(txt.charAt(i)) && count == 5){
                count = 0;
                zahl = "";
                block = true;
            } else if(!isDigit(txt.charAt(i))){
                block = false;
            }

            if(count == 5 && isLetter(txt.charAt(i))){
                return zahl;
            }

            if(isDigit(txt.charAt(i)) && !block){
                count++;
                zahl += txt.charAt(i);
                continue;
            } else {
                count = 0;
                zahl = "";
                continue;
            }
        }
        return "PLZ NICHT ERKANNT";
    }

    /**
     * Liest die Telefonnummer aus einem String aus
     * @param txt
     * @return String
     */
    public String getTel(String txt){

        // Existiert überhaupt eine Telefonnummer im String?
        if(txt.contains("Tel") ||
                txt.contains("TEL") ||
                txt.contains("tel") ||
                txt.contains("Telefon")) {
            String tel[];
            // Falls Ja, bitte splitte den String an der Stelle
            if(txt.contains("Telefon")) {
                tel = txt.split("Telefon");
            } else if (txt.contains("TEL")) {
                tel = txt.split("TEL");
            } else if (txt.contains("tel")) {
                tel = txt.split("tel");
            } else if (txt.contains("Tel")) {
                tel = txt.split("Tel");
            } else {
                tel = new String[2];
            }
            String retNumber = "";
            // Ließ 50 Charaktere nach dem split aus und gib die nächsten
            // Zahlen bis zu einem Charakter zurück
            for(int i = 0; i < 50; i++){
                if(this.isDigit(tel[1].charAt(i))){
                    retNumber += tel[1].charAt(i);
                }
                if(this.isLetter(tel[1].charAt(i))){
                    break;
                }
            }
            return retNumber;
        } else {
            // Falls nein, bitte gib eine Meldung zurück
            return "KEINE TELEFONNUMMER ERKANNT!";
        }
    }

    /**
     * Wenn der Preis die Richtlinien erfüllt dann true, sonst false
     * @param x
     * @return true oder false
     */
    public boolean isPriceOK(String x){
        return ((x.contains(",") ||
                x.contains(".")) &&
                x.length() > 3);
    }

    /**
     * Ist der übergebene char eine Zahl? true ja, false nein
     * @param c
     * @return true, false
     */
    public boolean isDigit(char c){
        return (c >= '0' && c <= '9');
    }

    /**
     * Ist der übergebene char ein Buchstabe? true ja, false nein
     * @param c
     * @return true, false
     */
    public boolean isLetter(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    /**
     * Ist der übergebene char ein return? true ja, false nein
     * @param c
     * @return true, false
     */
    public boolean isReturn(char c){
        return (c == '\n');
    }

    /**
     * Ist der übergeben char ein separator? true ja, false nein
     * @param c
     * @return true, false
     */
    public boolean isSeparate(char c){
        return (c == ',' || c == '.');
    }
}
