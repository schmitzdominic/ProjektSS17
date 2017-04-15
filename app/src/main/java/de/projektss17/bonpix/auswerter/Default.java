package de.projektss17.bonpix.auswerter;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Domi on 15.04.2017.
 */

public class Default implements I_Auswerter{

    protected Context context;
    protected Resources res;

    public Default(Context context){
        this.context = context;
        this.res = this.context.getResources();
    }

    @Override
    public String[] getProducts(String txt) {
        return new String[0];
    }

    @Override
    public String[] getPrices(String txt) {
        return new String[0];
    }

    public String getAdresse(String txt){
        return "";
    }
}
