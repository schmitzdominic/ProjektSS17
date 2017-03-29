package de.projektss17.bonpix;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Domi on 29.03.2017.
 * Diese KLasse wird verwendet um den kompletten statischen Kontent aufzunehmen
 */

public class S extends Activity {
    /**
     * Ruft die Foto funktion auf
     */
    public static void showFoto(AppCompatActivity beforeActivity){
        // TODO Aufruf der e_kamera_ocr
        /**Beispiel:
        Intent intent = new Intent(beforeActivity, OTHERACTIVITY.class);
        beforeActivity.startActivity(intent);
        */
    }

    /**
     * Ruft die Manuell Activity auf
     */
    public static void showManuell(AppCompatActivity beforeActivity){
        // TODO Aufruf der Manuell Funktion
    }

    /**
     * Ruft die Budget Activity auf
     */
    public static void showBudget(AppCompatActivity beforeActivity){
        // TODO Activity A_Budget aufrufen
    }

    /**
     * Ruft die Gruppen Activity auf
     */
    public static void showGruppen(AppCompatActivity beforeActivity){
        // TODO Activity A_Gruppen aufrufen

    }

    /**
     * Ruft die Favoriten Activity auf
     */
    public static void showFavoriten(AppCompatActivity beforeActivity){
        // TODO Activity A_Favoriten aufrufen
    }

    /**
     * Ruft die Garantie Activity auf
     */
    public static void showGarantie(AppCompatActivity beforeActivity){
        // TODO Activity A_Garantie aufrufen
    }

    /**
     * Ruft die Budget Activity auf
     */
    public static void showEinstellungen(AppCompatActivity beforeActivity){
        // TODO Activity A_Einstellungen aufrufen
    }

}
