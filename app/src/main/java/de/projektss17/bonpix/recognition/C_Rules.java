package de.projektss17.bonpix.recognition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Domi on 05.04.2017.
 */

public class C_Rules {

    public String formater(String txt){
        txt = txt.replaceAll(" ", "");


        ArrayList<String> betraege = new ArrayList<>();
        ArrayList<String> betraegeReverse = new ArrayList<>();

        String dummy = "";
        int afterComma = 0;

        for(int i = 0; i < txt.length(); i++){
            if(this.isDigit(txt.charAt(i))){
                dummy += txt.charAt(i);
                if(afterComma <= 1 && afterComma < 3){
                    afterComma++;
                }
                continue;
            }
            if(this.isSeparate(txt.charAt(i))){
                dummy += txt.charAt(i);
                afterComma = 1;
                continue;
            }
            if(this.isReturn(txt.charAt(i)) || afterComma == 2){
                if(!dummy.equals("")){
                    betraege.add(dummy);
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
            if(x.contains(",") || x.contains(".") && x.length() > 3){
                rueck += (x + "\n");
            }
        }

        return rueck;
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
