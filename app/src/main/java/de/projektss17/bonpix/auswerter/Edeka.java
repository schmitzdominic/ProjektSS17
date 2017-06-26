package de.projektss17.bonpix.auswerter;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Edeka extends Default {

    private String name = "Edeka";
    private boolean SUMME = false;

    public Edeka(Context context){
        super(context);
    }

    @Override
    public ArrayList<String> getProducts(String txt) {

        txt = txt.replaceAll("Sofortstorno","");
        txt = txt.replaceAll("Posten", "");
        txt = txt.replaceAll("E\\.", "");
        txt = txt.replaceAll("E\\. +", "");

        ArrayList<String> products = super.getProducts(txt);

        if(products.size() != 0){
            if(products.get(products.size()-1).contains("SUMME")){
                products.remove(products.size()-1);
                this.SUMME = true;
            }
        }

        return products;
    }

    @Override
    public ArrayList<String> getPrices(String txt) {

        ArrayList<String> prices = super.getPrices(txt);

        if(this.SUMME){
            prices.remove(prices.size()-1);
        }

        return prices;
    }

    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public int getRecognizeArt() {
        return 1;
    }

    public double getCorrection(){
        return 0.1;
    }
}
