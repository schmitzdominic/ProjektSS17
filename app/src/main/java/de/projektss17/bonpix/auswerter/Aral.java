package de.projektss17.bonpix.auswerter;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Domi on 15.04.2017.
 */

public class Aral extends Default {

    private String name = "Aral";

    public Aral(Context context){
        super(context);
    }

    @Override
    public ArrayList<String> getProducts(String txt) {
        return super.getProducts(txt);
    }

    @Override
    public ArrayList<String> getPrices(String txt) {
        return super.getPrices(txt);
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
        return 1.0;
    }
}
