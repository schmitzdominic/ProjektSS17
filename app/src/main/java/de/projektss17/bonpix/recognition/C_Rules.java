package de.projektss17.bonpix.recognition;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Domi on 05.04.2017.
 */

public class C_Rules {

    public String formater(String txt){
        txt = txt.replaceAll(" ", "");
        return txt;
    }

    public String getPrices(String txt){
        ArrayList<String> betraege = new ArrayList<>();
        ArrayList<String> betraegeReverse = new ArrayList<>();

        String dummy = "";
        int afterComma = 0;

        for(int i = 0; i < txt.length(); i++){
            if(this.isDigit(txt.charAt(i))){
                if(afterComma == 0) {
                    dummy += txt.charAt(i);
                    continue;
                }
                if(afterComma > 0 && afterComma < 3){
                    dummy += txt.charAt(i);
                    afterComma++;
                    continue;
                }
            }
            if(this.isSeparate(txt.charAt(i)) &&
                    afterComma == 0 &&
                    dummy.length() > 0){
                if(txt.charAt(i) == '.'){
                    dummy += ',';
                }else{
                    dummy += txt.charAt(i);
                }
                afterComma = 1;
                continue;
            }
            if(this.isReturn(txt.charAt(i)) || afterComma == 2){
                if(!dummy.equals("") &&
                        dummy.contains(",")){
                    betraege.add(dummy);
                    Log.i("",""+dummy);
                    dummy = "";
                    afterComma = 0;
                } else {
                    dummy = "";
                    afterComma = 0;
                }
            }
        }

        String rueck = "";

        for(String x : betraege){
            betraegeReverse.add(x);
        }

        for(String x : betraegeReverse){
            if(this.isPriceOK(x)){
                rueck += (x + "\n");
            }
        }
        return rueck;
    }

    public boolean isPriceOK(String x){
        return ((x.contains(",") ||
                x.contains(".")) &&
                x.length() > 3);
    }

    public boolean isDigit(char c){
        return (c >= '0' && c <= '9');
    }

    public boolean isReturn(char c){
        return (c == '\n');
    }

    public boolean isSeparate(char c){
        return (c == ',' || c == '.');
    }

    public boolean isLetter(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    public String getPLZ(String txt){

        int count = 0;
        String zahl = "";

        for(int i = 0; i < txt.length(); i++){
            if(isDigit(txt.charAt(i))){
                count++;
                zahl += txt.charAt(i);
            } else {
                count = 0;
                zahl = "";
            }
            if(count == 5){
                return zahl;
            }
        }
        return "PLZ NICHT ERKANNT";
    }

    public String getTel(String txt){

        if(txt.contains("Tel") || txt.contains("TEL") || txt.contains("tel")) {
            String tel[];
            if(txt.contains("Tel")) {
                tel = txt.split("Tel");
            } else if (txt.contains("TEL")) {
                tel = txt.split("TEL");
            } else if (txt.contains("tel")) {
                tel = txt.split("tel");
            } else {
                tel = new String[2];
            }
            String retNumber = "";
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
            return "KEINE TELEFONNUMMER ERKANNT!";
        }
    }
}
