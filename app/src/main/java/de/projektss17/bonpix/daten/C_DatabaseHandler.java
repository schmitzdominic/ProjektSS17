package de.projektss17.bonpix.daten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

                bon.setArticles(this.getAllArticleFromBon(db, bon));
                bonsList.add(bon);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return bonsList;
    }

    /**
     * Gibt alle Artikel eines Bons zurück
     * @param db Datenbank
     * @param bon Bon
     * @return ArrayList mit allen dazugehörigen Artikeln
     */
    public ArrayList<C_Artikel> getAllArticleFromBon(SQLiteDatabase db, C_Bon bon){

        ArrayList<C_Artikel> articleList = new ArrayList<>();

        String query = "SELECT a.artikelid, a.name, a.preis, a.kategorie FROM artikel a " +
                "LEFT JOIN bonartikel ba ON ba.artikelid = a.artikelid " +
                "WHERE ba.bonid = '" + bon.getId() + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                articleList.add(new C_Artikel(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return articleList;
    }

    /**
     * Gibt alle Laeden zurück
     * @param db Datenbank
     * @return Arraylist Mit C_MatchLaden objekten
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
    public ArrayList<C_Artikel> getAllArticle(SQLiteDatabase db){

        ArrayList<C_Artikel> list = new ArrayList<>();
        String query = "SELECT * FROM artikel";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                list.add(new C_Artikel(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3)));
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
    public HashMap<Integer, Integer> getAllBonArticle(SQLiteDatabase db){

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
     * Gibt einen Laden zurück falls dieser existiert
     * @param db Datenbank
     * @param ladenName Name des gesuchtne Ladens
     * @return C_MatchLaden wenn er exisitert, null wenn nicht
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
     * @return C_MatchLaden wenn er exisitert, null wenn nicht
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
     * Gibt einen Artikel zurück falls dieser exisitert
     * @param db Datenbank
     * @param articleName Artikel Name
     * @param articlePrice Artikel Preis
     * @return C_Artikel wenn er existiert, null wenn nicht
     */
    public C_Artikel getArticle(SQLiteDatabase db, String articleName, float articlePrice){

        if(checkIfArticleExist(db, articleName, articlePrice)){
            for(C_Artikel article : this.getAllArticle(db)){
                if(article.getName().equals(articleName) && article.getPrice() == articlePrice){
                    return article;
                }
            }
        }
        return null;
    }

    /**
     * Gibt einen Artikel zurück falls dieser existiert
     * @param db Datenbank
     * @param id ID des Artikels
     * @return C_Artikel wenn er existiert, null wenn nicht
     */
    public C_Artikel getArticle(SQLiteDatabase db, int id){

        if(checkIfArticleExist(db, id)){
            for(C_Artikel article : this.getAllArticle(db)){
                if(article.getId() == id){
                    return article;
                }
            }
        }
        return null;
    }

    /**
     * Fügt einen neuen Bon hinzu
     * @param db Datenbank
     * @param bon Bon
     */
    public void addBon(SQLiteDatabase db, C_Bon bon){

        ContentValues values = new ContentValues();
        int ladenId;

        if(this.checkIfLadenExist(db, bon.getShopName())){
            ladenId = this.getLaden(db, bon.getShopName()).getId();
        } else {
            this.addLaden(db, new C_Laden(bon.getShopName()));
            ladenId = this.getLaden(db, bon.getShopName()).getId();
        }

        values.put("bildpfad", bon.getPath());
        values.put("ladenname", ladenId);
        values.put("anschrift", bon.getAdress());
        values.put("sonstigeinfos", bon.getOtherInformations());
        values.put("datum", bon.getDate());
        values.put("garantieende", bon.getGuaranteeEnd());
        values.put("favoriten", bon.getFavourite());
        values.put("garantie", bon.getGuarantee());

        db.insert("bon", null, values);

        int bonid = this.getAllBons(S.db).get(this.getAllBons(S.db).size()-1).getId();

        if(this.checkIfBonExist(db, bonid)){
            for(C_Artikel a : bon.getArticles()) {
                this.addArticle(db, a);
            }
        }

        if(this.checkIfBonExist(db, bonid)){
            for(C_Artikel bonArticle : bon.getArticles()){
                for(C_Artikel dbArticle : this.getAllArticle(db)){
                    if(bonArticle.getName().equals(dbArticle.getName()) && bonArticle.getPrice() == dbArticle.getPrice()){
                        this.addBonArticle(db, bonid, dbArticle.getId());
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
     * @param article Artikel
     */
    public void addArticle(SQLiteDatabase db, C_Artikel article){

        boolean dontmatch = true;

        for(C_Artikel a : this.getAllArticle(db)){
            if(a.getName().equals(article.getName())){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("name", article.getName());
            values.put("preis", article.getPrice());
            if(article.getCategorie() != null){
                values.put("kategorie", article.getCategorie());
            }
            db.insert("artikel", null, values);
        }
    }

    /**
     * Fügt eine neue zuweißung Bon zu Artikel hinzu
     * @param db Datenbank
     * @param bonId BonId
     * @param articleId ArtikelId
     */
    public void addBonArticle(SQLiteDatabase db, int bonId, int articleId){

        boolean dontmatch = true;

        for(int a : this.getAllBonArticle(db).keySet()){
            if(a == bonId && this.getAllBonArticle(db).get(a) == articleId){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("bonid", bonId);
            values.put("artikelid", articleId);
            db.insert("bonartikel", null, values);
        }
    }

    /**
     * Updated einen Artikel
     * @param db Datenbank
     * @param article Artikel
     */
    public void updateArticle(SQLiteDatabase db, C_Artikel article){

        ContentValues values = new ContentValues();

        values.put("artikelid", article.getId());
        values.put("name", article.getName());
        values.put("preis", article.getPrice());
        values.put("kategorie", article.getCategorie());

        db.update("artikel", values, "artikelid="+article.getId(), null);
    }

    /**
     * Updated einen Laden
     * @param db Datenbank
     * @param laden Laden
     */
    public void updateLaden(SQLiteDatabase db, C_Laden laden){

        ContentValues values = new ContentValues();

        values.put("ladenid", laden.getId());
        values.put("name", laden.getName());

        db.update("laden", values, "ladenid="+laden.getId(), null);
    }

    /**
     * Updated einen Bon
     * @param db Datenbank
     * @param bon Bon
     */
    public void updateBon(SQLiteDatabase db, C_Bon bon){

        ContentValues values = new ContentValues();

        int ladenId;

        if(this.checkIfLadenExist(db, bon.getShopName())){
            ladenId = this.getLaden(db, bon.getShopName()).getId();
        } else {
            this.addLaden(db, new C_Laden(bon.getShopName()));
            ladenId = this.getLaden(db, bon.getShopName()).getId();
        }

        values.put("bonid", bon.getId());
        values.put("bildpfad", bon.getPath());
        values.put("ladenname", ladenId);
        values.put("anschrift", bon.getAdress());
        values.put("sonstigeinfos", bon.getOtherInformations());
        values.put("datum", bon.getDate());
        values.put("garantieende", bon.getGuaranteeEnd());
        values.put("favoriten", bon.getFavourite());
        values.put("garantie", bon.getGuarantee());

        db.update("bon", values, "bonid="+bon.getId(), null);

        for(C_Artikel a : bon.getArticles()) {
            this.addArticle(db, a);
        }

        db.delete("bonartikel", "bonid="+bon.getId(), null);

        for(C_Artikel bonArticle : bon.getArticles()){
            for(C_Artikel dbArticle : this.getAllArticle(db)){
                if(bonArticle.getName().equals(dbArticle.getName()) && bonArticle.getPrice() == dbArticle.getPrice()){
                    this.addBonArticle(db, bon.getId(), dbArticle.getId());
                }
            }
        }

    }

    /**
     * Überprüft ob ein Artikel bereits existiert
     * @param db Datenbank
     * @param articleName Artikel Name
     * @param articlePreis Artikel Preis
     * @return true - existiert, false - existiert nicht
     */
    public boolean checkIfArticleExist(SQLiteDatabase db, String articleName, float articlePreis){
        for(C_Artikel article : this.getAllArticle(db)){
            if(article.getName().equals(articleName) && article.getPrice() == articlePreis) {
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob ein Artikel bereits exisitert
     * @param db Datenbank
     * @param id Id des Artikels
     * @return true - existiert, false - existiert nicht
     */
    public boolean checkIfArticleExist(SQLiteDatabase db, int id){
        for(C_Artikel article : this.getAllArticle(db)){
            if(article.getId() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob ein Laden bereits existiert.
     * @param db Datenbank
     * @param ladenName Name des gesuchten Ladens
     * @return true - existiert, false - existiert nicht
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
     * @return true - existiert, false - existiert nicht
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
     * @return true - existiert, false - existiert nicht
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
     * * * !!!!!! WICHTIG !!!!!!!!!!
     * LÖSCHT EBENSO ALLE KASSENZETTEL DIE MIT DEM LADEN
     * VERKNÜPFT SIND DA SONST DIE APP ABSTÜRZT!!
     * Also bitte den User warnen!
     *
     * Löscht einen spezifischen Laden
     * @param db Datenbank
     * @param id ID des Ladens
     */
    public void removeLaden(SQLiteDatabase db, int id){
        if(this.checkIfLadenExist(db, id)){
            for(C_Bon bon : this.getAllBons(db)){
                if(bon.getShopName().equals(this.getLaden(db, id).getName())){
                    this.removeBon(db, bon.getId());
                }
            }
            db.delete("laden", "ladenid="+id, null);
        } else {
            Log.e("#### DB-HANDLER", "LADEN " + id + " DOES NOT EXIST");
        }
    }

    /**
     * * * !!!!!! WICHTIG !!!!!!!!!!
     * LÖSCHT EBENSO ALLE KASSENZETTEL DIE MIT DEM LADEN
     * VERKNÜPFT SIND DA SONST DIE APP ABSTÜRZT!!
     * Also bitte den User warnen!
     *
     * Löscht einen spezifischen Laden
     * @param db Datenbank
     * @param ladenName Name des Ladens
     */
    public void removeLaden(SQLiteDatabase db, String ladenName){
        if(this.checkIfLadenExist(db, ladenName)){
            this.removeLaden(db, this.getLaden(db, ladenName).getId());
        } else {
            Log.e("#### DB-HANDLER", "LADEN " + ladenName + " DOES NOT EXIST");
        }
    }

    /**
     * Löscht einen spezifischen Bon
     * @param db Datenbank
     * @param id ID des Bons
     */
    public void removeBon(SQLiteDatabase db, int id){
        if(this.checkIfBonExist(db, id)){
            db.delete("bon", "bonid="+id, null);
            db.delete("bonartikel", "bonid="+id, null);
        }
    }

    /**
     * Löscht einen spezifischen Artikel
     * @param db Datenbank
     * @param id ID des Artikels
     */
    public void removeArticle(SQLiteDatabase db, int id){
        if(this.checkIfArticleExist(db, id)){
            db.delete("artikel", "artikelid="+id, null);
            db.delete("bonartikel", "artikelid="+id, null);
        }
    }

    /**
     * Gibt alle Daten aus der DB im Log aus.
     */
    public void showLogAllDBEntries(){

        Log.e("######### DB LAEDEN","#########################################");
        for(C_Laden laden : S.dbHandler.getAllLaeden(S.db)){
            Log.e("######### LADEN: ", laden.toString() + "\n---------------------");
        }

        Log.e("######### DB BONS","#########################################");
        for(C_Bon bon : S.dbHandler.getAllBons(S.db)){
            Log.e("######### BON: ", bon.toString() + "\n---------------------");
        }

        Log.e("######### DB ARTIKEL","#########################################");
        for(C_Artikel artikel : S.dbHandler.getAllArticle(S.db)){
            Log.e("######### ARTIKEL: ", artikel.toString() + "\n---------------------");
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
                "name VARCHAR(100), " +
                "preis DECIMAL(6,2), " +
                "kategorie VARCHAR(50))";

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
}