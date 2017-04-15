package de.projektss17.bonpix.recognition;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Pattern;

import de.projektss17.bonpix.R;
import de.projektss17.bonpix.auswerter.*;

/**
 * Created by Domi on 14.04.2017.
 */

public class C_Laden{

    Context context;
    Resources res;

    public C_Laden(Context context){
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
            p = Pattern.compile(laden);
            if(p.matcher(txt).find())
                return laden;
        }

        return "NOT SUPPORTED";
    }

    /**
     * Gibt eine Instanz der angegebenen Klasse zurück vom Typ Default
     * @param className Klassenname von dem man eine Instanz erwartet
     * @return Instanz der Klasse vom Typ Default
     */
    public Default getInstanceOf(String className){

        Class c = this.getAuswerterClass(className);

        Class[] cArg = new Class[1];
        cArg[0] = Context.class;

        try {
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
        }
        return null;
    }

    private Class<?> getAuswerterClass(String name){
        List<Class<?>> classes = ClassFinder.find("de.projektss17.bonpix.auswerter");
        Pattern p  = Pattern.compile(name.replaceAll(" ", ""), Pattern.CASE_INSENSITIVE);;

        for(Class<?> x : classes)
            if(p.matcher(x.getSimpleName()).find())
                return x;

        return null;
    }


}
