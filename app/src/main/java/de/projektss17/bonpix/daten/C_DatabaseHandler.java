package de.projektss17.bonpix.daten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Marcus on 11.04.2017.
 */

public class C_DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bonpix";
    private static final int DATABASE_VERSION = 1;
    final String KEY_LAEDEN = "laeden_name";


    public C_DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        checkTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Get all Bons via ArrayList<String>
     * @param db
     * @return
     */
    public ArrayList<String> getAllBons(SQLiteDatabase db){
        ArrayList<String> bonsList = new ArrayList();
        String query = "SELECT * FROM bons";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                String pass = cursor.getString(1);
                bonsList.add(pass);
                //Log.e("#DBHANDLER Bons:"," ### " + pass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bonsList;
    }

    /**
     * Get all Laeden via ArrayList
     * @param db
     * @return
     */
    public ArrayList<String> getAllLaeden(SQLiteDatabase db){
        ArrayList<String> list = new ArrayList();
        String query = "SELECT * FROM laeden";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                String pass = cursor.getString(1);
                list.add(pass);
                //Log.e("#DBHANDLER Laeden:"," ### " + pass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * Set new Laden
     * @param db
     * @param pass
     */
    public void setLaeden(SQLiteDatabase db, String pass){
        ContentValues values = new ContentValues();
        values.put(KEY_LAEDEN, pass);
        db.insert("laeden", null, values);
    }

    /**
     * Create Tables if not exists
     * @param db
     */
    public void checkTables(SQLiteDatabase db){

        String CREATE_TABLE_Laden = "CREATE TABLE IF NOT EXISTS laden (ladenid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255))";

        String CREATE_TABLE_Bon = "CREATE TABLE IF NOT EXISTS bon (bonid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "bildpfad VARCHAR(255), " +
                "ladenname INTEGER, " +
                "anschrift VARCHAR(255), " +
                "sonstigeinfos VARCHAR(255), " +
                "datum VARCHAR(255), " +
                "garantieende VARCHAR(255), " +
                "favoriten BOOLEAN, " +
                "garantie BOOLEAN, " +
                "FOREIGN KEY (ladenname) REFERENCES laden(ladenid))";

        String CREATE_TABLE_Artikel = "CREATE TABLE IF NOT EXISTS artikel (artikelid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255), " +
                "preis VARCHAR(255))";

        String CREATE_TABLE_BonArtikel = "CREATE TABLE IF NOT EXISTS bonartikel (bonid INTEGER NOT NULL, " +
                "artikelid INTEGER NOT NULL, " +
                "PRIMARY KEY (bonid, artikelid), " +
                "FOREIGN KEY (bonid) REFERENCES bon(bonid), " +
                "FOREIGN KEY (artikelid) REFERENCES artikel(artikelid))";

        db.execSQL(CREATE_TABLE_Laden);
        db.execSQL(CREATE_TABLE_Bon);
        db.execSQL(CREATE_TABLE_Artikel);
        db.execSQL(CREATE_TABLE_BonArtikel);

    }

    /**
     * Delete specific Laden
     * @param db
     * @param id
     */
    public void deleteLaden(SQLiteDatabase db, int id){
        String filter = "laeden_id=" + id;
        db.delete("laeden", filter, null);
    }
}