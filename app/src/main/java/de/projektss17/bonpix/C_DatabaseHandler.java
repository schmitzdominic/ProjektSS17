package de.projektss17.bonpix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcus on 11.04.2017.
 */

public class C_DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test";
    private static final int DATABASE_VERSION = 1;


    public C_DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}
