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
        S.startActivity(beforeActivity, OTHERACTVITIY.class);
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
        S.startActivity(beforeActivity, A_Budget.class);
    }

    /**
     * Ruft die Gruppen Activity auf
     */
    public static void showGruppen(AppCompatActivity beforeActivity){
        S.startActivity(beforeActivity, A_Gruppen.class);
    }

    /**
     * Ruft die Favoriten Activity auf
     */
    public static void showFavoriten(AppCompatActivity beforeActivity){
        S.startActivity(beforeActivity, A_Favoriten.class);
    }

    /**
     * Ruft die Garantie Activity auf
     */
    public static void showGarantie(AppCompatActivity beforeActivity){
        S.startActivity(beforeActivity, A_Garantie.class);
    }

    /**
     * Ruft die Budget Activity auf
     */
    public static void showEinstellungen(AppCompatActivity beforeActivity){

        S.startActivity(beforeActivity, A_Einstellungen.class);
    }

    /**
     * Startet Activitys
     * @param beforeActivity Vorherige Instanz der Activity
     * @param cls Activity Klasse die gestartet werden soll
     */
    public static void startActivity(AppCompatActivity beforeActivity, Class<?> cls){
        Intent intent = new Intent(beforeActivity, cls);
        beforeActivity.startActivity(intent);
    }

}
