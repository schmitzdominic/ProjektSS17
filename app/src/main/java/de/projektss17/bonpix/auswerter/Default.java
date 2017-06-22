package de.projektss17.bonpix.auswerter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Default implements I_Auswerter{

    protected Context context;
    protected Resources res;

    public Default(Context context){
        this.context = context;
        this.res = this.context.getResources();
    }

    @Override
    public ArrayList<String> getProducts(String txt) {

        String dumString = "";
        ArrayList<String> retString = new ArrayList<>();

        txt = txt.replaceAll(" +"," ");
        txt = txt.replaceAll("\\d","");
        txt = txt.replaceAll(" \\w ", "");
        txt = txt.replaceAll(" \\w\n", "\n");

        for(int i = 0; i < txt.length(); i++){
            if(txt.charAt(i) == '\n'){
                if(dumString.length() > 4 && !dumString.contains("storno") && !dumString.contains("STORNO")){
                    if(dumString.contains("pfand") || dumString.contains("PFAND") || dumString.contains("Pfand")){
                        dumString = "Pfand";
                    }
                    retString.add(dumString);
                }
                dumString = "";
            } else {
                if(this.isLetter(txt.charAt(i)) || txt.charAt(i) == '.' || txt.charAt(i) == '&' || txt.charAt(i) == ' '){
                    dumString += txt.charAt(i);
                }
            }
        }

        return retString;

    }

    @Override
    public ArrayList<String> getPrices(String txt) {

        String dumString = "";
        ArrayList<String> retString = new ArrayList<>();
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
                    if((i - dumString.length() - 1) >= 0){
                        if(txt.charAt(i - dumString.length() - 1) == '-'){
                            retString.add("-" + dumString);
                        } else {
                            retString.add(dumString);
                        }
                    } else {
                        retString.add(dumString);
                    }
                }
                dumString = "";
                count = 0;
                continue;
            }
        }

        return retString;
    }

    public int getRecognizeArt(){
        return 2;
    }

    public double getCorrection(){
        return 0;
    }

    public int getFirstLine(){
        return 0;
    }

    public double getDefaultSize(){
        return 1.1;
    }

    /**
     * Liest die Postleitzahl aus einem String aus
     * @param txt
     * @return Postleitzahl String
     */
    public String getAdress(String txt){

        String adressReg = "(\\d{5})\\s(\\w{1,20})";

        Pattern c = Pattern.compile(adressReg);
        Matcher m = c.matcher(txt);

        if(m.find()){
            try {
                Log.e("GROUP", m.group());
                return m.group();
            } catch(IllegalStateException e){
                return "KEINE ADRESSE GEFUNDEN!";
            }
        }

        return "KEINE ADRESSE GEFUNDEN!";
    }

    /**
     * Liest die Telefonnummer aus einem String aus
     * @param txt
     * @return String
     */
    public String getTel(String txt){

        String telReg = "(\\d{3,6})\\D(\\d{3,6})\\D(\\d{3,6})|"+    // Art 0821/3882-4839
                "(\\d{3,6})\\D(\\d{4,10})|"+                // Art 0821/4838284
                "(\\d{8,15})",                              // Art 080722284
                telx[];

        Pattern c = Pattern.compile("telefon|tel",Pattern.CASE_INSENSITIVE);
        Matcher m = c.matcher(txt);

        if(m.find()){
            telx = txt.split(m.group());
            telx[1] = telx[1];
            c = Pattern.compile(telReg);
            m = c.matcher(telx[1]);
            m.find();
            try {
                return m.group();
            } catch(IllegalStateException e){
                return "KEINE TELEFONNUMMER GEFUNDEN!";
            }
        }

        return "KEINE TELEFONNUMMER GEFUNDEN!";
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
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == 'ä') || (c == 'Ä') || (c == 'ü') || (c == 'Ü') || (c == 'ö') || (c == 'Ö') || (c == 'ß'));
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
