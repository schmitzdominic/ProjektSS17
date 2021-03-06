package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.auswerter.*;

public class C_MatchLaden {

    Context context;
    Resources res;

    /**
     * Standard Constructor
     * @param context
     */
    public C_MatchLaden(Context context){
        this.context = context;
        this.res = this.context.getResources();
    }

    /**
     * Gibt den Laden als String zurück sofern er einen findet
     * Wenn nicht, gibt er NOT SUPPORTET zurück.
     * @param txt Text der Nach läden durchsucht werden soll
     * @return laden bei Fund, NOT SUPPORTET bei nicht auffinden
     */
    public String getLaden(String txt){

        String[] laeden = res.getStringArray(R.array.supp_laeden);

        Pattern p;

        for(String laden : laeden){
            p = Pattern.compile(laden.replaceAll(" ",""), Pattern.CASE_INSENSITIVE);

            if(p.matcher(txt.replaceAll(" ","")).find()){
                return laden;
            }
        }
        return "NOT SUPPORTED";
    }

    /**
     * Gibt eine Instanz der angegebenen Klasse zurück vom Typ Default
     * @param className Klassenname von dem man eine Instanz erwartet
     * @return Instanz der Klasse vom Typ Default
     */
    public Default getInstanceOf(String className){

        try {
            Class c = Class.forName("de.projektss17.bonpix.auswerter."+this.getAuswerterClass(className));
            Class[] cArg = new Class[1];
            cArg[0] = Context.class;

            Object d = c.getDeclaredConstructor(cArg).newInstance(this.context);
            if(d != null){
                return (Default) d;
            } else {
                return null;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gibt den Klassennamen zurück
     * @param name name nachdem gesucht werden soll
     * @return Klassenname
     */
    public String getAuswerterClass(String name){

        String [] classArray = this.context.getResources().getStringArray(R.array.klassen);
        Pattern p  = Pattern.compile(name.replaceAll(" ", ""), Pattern.CASE_INSENSITIVE);

        for(String c : classArray)
            if(p.matcher(c).find())
                return c;
        return null;
    }
}