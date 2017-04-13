package de.projektss17.bonpix;

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


    public C_DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS bons (bons_id INTEGER PRIMARY KEY AUTOINCREMENT, bons_name VARCHAR(255))";
            db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<String> getAllBons(SQLiteDatabase db){
        ArrayList<String> bonsList = new ArrayList();
        String query = "SELECT * FROM bons";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                String pass = cursor.getString(1);
                bonsList.add(pass);
                Log.i("#DBHANDLER CURSOR:"," ### " + pass);
            } while (cursor.moveToNext());
        }
        return bonsList;
    }
}