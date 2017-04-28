package de.projektss17.bonpix.daten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import de.projektss17.bonpix.S;

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
        String query = "SELECT * FROM bon";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {

                C_Bon bon = new C_Bon(cursor.getInt(0),
                        cursor.getString(1),
                        this.getLaden(db, cursor.getInt(2)).getName(),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7) > 0,
                        cursor.getInt(8) > 0);

                bon.setArtikel(this.getAllArtikelFromBon(db, bon));
                bonsList.add(bon);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return bonsList;
    }

    /**
     * Gibt alle Artikel eines Bons aus
     * @param db Datenbank
     * @param bon Bon
     * @return ArrayList mit allen dazugehörigen Artikeln
     */
    public ArrayList<C_Artikel> getAllArtikelFromBon(SQLiteDatabase db, C_Bon bon){

        ArrayList<C_Artikel> artikelList = new ArrayList<>();

        String query = "SELECT a.artikelid, a.name, a.preis FROM artikel a " +
                "LEFT JOIN bonartikel ba ON ba.artikelid = a.artikelid " +
                "WHERE ba.bonid = '" + bon.getId() + "'";

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
     * Gibt alle Laeden zurück
     * @param db Datenbank
     * @return Arraylist Mit C_Laden objekten
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
     * Gibt alle Artikel zurück
     * @param db Datenbank
     * @return ArrayList Mit C_Artikel objekten
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
     * @param db Datenbank
     * @return HashMap mit allen Zuweißungen Integer, Integer
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
     * Fügt einen neuen Bon hinzu
     * @param db Datenbank
     * @param bon Bon
     */
    public void setBon(SQLiteDatabase db, C_Bon bon){

        ContentValues values = new ContentValues();
        int ladenId = 0;

        if(this.checkIfLadenExist(db, bon.getLadenname())){
            ladenId = this.getLaden(db, bon.getLadenname()).getId();
        } else {
            this.addLaden(db, new C_Laden(bon.getLadenname()));
            ladenId = this.getLaden(db, bon.getLadenname()).getId();
        }

        values.put("bildpfad", bon.getBildpfad());
        values.put("ladenname", ladenId);
        values.put("anschrift", bon.getAnschrift());
        values.put("sonstigeinfos", bon.getSonstigeInfos());
        values.put("datum", bon.getDatum());
        values.put("garantieende", bon.getGarantieEnde());
        values.put("favoriten", bon.getFavorite());
        values.put("garantie", bon.getGarantie());

        db.insert("bon", null, values);

        int bonid = this.getAllBons(S.db).get(this.getAllBons(S.db).size()-1).getId();

        if(this.checkIfBonExist(db, bonid)){
            for(C_Artikel a : bon.getArtikel()) {
                this.addArtikel(db, a);
            }
        }

        if(this.checkIfBonExist(db, bonid)){
            for(C_Artikel bonArtikel : bon.getArtikel()){
                for(C_Artikel dbArtikel : this.getAllArtikel(db)){
                    if(bonArtikel.getName().equals(dbArtikel.getName()) && bonArtikel.getPreis().equals(dbArtikel.getPreis())){
                        this.addBonArtikel(db, bonid, dbArtikel.getId());
                    }
                }
            }
        }
    }

    /**
     * Fügt einen neuen Laden hinzu
     * @param db Datenbank
     * @param laden Laden
     */
    public void addLaden(SQLiteDatabase db, C_Laden laden){
        ContentValues values = new ContentValues();
        values.put("name", laden.getName());
        db.insert("laden", null, values);
    }

    /**
     * Fügt einen neuen Artikel hinzu
     * @param db Datenbank
     * @param artikel Artikel
     */
    public void addArtikel(SQLiteDatabase db, C_Artikel artikel){

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
     * Fügt eine neue zuweißung Bon zu Artikel hinzu
     * @param db Datenbank
     * @param bonId BonId
     * @param artikelId ArtikelId
     */
    public void addBonArtikel(SQLiteDatabase db, int bonId, int artikelId){

        boolean dontmatch = true;

        for(int a : this.getAllBonArtkel(db).keySet()){
            if(a == bonId && this.getAllBonArtkel(db).get(a) == artikelId){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("bonid", bonId);
            values.put("artikelid", artikelId);
            db.insert("bonartikel", null, values);
        }
    }

    /**
     * Überprüft ob ein Laden bereits existiert.
     * @param db Datenbank
     * @param ladenName Name des gesuchten Ladens
     * @return true - existiert, false - existiert noch nicht
     */
    public boolean checkIfLadenExist(SQLiteDatabase db, String ladenName){
        for(C_Laden laden : this.getAllLaeden(db)){
            if(laden.getName().equals(ladenName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob ein Laden bereits existiert.
     * @param db Datenbank
     * @param id des ladens
     * @return true - existiert, false - existiert noch nicht
     */
    public boolean checkIfLadenExist(SQLiteDatabase db, int id){
        for(C_Laden laden : this.getAllLaeden(db)){
            if(laden.getId() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob ein Bon bereits existiert.
     * @param db Datenbank
     * @param id des Bons
     * @return true - existiert, false - existiert noch nicht
     */
    public boolean checkIfBonExist(SQLiteDatabase db, int id){
        for(C_Bon bon : this.getAllBons(db)){
            if(bon.getId() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt einen Laden zurück falls dieser existiert
     * @param db Datenbank
     * @param ladenName Name des gesuchtne Ladens
     * @return C_Laden wenn er exisitert, null wenn nicht
     */
    public C_Laden getLaden(SQLiteDatabase db, String ladenName){

        if(checkIfLadenExist(db, ladenName)){
            for(C_Laden laden : this.getAllLaeden(db)){
                if(laden.getName().equals(ladenName)){
                    return laden;
                }
            }
        }
        return null;
    }

    /**
     * Gibt einen Laden zurück falls dieser existiert
     * @param db Datenbank
     * @param id ID des Ladens
     * @return C_Laden wenn er exisitert, null wenn nicht
     */
    public C_Laden getLaden(SQLiteDatabase db, int id){

        if(checkIfLadenExist(db, id)){
            for(C_Laden laden : this.getAllLaeden(db)){
                if(laden.getId() == id){
                    return laden;
                }
            }
        }
        return null;
    }

    /**
     * Gibt einen Bon zurück falls dieser existiert
     * @param db Datenbank
     * @param id ID des Bons
     * @return C_Bon wenn er existiert, null wenn nicht
     */
    public C_Bon getBon(SQLiteDatabase db, int id){

        if(checkIfBonExist(db, id)){
            for(C_Bon bon : this.getAllBons(db)){
                if(bon.getId() == id){
                    return bon;
                }
            }
        }
        return null;
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