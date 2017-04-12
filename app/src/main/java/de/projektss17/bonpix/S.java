package de.projektss17.bonpix;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
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
        S.startActivitiy(beforeActivity,A_OCR_Manuell.class);
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
     * Ruft die Budget Activity auf
     */
    public static void showEinstellungen(AppCompatActivity beforeActivity){

        S.startActivitiy(beforeActivity, A_Einstellungen.class);
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


    // Erstellt von Johanns am 09.04.17
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



}
