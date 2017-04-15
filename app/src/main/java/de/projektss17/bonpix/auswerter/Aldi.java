package de.projektss17.bonpix.auswerter;

import android.content.Context;

/**
 * Created by Domi on 15.04.2017.
 */

public class Aldi extends Default {

    private String name = "Aldi";

    public Aldi(Context context){
        super(context);
    }

    @Override
    public String[] getProducts(String txt) {
        return super.getProducts(txt);
    }

    @Override
    public String[] getPrices(String txt) {
        return super.getPrices(txt);
    }

    @Override
    public String toString(){
        return this.name;
    }
}
