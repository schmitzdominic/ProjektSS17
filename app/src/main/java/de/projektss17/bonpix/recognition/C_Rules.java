package de.projektss17.bonpix.recognition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domi on 05.04.2017.
 */

public class C_Rules {

    public String formater(String txt){
        txt = txt.replaceAll(" ", "");

        List<String> betraege = new ArrayList();

        String dummy = "";

        for(int i = 0; i < txt.length(); i++){
            if(this.isDigit(txt.charAt(i))){
                dummy += txt.charAt(i);
            }
            if(this.isSeparate(txt.charAt(i))){
                dummy += txt.charAt(i);
            }
            if(this.isReturn(txt.charAt(i))){
                betraege.add(dummy);
            }
        }

        String rueck = "";

        for(String x : betraege){
            rueck += (x + "\n");
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
