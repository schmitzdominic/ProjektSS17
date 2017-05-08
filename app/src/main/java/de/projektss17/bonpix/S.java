package de.projektss17.bonpix;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import de.projektss17.bonpix.daten.C_AssetHelper;
import de.projektss17.bonpix.daten.C_DatabaseHandler;
import de.projektss17.bonpix.daten.C_Preferences;

/**
 * Created by Domi on 29.03.2017.
 * Diese KLasse wird verwendet um den kompletten statischen Kontent aufzunehmen
 */

public class S extends Activity {

    public static C_DatabaseHandler dbHandler; // DB-Handler
    public static C_AssetHelper dbArtikelHandler; // DB-Artikel-Handler
    public static SQLiteDatabase db, dbArtikel; // DB
    public static C_Preferences prefs; // Preferences

    /**
     * Ruft die Manuell Activity auf
     */
    public static void showManuell(AppCompatActivity beforeActivity, ArrayList<String> path, String state){

        Intent intent = new Intent(beforeActivity, A_OCR_Manuell.class);
        intent.putExtra("ArrayList",path);
        intent.putExtra("manuellState",state);
        beforeActivity.startActivity(intent);
    }

    /**
     * Ruft die Manuell Activity auf (überschrieben)
     */
    public static void showManuell(AppCompatActivity beforeActivity,  String state){

        Intent intent = new Intent(beforeActivity, A_OCR_Manuell.class);
        intent.putExtra("manuellState",state);
        beforeActivity.startActivity(intent);
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
     * Ruft die Backup Activity auf
     */
    public static void showBackup(PreferenceFragment context){
        Intent intent = new Intent(context.getActivity(), A_Backup.class);
        context.startActivity(intent);
    }

    /**
     * Ruft die Versions Activity auf
     */
    public static void showVersion(PreferenceActivity context){
        Intent intent = new Intent(context, A_Version.class);
        context.startActivity(intent);
    }

    /**
     * Ruft die Max Bon Activity auf
     */
    public static void showMaxBonPic(AppCompatActivity beforeActivity, String uriPath){
        Intent intent = new Intent(beforeActivity, A_Max_Bon_Pic.class);
        intent.putExtra("imageUri", uriPath);
        beforeActivity.startActivity(intent);
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
     * Öffnet ein POPUP-Fenster (Hinweis/Alert) beim Aufruf
     * @param beforeActivity Vorherige Instanz der Activity
     * @param afterActivity Activity Klasse die gestartet werden soll
     * @param title Legt den Titel des PopUp-Fensters fest (wird im Fenster angezeigt)
     * @param message Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param cancel Legt den Ihnalt des Cancel-Buttons fest
     * @param confirm Legt den Inhalt des Confirm-Buttons fest
     */
    public static void popUpDialog(final AppCompatActivity beforeActivity, final Class<?> afterActivity,int title, int message, int cancel, int confirm){

        new AlertDialog.Builder(beforeActivity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(cancel,null)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        S.startActivitiy(beforeActivity, afterActivity);
                    }
                }).create().show();
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
