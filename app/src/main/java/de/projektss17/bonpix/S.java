package de.projektss17.bonpix;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
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
    public static void showManuell(AppCompatActivity beforeActivity, ArrayList<String> path, String state) {

        Intent intent = new Intent(beforeActivity, A_OCR_Manuell.class);
        intent.putExtra("ArrayList", path);
        intent.putExtra("manuellState", state);
        beforeActivity.startActivity(intent);
    }

    /**
     * Ruft die Manuell Activity auf (überschrieben)
     */
    public static void showManuell(AppCompatActivity beforeActivity, String state) {

        Intent intent = new Intent(beforeActivity, A_OCR_Manuell.class);
        intent.putExtra("manuellState", state);
        beforeActivity.startActivity(intent);
    }

    /**
     * Ruft die Manuell Activity auf (überschrieben)
     */
    public static void showManuell(AppCompatActivity beforeActivity, int bonId, String state) {

        Intent intent = new Intent(beforeActivity, A_OCR_Manuell.class);
        intent.putExtra("bonId", bonId);
        intent.putExtra("manuellState", state);
        beforeActivity.startActivity(intent);
    }

    /**
     * Ruft die Tutorial Activity auf
     */
    public static void showTutorial(Context beforeActivity){
        Intent intent = new Intent(beforeActivity, A_Tutorial.class);
        beforeActivity.startActivity(intent);
    }

    /**
     * Ruft die Budget Activity auf
     */
    public static void showBudget(AppCompatActivity beforeActivity) {
        S.startActivitiy(beforeActivity, A_Budget.class);
    }

    /**
     * Ruft die Gruppen Activity auf
     */
    public static void showGruppen(AppCompatActivity beforeActivity) {
        S.startActivitiy(beforeActivity, A_Gruppen.class);
    }

    /**
     * Ruft die Favoriten Activity auf
     */
    public static void showFavoriten(AppCompatActivity beforeActivity) {
        S.startActivitiy(beforeActivity, A_Favoriten.class);
    }

    /**
     * Ruft die Garantie Activity auf
     */
    public static void showGarantie(AppCompatActivity beforeActivity) {
        S.startActivitiy(beforeActivity, A_Garantie.class);
    }

    /**
     * Ruft die Laeden Activity auf
     */
    public static void showLaeden(AppCompatActivity beforeActivity) {
        S.startActivitiy(beforeActivity, A_Laeden.class);
    }

    /**
     * Ruft die Einstellungen Activity auf
     */
    public static void showEinstellungen(AppCompatActivity beforeActivity) {
        S.startActivitiy(beforeActivity, A_Einstellungen.class);
    }

    /**
     * Ruft die Backup Activity auf
     */
    public static void showBackup(PreferenceFragment context) {
        Intent intent = new Intent(context.getActivity(), A_Backup.class);
        context.startActivity(intent);
    }

    /**
     * Ruft die Impressum Activity auf
     */
    public static void showImpressum(AppCompatActivity context){
        Intent intent = new Intent(context, A_Impressum.class);
        context.startActivity(intent);
    }

    /**
     * Ruft die Impressum Activity auf (durch shaken des Smartphones in der Einstellungs Activity)
     */
    public static void showImpressum(PreferenceActivity context){
        Intent intent = new Intent(context, A_Impressum.class);
        context.startActivity(intent);
    }

    /**
     * Ruft die Max Bon Activity auf
     */
    public static void showMaxBonPic(AppCompatActivity beforeActivity, String uriPath) {
        Intent intent = new Intent(beforeActivity, A_Max_Bon_Pic.class);
        intent.putExtra("imageUri", uriPath);
        beforeActivity.startActivity(intent);
    }

    /**
     * Startet Activitys
     *
     * @param beforeActivity Vorherige Instanz der Activity
     * @param cls            Activity Klasse die gestartet werden soll
     */
    public static void startActivitiy(AppCompatActivity beforeActivity, Class<?> cls) {
        Intent intent = new Intent(beforeActivity, cls);
        beforeActivity.startActivity(intent);
    }


    //Erstellt von Johanns am 03.04.2017

    /**
     * Öffnet ein POPUP-Fenster (Hinweis/Alert) beim Aufruf
     *
     * @param beforeActivity Vorherige Instanz der Activity
     * @param afterActivity  Activity Klasse die gestartet werden soll
     * @param title          Legt den Titel des PopUp-Fensters fest (wird im Fenster angezeigt)
     * @param message        Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param cancel         Legt den Ihnalt des Cancel-Buttons fest
     * @param confirm        Legt den Inhalt des Confirm-Buttons fest
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
     *
     * @param beforeActivity Vorherige Instanz der Activity
     * @param title          Legt den Titel des PopUp-Fensters fest (wird im Fenster angezeigt)
     * @param message        Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param cancel         Legt den Ihnalt des Cancel-Buttons fest
     * @param confirm        Legt den Inhalt des Confirm-Buttons fest
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
                        beforeActivity.finish();

                    }
                }).create().show();
    }


    // Erstellt von Johanns am 27.04.2017

    /**
     * Öffnet ein POPUP-Fenster (INFO) welches nach dem Aufruf automatisch geschlossen wird
     *
     * @param title   Legt den Titel des PopUp-Fensters fest (wird im Fenster als Überschrift angezeigt)
     * @param message Legt die Nachricht (z.B. eine Frage) des Fenster fest
     * @param time    Legt die Zeit fest, nach welchem das PopUp automatisch geschlossen wird
     */

    public static void popUpInfo(AppCompatActivity beforeActivity, int title, int message, int time) {

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
     *
     * @param context Class.this (instanz übergeben)
     * @param msg     Text der Ausgegeben werden soll
     */
    public static void outShort(AppCompatActivity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Ausgabe eines Strings (lang)
     * Ausgabe erfolgt über einen Toast
     *
     * @param context Class.this (instanz übergeben)
     * @param msg     Text der Ausgegeben werden soll
     */
    public static void outLong(AppCompatActivity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Gibt anhand des namens das richtige Icon zurück
     *
     * @param res  Resource
     * @param name Name des Ladens
     * @return Bitmap des Ladens
     */
    public static Bitmap getShopIcon(Resources res, String name) {

        switch (name) {
            case "Rossmann":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_rossmannlogo);
            case "DM":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_dmlogo);
            case "Real":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_reallogo);
            case "Penny":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_pennylogo);
            case "Norma":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_normalogo);
            case "Marktkauf":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_marktkauflogo);
            case "Expert Markt":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_expertlogo);
            case "OMV":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_omvlogo);
            case "Jet":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_jetlogo);
            case "Agip":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_agiplogo);
            case "Esso":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_essologo);
            case "ALDI NORD":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_aldinordlogo);
            case "ALDI SÜD":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_aldisuedlogo);
            case "Aral":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_arallogo);
            case "Edeka":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_edekalogo);
            case "Media Markt":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_mediamarktlogo);
            case "Müller":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_muellerlogo);
            case "Netto":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_nettologo);
            case "REWE":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_rewelogo);
            case "Saturn":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_saturnlogo);
            case "Shell":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_shelllogo);
            case "Lidl":
                return BitmapFactory.decodeResource(res, R.mipmap.ic_lidllogo);
            default:
                return BitmapFactory.decodeResource(res, R.mipmap.bonpix);
        }

    }

    /**
     * Gibt den Tag anhand des int wertes zurück
     *
     * @param res Resource
     * @param day int tag
     * @return Return String des Wochentags
     */
    public static String getWeekday(Resources res, int day) {
        switch (day) {
            case 1:
                return res.getString(R.string.wochentag_1);
            case 2:
                return res.getString(R.string.wochentag_2);
            case 3:
                return res.getString(R.string.wochentag_3);
            case 4:
                return res.getString(R.string.wochentag_4);
            case 5:
                return res.getString(R.string.wochentag_5);
            case 6:
                return res.getString(R.string.wochentag_6);
            case 7:
                return res.getString(R.string.wochentag_7);
            default:
                return " ";
        }
    }

    /**
     * Gibt anhand eines Datum strings den Tag als int wert zurück
     *
     * @param dateString Datum als String
     * @return Wochentag als int
     */
    public static int getWeekdayNumber(String dateString) {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;

        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c.get(Calendar.DAY_OF_WEEK);
        } else {
            return 0;
        }
    }


    /**
     * Gibt das von und bis Datum einer Woche wieder
     *
     * @return Array mit 0 - Von Datum und 1 - Bis Datum
     */
    public static String[] getWeek() {

        String date[] = new String[2];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        date[0] = new SimpleDateFormat("dd").format(cal.getTime()) + "."
                + new SimpleDateFormat("MM").format(cal.getTime()) + "."
                + new SimpleDateFormat("yyyy").format(cal.getTime());

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        date[1] = new SimpleDateFormat("dd").format(cal.getTime()) + "."
                + new SimpleDateFormat("MM").format(cal.getTime()) + "."
                + new SimpleDateFormat("yyyy").format(cal.getTime());

        return date;
    }

    /**
     * Gibt ein Array mit jedem Datum der letzten 7 Tage zurück
     *
     * @return Array mit den letzten 7 Tagen
     */
    public static String[] getFullWeek() {
        String date[] = new String[7];
        Calendar cal = Calendar.getInstance();

        date[0] = getNumberWithZero(cal.get(Calendar.DAY_OF_MONTH)) + "."
                + getNumberWithZero(cal.get(Calendar.MONTH) + 1) + "."
                + cal.get(Calendar.YEAR);

        for (int i = 1; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -1);

            date[i] = new SimpleDateFormat("dd").format(cal.getTime()) + "."
                    + new SimpleDateFormat("MM").format(cal.getTime()) + "."
                    + new SimpleDateFormat("yyyy").format(cal.getTime());
        }

        return date;
    }

    /**
     * Gibt das von und bis Datum eines Monats wieder
     *
     * @return Array mit 0 - Von Datum und 1 - Bis Datum
     */
    public static String[] getMonth() {

        String date[] = new String[2];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        date[0] = new SimpleDateFormat("dd").format(cal.getTime()) + "."
                + new SimpleDateFormat("MM").format(cal.getTime()) + "."
                + new SimpleDateFormat("yyyy").format(cal.getTime());

        String lastDay = getNumberWithZero(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));

        date[1] = lastDay + "."
                + new SimpleDateFormat("MM").format(cal.getTime()) + "."
                + new SimpleDateFormat("yyyy").format(cal.getTime());

        return date;
    }

    /**
     * Gibt das von und bis Datum eines Quartals wieder
     *
     * @return Array mit 0 - Von Datum und 1 - Bis Datum
     */
    public static String[] getQuartal() {

        String date[] = new String[2];
        Calendar cal = Calendar.getInstance();
        int month = Calendar.getInstance().get(Calendar.MONTH);

        switch ((month >= Calendar.JANUARY && month <= Calendar.MARCH) ? "Q1" :
                (month >= Calendar.APRIL && month <= Calendar.JUNE) ? "Q2" :
                        (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) ? "Q3" :
                                "Q4") {
            case "Q1":
                date[0] = "01.01." + new SimpleDateFormat("yyyy").format(cal.getTime());
                date[1] = "31.03." + new SimpleDateFormat("yyyy").format(cal.getTime());
                break;
            case "Q2":
                date[0] = "01.04." + new SimpleDateFormat("yyyy").format(cal.getTime());
                date[1] = "30.06." + new SimpleDateFormat("yyyy").format(cal.getTime());
                break;
            case "Q3":
                date[0] = "01.07." + new SimpleDateFormat("yyyy").format(cal.getTime());
                date[1] = "30.09." + new SimpleDateFormat("yyyy").format(cal.getTime());
                break;
            case "Q4":
                date[0] = "01.10." + new SimpleDateFormat("yyyy").format(cal.getTime());
                date[1] = "31.12." + new SimpleDateFormat("yyyy").format(cal.getTime());
                break;
        }

        return date;
    }

    /**
     * Gibt eine Int Zahl mit einer 0 davor zurück (sofern kleiner als 10)
     *
     * @param zahl Zahl
     * @return String Zahl mit 0
     */
    public static String getNumberWithZero(int zahl) {
        if (zahl > 0 && zahl < 10) {
            return "0" + zahl;
        } else {
            return "" + zahl;
        }
    }


    /**
     * Notification Builder - Opens a clickable notification!
     * (Could be opened with S.sendNotification(context, activity.class, string, string, boolean))
     *
     * @param context Context
     * @param cls     Klasse zu der beim Klick gewechselt werden soll
     * @param title   Titel der Notification
     * @param txt     Text der Notification
     */
    public static void sendNotification(Context context, Class<?> cls, String title, String txt) {
        sendNotification(context, cls, title, txt, true);
    }

    /**
     * Notification Builder - Opens a clickable notification!
     * (Could be opened with S.sendNotification(context, activity.class, string, string, boolean))
     *
     * @param context         Context
     * @param cls             Klasse zu der beim Klick gewechselt werden soll
     * @param title           Titel der Notification
     * @param txt             Text der Notification
     * @param closeAfterClick Soll die Notification geschlossen werden nach dem klick
     */
    public static void sendNotification(Context context, Class<?> cls, String title, String txt, boolean closeAfterClick) {
        if (S.prefs.getPrefBoolean("pref_notifications")) {
            NotificationCompat.Builder builder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.bonpix)
                            .setContentTitle(title)
                            .setContentText(txt)
                            .setAutoCancel(closeAfterClick);

            Intent notificationIntent = new Intent(context, cls);

            //Defines the action inside the notification
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }

    /**
     * Setzt den Navigation Drawer Counter
     * @param itemId ID des Elements
     * @param count Anzahl der Elemente
     * @param navigationView Navigation View
     */
    public static void setMenuCounter(int itemId, int count, NavigationView navigationView) {

        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        if(count > 99){
            view.setText(count > 99 ? "99+" : String.valueOf(count));
        } else {
            view.setText(count > 0 ? String.valueOf(count) : null);
        }

    }

    /**
     * Gibt einen Preis gerundet wieder
     * @param price
     * @return
     */
    public static String roundPrice(Double price){
        price = Math.round(price * 100) / 100.00;
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(price);
    }
}
