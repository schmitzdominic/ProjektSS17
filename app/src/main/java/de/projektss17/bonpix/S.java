package de.projektss17.bonpix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import de.projektss17.bonpix.daten.C_DatabaseHandler;
import de.projektss17.bonpix.daten.C_Preferences;

/**
 * Created by Domi on 29.03.2017.
 * Diese KLasse wird verwendet um den kompletten statischen Kontent aufzunehmen
 */

public class S extends Activity {

    public static C_DatabaseHandler dbHandler;
    public static SQLiteDatabase db;
    public static C_Preferences prefs;

    /**
     * Ruft die Foto funktion auf
     */
    public static void showFoto(AppCompatActivity beforeActivity){
        // TODO Kamera Klasse implementieren und die benötigten Einstellungen bauen
    }

    /**
     * Ruft die Recognition Activity auf
     */
    public static void showRecognition(AppCompatActivity beforeActivity, ArrayList<String> path){
        Intent intent = new Intent(beforeActivity, A_Show_Recognition.class);
        intent.putExtra("ArrayList",path);
        beforeActivity.startActivity(intent);
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
        S.startActivitiy(beforeActivity, A_Budget.class);
    }

    /**
     * Ruft die Gruppen Activity auf
     */
    public static void showGruppen(AppCompatActivity beforeActivity){
        S.startActivitiy(beforeActivity, A_Gruppen.class);
    }

    /**
     * Ruft die Favoriten Activity auf
     */
    public static void showFavoriten(AppCompatActivity beforeActivity){
        S.startActivitiy(beforeActivity, A_Favoriten.class);
    }

    /**
     * Ruft die Garantie Activity auf
     */
    public static void showGarantie(AppCompatActivity beforeActivity){
        S.startActivitiy(beforeActivity, A_Garantie.class);
    }

    /**
     * Ruft die Einstellungen Activity auf
     */
    public static void showEinstellungen(AppCompatActivity beforeActivity){

        S.startActivitiy(beforeActivity, A_Einstellungen.class);
    }

    /**
     * Ruft die Versions Activity auf
     */
    public static void showVersion(PreferenceActivity context){

        Intent intent = new Intent(context, A_Version.class);
        context.startActivity(intent);
    }

    /**
     * Startet Activitys
     * @param beforeActivity Vorherige Instanz der Activity
     * @param cls Activity Klasse die gestartet werden soll
     */
    public static void startActivitiy(AppCompatActivity beforeActivity, Class<?> cls){
        Intent intent = new Intent(beforeActivity, cls);
        beforeActivity.startActivity(intent);
    }

    /**
     * Ausgabe eines Strings (kurz)
     * Ausgabe erfolgt über einen Toast
     * @param context Class.this (instanz übergeben)
     * @param msg Text der Ausgegeben werden soll
     */
    public static void outShort(AppCompatActivity context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Ausgabe eines Strings (lang)
     * Ausgabe erfolgt über einen Toast
     * @param context Class.this (instanz übergeben)
     * @param msg Text der Ausgegeben werden soll
     */
    public static void outLong(AppCompatActivity context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
