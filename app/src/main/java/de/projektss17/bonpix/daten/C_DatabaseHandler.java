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

    // Example Function to pass all Bons
    public ArrayList<String> getAllBons(SQLiteDatabase db){
        ArrayList<String> bonsList = new ArrayList();
        String query = "SELECT * FROM bons";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                String pass = cursor.getString(1);
                bonsList.add(pass);
                Log.e("#DBHANDLER Bons:"," ### " + pass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bonsList;
    }

    // Example Function to pass all Laeden
    public ArrayList<String> getAllLaeden(SQLiteDatabase db){
        ArrayList<String> list = new ArrayList();
        String query = "SELECT * FROM laeden";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                String pass = cursor.getString(1);
                list.add(pass);
                Log.e("#DBHANDLER Laeden:"," ### " + pass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Example Function to set new Laden
    public void setLaeden(SQLiteDatabase db, String pass){
        ContentValues values = new ContentValues();
        values.put(KEY_LAEDEN, pass);
        db.insert("laeden", null, values);
    }

    // Create Tables
    public void checkTables(SQLiteDatabase db){
        String CREATE_TABLE_Laeden = "CREATE TABLE IF NOT EXISTS laeden (laeden_id INTEGER PRIMARY KEY AUTOINCREMENT, laeden_name VARCHAR(255))";
        db.execSQL(CREATE_TABLE_Laeden);
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS bons (bons_id INTEGER PRIMARY KEY AUTOINCREMENT, bons_name VARCHAR(255))";
        db.execSQL(CREATE_TABLE);
    }

    // Delete specific Laden
    public void deleteLaden(SQLiteDatabase db, int id){
        String filter = "laeden_id=" + id;
        db.delete("laeden", filter, null);
    }
}