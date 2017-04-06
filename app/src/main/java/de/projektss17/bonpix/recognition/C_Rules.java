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
}
