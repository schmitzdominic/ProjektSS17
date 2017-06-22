package de.projektss17.bonpix.auswerter;

import android.content.Context;

import java.util.ArrayList;

public class Netto extends Default {

    private String name = "Netto";

    public Netto(Context context){
        super(context);
    }

    @Override
    public ArrayList<String> getProducts(String txt) {
        return super.getProducts(txt);
    }

    @Override
    public ArrayList<String> getPrices(String txt) {

        txt = txt.replaceAll("\\. ",".");
        txt = txt.replaceAll(", ",".");
        txt = txt.replaceAll("#", "");

        ArrayList<String> prices = super.getPrices(txt);
        ArrayList<String> onePrice = new ArrayList<>();

        if(prices.size() == 2){
            onePrice.add(prices.get(1));
            return onePrice;
        } else {
            return prices;
        }
    }

    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public int getRecognizeArt() {
        return 2;
    }

    @Override
    public double getDefaultSize(){
        return 1;
    }

    @Override
    public double getCorrection(){
        return 0.3 ;
    }

    @Override
    public int getFirstLine(){
        return 1;
    }
}
