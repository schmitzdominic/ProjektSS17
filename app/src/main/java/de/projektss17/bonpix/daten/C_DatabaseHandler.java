package de.projektss17.bonpix.daten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

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
    public ArrayList<C_Bon> getAllBons(SQLiteDatabase db){

        ArrayList<C_Bon> bonsList = new ArrayList<>();
        ArrayList<C_Artikel> artikelList = new ArrayList<>();
        C_Bon bon;
        String ladenName = " ";

        String query = "SELECT * FROM bon";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Log.e("### ARSCHLOCH", cursor.getString(2)+"");
                for(C_Laden laden : this.getAllLaeden(db)){ //laden.getId() == cursor.getInt(2)
                    if(true){
                        ladenName = laden.getName();
                    }
                }

                bon = new C_Bon(cursor.getInt(0),
                        cursor.getString(1),
                        ladenName,
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7) > 0,
                        cursor.getInt(8) > 0);
                Log.e("#### BON AUSLESEN",bon.getLadenname());
                bon.setArtikel(this.getAllArtikelFromBon(db, bon));
                bonsList.add(bon);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return bonsList;
    }

    /**
     * Gibt alle
     * @param db
     * @param bon
     * @return
     */
    public ArrayList<C_Artikel> getAllArtikelFromBon(SQLiteDatabase db, C_Bon bon){

        ArrayList<C_Artikel> artikelList = new ArrayList<>();

        String query = "SELECT a.artikelid, a.name, a.preis FROM (bon b INNER JOIN bonartikel ba ON (b.bonid = ba.bonid)) " +
                "INNER JOIN artikel a ON (ba.artikelid = a.artikelid)";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                artikelList.add(new C_Artikel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return artikelList;
    }

    /**
     * Get all Laeden via ArrayList
     * @param db
     * @return
     */
    public ArrayList<C_Laden> getAllLaeden(SQLiteDatabase db){

        ArrayList<C_Laden> list = new ArrayList<>();
        String query = "SELECT * FROM laden";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                list.add(new C_Laden(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * Get all Artikel via ArrayList
     * @param db
     * @return
     */
    public ArrayList<C_Artikel> getAllArtikel(SQLiteDatabase db){

        ArrayList<C_Artikel> list = new ArrayList<>();
        String query = "SELECT * FROM artikel";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                list.add(new C_Artikel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * Gibt alle Bon Artikel zurück
     * @param db
     * @return
     */
    public HashMap<Integer, Integer> getAllBonArtkel(SQLiteDatabase db){

        HashMap<Integer, Integer> list = new HashMap<>();
        String query = "SELECT * FROM bonartikel";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                list.put(cursor.getInt(0), cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * Set new Bon
     * @param db Datenbank
     * @param bon Bon
     */
    public void setBon(SQLiteDatabase db, C_Bon bon){

        ContentValues values = new ContentValues();
        int id = 0;

        for(C_Laden laden : this.getAllLaeden(db)){
            if(laden.getName().equals(bon.getLadenname())){
                id = laden.getId();
                break;
            }
        }

        if(id == 0){
            this.setLaden(db, new C_Laden(bon.getLadenname()));
        }

        values.put("bildpfad", bon.getBildpfad());
        values.put("ladenname", id);
        values.put("anschrift", bon.getAnschrift());
        values.put("sonstigeinfos", bon.getSonstigeInfos());
        values.put("datum", bon.getDatum());
        values.put("garantieende", bon.getGarantieEnde());
        values.put("favoriten", bon.getFavorite());
        values.put("garantie", bon.getGarantie());

        db.insert("bon", null, values);

        for(C_Artikel artikel : bon.getArtikel()){
            this.setArtikel(db, artikel);
            this.setBonArtikel(db, bon.getId(), artikel.getId());
        }

    }

    /**
     * Set new Laden
     * @param db Datenbank
     * @param laden Laden
     */
    public void setLaden(SQLiteDatabase db, C_Laden laden){
        ContentValues values = new ContentValues();
        values.put("name", laden.getName());
        db.insert("laden", null, values);
    }

    /**
     * Set new Laden
     * @param db Datenbank
     * @param artikel Artikel
     */
    public void setArtikel(SQLiteDatabase db, C_Artikel artikel){

        boolean dontmatch = true;

        for(C_Artikel a : this.getAllArtikel(db)){
            if(a.getName().equals(artikel.getName())){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("name", artikel.getName());
            values.put("preis", artikel.getPreis());
            db.insert("artikel", null, values);
        }
    }

    /**
     *
     * @param db
     * @param bonid
     * @param artikelid
     */
    public void setBonArtikel(SQLiteDatabase db, int bonid, int artikelid){

        boolean dontmatch = true;

        for(int a : this.getAllBonArtkel(db).keySet()){
            if(a == bonid && this.getAllBonArtkel(db).get(a) == artikelid){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("bonid", bonid);
            values.put("artikelid", artikelid);
            db.insert("bonartikel", null, values);
        }
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