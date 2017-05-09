package de.projektss17.bonpix.daten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String CREATE_TABLE_Laeden = "CREATE TABLE IF NOT EXISTS laeden (laeden_id INTEGER PRIMARY KEY AUTOINCREMENT, laeden_name VARCHAR(255))";
        db.execSQL(CREATE_TABLE_Laeden);
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS bons (bons_id INTEGER PRIMARY KEY AUTOINCREMENT, bons_name VARCHAR(255))";
        db.execSQL(CREATE_TABLE);
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

    /**
     * Get the Data for BarCharts
     * @param time
     * @return
     */
    public List<BarEntry> getBarData(int time){
        //TODO: Logic part for preparing Bar Data
        List<BarEntry> dataList = new ArrayList<>();
        switch(time){
            case 1:
                dataList.add(new BarEntry(0f, 30f));
                dataList.add(new BarEntry(1f, 80f));
                dataList.add(new BarEntry(2f, 60f));
                dataList.add(new BarEntry(3f, 50f));
                dataList.add(new BarEntry(5f, 70f));
                dataList.add(new BarEntry(6f, 60f));
                return dataList;
            default:
                dataList.add(new BarEntry(0f, 30f));
                dataList.add(new BarEntry(1f, 80f));
                dataList.add(new BarEntry(2f, 60f));
                dataList.add(new BarEntry(3f, 50f));
                dataList.add(new BarEntry(5f, 70f));
                dataList.add(new BarEntry(6f, 60f));
                return dataList;
        }
    }

    /**
     * Get the Data for LineCharts
     * @param  time
     * @return
     */
    public Map<String, List<Entry>> getLineData(int time){
        //TODO: Logic part for preparing Bar Data
        Map<String, List<Entry>> map = new HashMap();
        List<Entry> lineOne = new ArrayList<Entry>();
        List<Entry> lineTwo = new ArrayList<Entry>();
        switch(time){
            case 1:
                Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
                lineOne.add(c1e1);
                Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
                lineOne.add(c1e2);
                map.put("lineOne", lineOne);
                Entry c2e1 = new Entry(0f, 130000f); // 0 == quarter 1
                lineTwo.add(c2e1);
                Entry c2e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
                lineTwo.add(c2e2);
                map.put("lineTwo", lineTwo);
                return map;
            default:
                Entry c3e1 = new Entry(0f, 100000f); // 0 == quarter 1
                lineOne.add(c3e1);
                Entry c3e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
                lineOne.add(c3e2);
                map.put("lineOne", lineOne);
                Entry c4e1 = new Entry(0f, 130000f); // 0 == quarter 1
                lineTwo.add(c4e1);
                Entry c4e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
                lineTwo.add(c4e2);
                map.put("lineTwo", lineTwo);
                return map;
        }
    }

    /**
     * Get the Data for PieCharts
     * @param time
     * @return
     */
    public List<PieEntry> getPieData(int time){
        List<PieEntry> dataList = new ArrayList<>();
        //TODO: Logic part for preparing Bar Data
        switch(time){
            case 1:
                dataList.add(new PieEntry(18.5f, "Green"));
                dataList.add(new PieEntry(26.7f, "Yellow"));
                dataList.add(new PieEntry(24.0f, "Red"));
                dataList.add(new PieEntry(30.8f, "Blue"));
                return dataList;
            default:
                dataList.add(new PieEntry(18.5f, "Green"));
                dataList.add(new PieEntry(26.7f, "Yellow"));
                dataList.add(new PieEntry(24.0f, "Red"));
                dataList.add(new PieEntry(30.8f, "Blue"));
                return dataList;
        }
    }
}