package de.projektss17.bonpix;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.projektss17.bonpix.daten.C_AssetHelper;
import de.projektss17.bonpix.daten.C_Bon;
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
     * Ruft die Manuell Activity auf (überschrieben)
     */
    public static void showManuell(AppCompatActivity beforeActivity, int bonId, String state){

        Intent intent = new Intent(beforeActivity, A_OCR_Manuell.class);
        intent.putExtra("bonId",bonId);
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
     * Ruft die Laeden Activity auf
     */
    public static void showLaeden(AppCompatActivity beforeActivity){
        S.startActivitiy(beforeActivity, A_Laeden.class);
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


    //Erstellt von Johanns am 03.04.2017

    /**
     * Öffnet ein POPUP-Fenster (Hinweis/Alert) beim Aufruf
     * @param beforeActivity Vorherige Instanz der Activity
     * @param afterActivity Activity Klasse die gestartet werden soll
     * @param title Legt den Titel des PopUp-Fensters fest (wird im Fenster angezeigt)
     * @param message Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param cancel Legt den Ihnalt des Cancel-Buttons fest
     * @param confirm Legt den Inhalt des Confirm-Buttons fest
     */
    public static void popUpDialog(final AppCompatActivity beforeActivity, final Class<?> afterActivity, int title, int message, int cancel, int confirm) {

        new AlertDialog.Builder(beforeActivity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(cancel, null)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        S.startActivitiy(beforeActivity, afterActivity);
                    }
                }).create().show();
    }

    /**
     * Öffnet ein POPUP-Fenster (Hinweis/Alert) beim Aufruf
     * @param beforeActivity Vorherige Instanz der Activity
     * @param title Legt den Titel des PopUp-Fensters fest (wird im Fenster angezeigt)
     * @param message Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param cancel Legt den Ihnalt des Cancel-Buttons fest
     * @param confirm Legt den Inhalt des Confirm-Buttons fest
     */
    public static void popUpDialogSaveBon(final AppCompatActivity beforeActivity, final Intent intent, int title, int message, int cancel, int confirm, final C_Bon bon) {

        new AlertDialog.Builder(beforeActivity)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(cancel, null)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        S.dbHandler.addBon(S.db, bon);
                        beforeActivity.startActivity(intent);
                        beforeActivity.finish();

                    }
                }).create().show();
    }



    // Erstellt von Johanns am 27.04.2017

    /**
     * Öffnet ein POPUP-Fenster (INFO) welches nach dem Aufruf automatisch geschlossen wird
     * @param title Legt den Titel des PopUp-Fensters fest (wird im Fenster als Überschrift angezeigt)
     * @param message Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param time Legt die Zeit fest, nach welchem das PopUp automatisch geschlossen wird
     */

    public static void popUpInfo(AppCompatActivity beforeActivity, int title, int message, int time){

        AlertDialog.Builder builder = new AlertDialog.Builder(beforeActivity)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true);

        final AlertDialog dialog = builder.create();

        dialog.show();

        final Timer zeitpunkt = new Timer();
        zeitpunkt.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                zeitpunkt.cancel();
            }
        }, time); // nach der TIME wird das PopUp automatisch geschlossen bzw. beendet

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

    /**
     * Gibt anhand des namens das richtige Icon zurück
     * @param res Resource
     * @param name Name des Ladens
     * @return Bitmap des Ladens
     */
    public static Bitmap getShopIcon(Resources res, String name){

        switch(name){
            case "ALDI NORD": return BitmapFactory.decodeResource(res,  R.mipmap.ic_aldinordlogo);
            case "ALDI SÜD": return BitmapFactory.decodeResource(res,  R.mipmap.ic_aldisuedlogo);
            case "Aral": return BitmapFactory.decodeResource(res,  R.mipmap.ic_arallogo);
            case "Edeka": return BitmapFactory.decodeResource(res,  R.mipmap.ic_edekalogo);
            case "Media Markt": return BitmapFactory.decodeResource(res,  R.mipmap.ic_mediamarktlogo);
            case "Müller": return BitmapFactory.decodeResource(res,  R.mipmap.ic_muellerlogo);
            case "Netto": return BitmapFactory.decodeResource(res,  R.mipmap.ic_nettologo);
            case "REWE": return BitmapFactory.decodeResource(res,  R.mipmap.ic_rewelogo);
            case "Saturn": return BitmapFactory.decodeResource(res,  R.mipmap.ic_saturnlogo);
            case "Shell": return BitmapFactory.decodeResource(res,  R.mipmap.ic_shelllogo);
            default: return BitmapFactory.decodeResource(res,  R.mipmap.ic_shopping_cart_black_24dp);
        }

    }

    /**
     * Gibt den Tag anhand des int wertes zurück
     * @param res Resource
     * @param day int tag
     * @return Return String des Wochentags
     */
    public static String getWeekday(Resources res, int day){
        switch(day){
            case 1: return res.getString(R.string.wochentag_1);
            case 2: return res.getString(R.string.wochentag_2);
            case 3: return res.getString(R.string.wochentag_3);
            case 4: return res.getString(R.string.wochentag_4);
            case 5: return res.getString(R.string.wochentag_5);
            case 6: return res.getString(R.string.wochentag_6);
            case 7: return res.getString(R.string.wochentag_7);
            default: return " ";
        }
    }

    /**
     * Gibt anhand eines Datum strings den Tag als int wert zurück
     * @param dateString Datum als String
     * @return Wochentag als int
     */
    public static int getWeekdayNumber(String dateString){

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date != null){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_WEEK);
        } else {
            return 0;
        }
    }

}
