package de.projektss17.bonpix.daten;

import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import de.projektss17.bonpix.S;

public class C_DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bonpix";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private C_Laden barDataLaden;
    private C_Laden PieDataLaden;


    public C_DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        checkTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public C_Laden getBarDataLaden() {
        return barDataLaden;
    }

    public void setBarDataLaden(C_Laden barDataLaden) {
        this.barDataLaden = barDataLaden;
    }

    public void backupDataChanged(){
        BackupManager backupManager = new BackupManager(context);
        backupManager.dataChanged();
    }

    public C_Laden getPieDataLaden() {
        return PieDataLaden;
    }

    public void setPieDataLaden(C_Laden pieDataLaden) {
        PieDataLaden = pieDataLaden;
    }

    /**
     * Dreht die Reihenfolge einer Liste um
     * @param list Liste
     * @return umgedrehte Liste
     */
    public ArrayList rotateList(ArrayList list){

        ArrayList rotateList = new ArrayList<>();

        for(int i = list.size()-1; i >= 0; i--){
            rotateList.add(list.get(i));
        }

        return rotateList;
    }

    /**
     * Wird in YYYY-MM-DD convertiert
     * @param date Original Datum DD.MM.YYYY
     * @return convertiertes Datum
     */
    public String convertToDateISO8601(String date){
        if(date.contains(".")){
            return date.split("\\.")[2] + "-" + date.split("\\.")[1] + "-" + date.split("\\.")[0];
        } else {
            return date;
        }

    }

    /**
     * Wird in DD.MM.YYYY convertiert
     * @param date Original Datum YYYY-MM-DD
     * @return convertiertes Datum
     */
    public String convertFromDateISO8601(String date){
        if(date.contains("-")){
            return date.split("-")[2] + "." + date.split("-")[1] + "." + date.split("-")[0];
        } else {
            return date;
        }

    }

    /**
     * Prüft ob sich die Artikel eines Budgets geändert haben.
     * @param db Datenbank
     * @param budget Budget
     */
    public void refreshBudget(SQLiteDatabase db, C_Budget budget){

        ArrayList<Integer> bonIds = new ArrayList<>();
        ArrayList<C_Bon> bonsBudget = budget.getBons();

        for(C_Bon bon : budget.getBons()){
            bonIds.add(bon.getId());
        }

        for(C_Bon bon : this.getBonsBetweenDate(db, budget.getZeitraumVon(), budget.getZeitraumBis())){
            if(!bonIds.contains(bon.getId())){
                bonsBudget.add(bon);
                budget.setBudgetLost((int) (budget.getBudgetLost() + Double.parseDouble(bon.getTotalPrice().replace(",","."))));
            }
        }

        budget.setBons(bonsBudget);
        S.dbHandler.updateBudget(db, budget);

    }

    /**
     * Get all Bons via ArrayList<C_Bon>
     * @param db Datenbank
     * @return ArrayList mit allen Bons
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
                        this.convertFromDateISO8601(cursor.getString(5)),
                        this.convertFromDateISO8601(cursor.getString(6)),
                        cursor.getString(7),
                        cursor.getInt(8) > 0,
                        cursor.getInt(9) > 0);

                bon.setArticles(this.getAllArticleFromBon(db, bon));
                bonsList.add(bon);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return bonsList;
    }

    /**
     * Get all Budgets via ArrayList<C_Budget>
     * @param db Datenbank
     * @return ArrayList mit allen Budgets
     */
    public ArrayList<C_Budget> getAllBudgets(SQLiteDatabase db){

        ArrayList<C_Budget> budgetList = new ArrayList<>();
        String query = "SELECT * FROM budget";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {

                C_Budget budget = new C_Budget(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        convertFromDateISO8601(cursor.getString(3)),
                        convertFromDateISO8601(cursor.getString(4)),
                        cursor.getString(5),
                        cursor.getString(6));

                budget.setBons(this.getAllBonsFromBudget(db, budget));
                budgetList.add(budget);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return budgetList;
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
     * Gibt alle Bons eines Budgets zurück
     * @param db Datenbank
     * @param budget Budget
     * @return ArrayList mit allen dazugehörigen Bons
     */
    public ArrayList<C_Bon> getAllBonsFromBudget(SQLiteDatabase db, C_Budget budget){

        ArrayList<C_Bon> bonList = new ArrayList<>();

        String query = "SELECT a.bonid, a.bildpfad, a.ladenname, a.anschrift, a.sonstigeinfos, a.datum, a.garantieende, a.gesamtpreis, a.favoriten, a.garantie FROM bon a " +
                "LEFT JOIN bonbudget bb ON bb.bonid = a.bonid " +
                "WHERE bb.budgetid = '" + budget.getId() + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                C_Bon bon = new C_Bon(cursor.getInt(0),
                        cursor.getString(1),
                        this.getLaden(db, cursor.getInt(2)).getName(),
                        cursor.getString(3),
                        cursor.getString(4),
                        this.convertFromDateISO8601(cursor.getString(5)),
                        this.convertFromDateISO8601(cursor.getString(6)),
                        cursor.getString(7),
                        cursor.getInt(8) > 0,
                        cursor.getInt(9) > 0);

                bon.setArticles(this.getAllArticleFromBon(db, bon));
                bonList.add(bon);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return bonList;
    }

    /**
     * Gibt alle bons zwischen zwei datums zurück
     * @param db Datenbank
     * @param date1 Datum von
     * @param date2 Datum bis
     * @return Alle bons in diesem Zeitraum
     */
    public ArrayList<C_Bon> getBonsBetweenDate(SQLiteDatabase db, String date1, String date2){

        ArrayList<C_Bon> list = new ArrayList<>();
        String query = "SELECT * FROM bon WHERE datum BETWEEN date('"+this.convertToDateISO8601(date1)+"') AND date('"+this.convertToDateISO8601(date2)+"')";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                C_Bon bon = new C_Bon(cursor.getInt(0),
                        cursor.getString(1),
                        this.getLaden(db, cursor.getInt(2)).getName(),
                        cursor.getString(3),
                        cursor.getString(4),
                        this.convertFromDateISO8601(cursor.getString(5)),
                        this.convertFromDateISO8601(cursor.getString(6)),
                        cursor.getString(7),
                        cursor.getInt(8) > 0,
                        cursor.getInt(9) > 0);

                bon.setArticles(this.getAllArticleFromBon(db, bon));
                list.add(bon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rotateList(list);
    }

    /**
     * Gibt die Summe aller Gesamtpreise einer Bonliste zurück
     * @param bons Bonliste
     * @return Alle Gesamtpreise summiert
     */
    public double getTotalPriceFromBonsSumup(ArrayList<C_Bon> bons){

        double summe = 0;

        for(C_Bon bon : bons){
            summe += Double.parseDouble(bon.getTotalPrice().replace(",","."));
        }

        return summe;
    }

    /**
     * Holt die neuesten Bons aus der Datenbank
     * @param db Datenbank
     * @param anzahl Anzahl der Bons die herausgeholt werden soll
     * @return ArrayList mit den aktuellsten Bons
     */
    public ArrayList<C_Bon> getNumberOfNewestBons(SQLiteDatabase db, int anzahl){

        ArrayList<C_Bon> list = new ArrayList<>();
        String query = "SELECT * FROM bon WHERE bonid = (SELECT MAX(bonid) FROM bon)";

        for(int i = 1; i < anzahl; i++){
            query += " OR bonid = (SELECT MAX(bonid)-"+i+" FROM bon)";
        }

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                C_Bon bon = new C_Bon(cursor.getInt(0),
                        cursor.getString(1),
                        this.getLaden(db, cursor.getInt(2)).getName(),
                        cursor.getString(3),
                        cursor.getString(4),
                        this.convertFromDateISO8601(cursor.getString(5)),
                        this.convertFromDateISO8601(cursor.getString(6)),
                        cursor.getString(7),
                        cursor.getInt(8) > 0,
                        cursor.getInt(9) > 0);

                bon.setArticles(this.getAllArticleFromBon(db, bon));
                list.add(bon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rotateList(list);

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
     * Get the Data for LineCharts
     * @return Last
     */
    public List<Entry> getLineData(SQLiteDatabase db, int count){

        List<Entry> dataList = new ArrayList<>();
        int counter = 1;

        ArrayList<C_Bon> revertedBonList = new ArrayList<>();
        ArrayList<C_Bon> bonList = this.getAllBons(db);

        for(int i = bonList.size()-1; i >= 0; i--){
            revertedBonList.add(bonList.get(i));
        }

        for(C_Bon bon : revertedBonList){

            float totalPrice = 0;

            for(C_Artikel artikel : bon.getArticles()){
                totalPrice += artikel.getPrice();
            }

            String sTotalPrice = "" + totalPrice;

            dataList.add(new Entry((float) counter, Float.parseFloat(sTotalPrice)));
            counter++;
            if(counter == count) break;
        }

        return dataList;
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
     * Gibt alle Bon Budgets zurück
     * @param db Datenbank
     * @return HashMap mit allen Zuweißungen Integer, Integer
     */
    public HashMap<Integer, Integer> getAllBonBudget(SQLiteDatabase db){

        HashMap<Integer, Integer> list = new HashMap<>();
        String query = "SELECT * FROM bonbudget";
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
     * Gibt ein Budget zurück falls dieses existiert
     * @param db Datenbank
     * @param id ID des Budgets
     * @return C_Budget wenn es existiert, null wenn nicht
     */
    public C_Budget getBudget(SQLiteDatabase db, int id){

        if(checkIfBudgetExist(db, id)){
            for(C_Budget budget : this.getAllBudgets(db)){
                if(budget.getId() == id){
                    return budget;
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
        values.put("datum", this.convertToDateISO8601(bon.getDate()));
        values.put("garantieende", this.convertToDateISO8601(bon.getGuaranteeEnd()));
        values.put("gesamtpreis", bon.getTotalPrice());
        values.put("favoriten", bon.getFavourite());
        values.put("garantie", bon.getGuarantee());

        db.insert("bon", null, values);
        backupDataChanged();

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
     * Fügt ein neues Budget hinzu
     * @param db Datenbank
     * @param budget Budget
     */
    public void addBudget(SQLiteDatabase db, C_Budget budget){

        ContentValues values = new ContentValues();

        values.put("budget", budget.getBudgetMax());
        values.put("stand", budget.getBudgetLost());
        values.put("von", this.convertToDateISO8601(budget.getZeitraumVon()));
        values.put("bis", this.convertToDateISO8601(budget.getZeitraumBis()));
        values.put("titel", budget.getTitle());
        values.put("sonstiges", budget.getSonstiges());

        db.insert("budget", null, values);
        backupDataChanged();

        int budgetid = this.getAllBudgets(S.db).get(this.getAllBudgets(S.db).size()-1).getId();

        if(this.checkIfBudgetExist(db, budgetid)){
            for(C_Bon budgetBon : budget.getBons()){
                for(C_Bon dbBon : this.getAllBons(db)){
                    if(budgetBon.getId() == dbBon.getId()){
                        this.addBudgetBon(db, budgetid, dbBon.getId());
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
        backupDataChanged();
    }

    /**
     * Fügt einen neuen Artikel hinzu
     * @param db Datenbank
     * @param article Artikel
     */
    public void addArticle(SQLiteDatabase db, C_Artikel article){

        boolean dontmatch = true;

        for(C_Artikel a : this.getAllArticle(db)){
            if(a.getName().equals(article.getName()) && a.getPrice() == article.getPrice()){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("name", article.getName());
            values.put("preis", article.getPrice());
            if(article.getCategory() != null){
                values.put("kategorie", article.getCategory());
            }
            db.insert("artikel", null, values);
            backupDataChanged();
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
            backupDataChanged();
        }
    }

    /**
     * Fügt eine neue zuweißung Budget zu Bon hinzu
     * @param db Datenbank
     * @param budgetId Budget Id
     * @param bonId Bon Id
     */
    public void addBudgetBon(SQLiteDatabase db, int budgetId, int bonId){

        boolean dontmatch = true;

        for(int a : this.getAllBonBudget(db).keySet()){
            if(a == budgetId && this.getAllBonBudget(db).get(a) == bonId){
                dontmatch = false;
            }
        }

        if(dontmatch){
            ContentValues values = new ContentValues();
            values.put("budgetid", budgetId);
            values.put("bonid", bonId);
            db.insert("bonbudget", null, values);
            backupDataChanged();
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
        values.put("kategorie", article.getCategory());

        db.update("artikel", values, "artikelid="+article.getId(), null);
        backupDataChanged();
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
        backupDataChanged();
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
        values.put("datum", this.convertToDateISO8601(bon.getDate()));
        values.put("garantieende", this.convertToDateISO8601(bon.getGuaranteeEnd()));
        values.put("gesamtpreis", bon.getTotalPrice());
        values.put("favoriten", bon.getFavourite());
        values.put("garantie", bon.getGuarantee());

        db.update("bon", values, "bonid="+bon.getId(), null);

        for(C_Artikel a : bon.getArticles()) {
            this.addArticle(db, a);
        }

        db.delete("bonartikel", "bonid="+bon.getId(), null);
        backupDataChanged();

        for(C_Artikel bonArticle : bon.getArticles()){
            for(C_Artikel dbArticle : this.getAllArticle(db)){
                if(bonArticle.getName().equals(dbArticle.getName()) && bonArticle.getPrice() == dbArticle.getPrice()){
                    this.addBonArticle(db, bon.getId(), dbArticle.getId());
                }
            }
        }

    }

    /**
     * Updated ein Budget
     * @param db Datenbank
     * @param budget Budget
     */
    public void updateBudget(SQLiteDatabase db, C_Budget budget){

        ContentValues values = new ContentValues();

        values.put("budgetid", budget.getId());
        values.put("budget", budget.getBudgetMax());
        values.put("stand", budget.getBudgetLost());
        values.put("von", this.convertToDateISO8601(budget.getZeitraumVon()));
        values.put("bis", this.convertToDateISO8601(budget.getZeitraumBis()));
        values.put("titel", budget.getTitle());
        values.put("sonstiges", budget.getSonstiges());

        db.update("budget", values, "budgetid="+budget.getId(), null);
        db.delete("bonbudget", "budgetid="+budget.getId(), null);

        backupDataChanged();

        for(C_Bon budgetBon : budget.getBons()){
            for(C_Bon dbBon : this.getAllBons(db)){
                if(budgetBon.getId() == dbBon.getId()){
                    this.addBudgetBon(db, budget.getId(), dbBon.getId());
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
     * Überprüft ob ein Budget bereits existiert.
     * @param db Datenbank
     * @param id des Budgets
     * @return true - existiert, false - existiert nicht
     */
    public boolean checkIfBudgetExist(SQLiteDatabase db, int id){
        for(C_Budget budget : this.getAllBudgets(db)){
            if(budget.getId() == id){
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
     * Löscht ein Budget
     * @param db Datenbank
     * @param id ID des Budgets
     */
    public void removeBudget(SQLiteDatabase db, int id){
        if(this.checkIfBudgetExist(db, id)){
            db.delete("budget", "budgetid="+id, null);
            db.delete("bonbudget", "budgetid="+id, null);
        }
    }

    /**
     * Get all Bons with specific Store
     * @param db
     * @param name
     * @return
     */
    public ArrayList<C_Bon> getBonsOfStore(SQLiteDatabase db, String name){
        ArrayList<C_Bon> list = new ArrayList<>();
        for (C_Bon bon : S.dbHandler.getAllBons(db)){
            if (bon.getShopName().equals(name)){
                list.add(bon);
            }
            else {
                continue;
            }
        }
        return list;
    }

    /**
     * Gibt die Anzahl aller vorhandenen Bons zurück
     * @param db Datenbank
     * @return Anzahl
     */
    public int getAllBonsCount(SQLiteDatabase db){
        return this.getAllBonsCount(db, null, null);
    }

    /**
     * Gibt die Anzahl aller vorhandenen Bons zurück
     * @param db Datenbank
     * @param date1 Von Datum
     * @param date2 Bis Datum
     * @return Anzahl
     */
    public int getAllBonsCount(SQLiteDatabase db, String date1, String date2){

        int count = 0;
        String query = date1 != null && date2 != null ?
                "SELECT bonid FROM bon WHERE datum BETWEEN date('"+this.convertToDateISO8601(date1)+"') AND date('"+this.convertToDateISO8601(date2)+"')" :
                "SELECT bonid FROM bon";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        return count;
    }

    /**
     * Gibt die Anzahl aller vorhandenen Laeden zurück
     * @param db Datenbank
     * @return Anzahl
     */
    public int getAllLaedenCount(SQLiteDatabase db){
        return this.getAllLaeden(db).size();
    }

    /**
     * Gibt die Anzahl aller vorhandenen Artikel zurück
     * @param db Datenbank
     * @return Anzahl
     */
    public int getAllArticleCount(SQLiteDatabase db){
        return this.getAllArticle(db).size();
    }

    /**
     * Gibt den Gesamtbetrag aller Ausgaben wieder
     * @param db Datenbank
     * @return Gesamtbetrag
     */
    public String getTotalExpenditure(SQLiteDatabase db){
        return getTotalExpenditure(db, null, null);
    }

    /**
     * Gibt den Gesamtbetrag aller Ausgaben wieder
     * @param db Datenbank
     * @param date1 Von Datum
     * @param date2 Bis Datum
     * @return Gesamtbetrag
     */
    public String getTotalExpenditure(SQLiteDatabase db, String date1, String date2){

        double totalExpenditure = 0;
        String query = date1 != null && date2 != null ?
                "SELECT gesamtpreis FROM bon WHERE datum BETWEEN date('"+this.convertToDateISO8601(date1)+"') AND date('"+this.convertToDateISO8601(date2)+"')" :
                "SELECT gesamtpreis FROM bon";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                totalExpenditure += Double.parseDouble(cursor.getString(0).replace(",","."));
            } while (cursor.moveToNext());
        }
        cursor.close();

        totalExpenditure = Math.round(totalExpenditure * 100) / 100.00;
        DecimalFormat df = new DecimalFormat("#0.00");

        return df.format(totalExpenditure);
    }

    /**
     * Gibt den meistbesuchten Laden zurück
     * @param db Datenbank
     * @return Meistbesuchten Laden
     */
    public C_Laden getMostVisitedLaden(SQLiteDatabase db){
        return this.getMostVisitedLaden(db, null, null);
    }

    /**
     * Gibt den meistbesuchten Laden zurück
     * @param db Datenbank
     * @param date1 Von Datum
     * @param date2 Bis Datum
     * @return Meistbesuchten Laden
     */
    public C_Laden getMostVisitedLaden(SQLiteDatabase db, String date1, String date2){

        HashMap<Integer, Integer> visitList = new HashMap<>();

        for(C_Laden laden : this.getAllLaeden(db)){
            visitList.put(laden.getId(), 0);
        }

        String query = date1 != null && date2 != null ?
                "SELECT ladenname FROM bon WHERE datum BETWEEN date('"+this.convertToDateISO8601(date1)+"') AND date('"+this.convertToDateISO8601(date2)+"')" :
                "SELECT ladenname FROM bon";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                int visit = visitList.get(cursor.getInt(0));
                visitList.put(cursor.getInt(0), ++visit);
            } while (cursor.moveToNext());
        }
        cursor.close();

        int mostUsed = 0, id = 0;

        for(Integer count : visitList.keySet()){
            if(visitList.get(count) > mostUsed){
                id = count;
                mostUsed = visitList.get(count);
            }
        }

        return id == 0 ? new C_Laden("") : this.getLaden(db, id);

    }

    /**
     * Durchschnittsbetrag eines Ladens
     * @param db Datenbank
     * @param laden Laden von dem wir den Durchschnittsbetrag bekommen wollen
     * @return Betrag
     */
    public String averageExpenditureLaden(SQLiteDatabase db, C_Laden laden){
        return this.averageExpenditureLaden(db, laden, null, null);
    }

    /**
     * Durchschnittsbetrag eines Ladens innerhalb eines Zeitraums
     * @param db Datenbank
     * @param laden Laden von dem wir den Durchschnittsbetrag bekommen wollen
     * @param date1 Von Datum
     * @param date2 Bis Datum
     * @return Betrag
     */
    public String averageExpenditureLaden(SQLiteDatabase db, C_Laden laden, String date1, String date2){

        double gesPreis = 0, anz = 0;

        String query = date1 != null && date2 != null ?
                "SELECT gesamtpreis FROM bon WHERE datum BETWEEN date('"+this.convertToDateISO8601(date1)+"') AND date('"+this.convertToDateISO8601(date2)+"') AND ( ladenname = '" + laden.getId() + "' )" :
                "SELECT gesamtpreis FROM bon WHERE ( ladenname = '" + laden.getId() + "' )";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                gesPreis += Double.parseDouble(cursor.getString(0).replace(",","."));
                anz++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        double average = Math.round((gesPreis / anz) * 100) / 100.00;
        DecimalFormat df = new DecimalFormat("#0.00");

        return df.format(average);

    }

    /**
     * Gibt eine Sortierte Liste der Läden mit deren Ausgaben zurück hohe Ausgabe > niedrige Ausgabe
     * @param db Datenbank
     * @return Sortierte Liste
     */
    public SortedSet<Hashtable.Entry<Integer, Float>> getExpenditureAllLaeden(SQLiteDatabase db){
        return getExpenditureAllLaeden(db, null, null);
    }

    /**
     * Gibt eine Sortierte Liste der Läden mit deren Ausgaben zurück hohe Ausgabe > niedrige Ausgabe
     * @param db Datenbank
     * @param date1 Von - Datum
     * @param date2 Bis - Datum
     * @return Sortierte Liste
     */
    public SortedSet<Hashtable.Entry<Integer, Float>> getExpenditureAllLaeden(SQLiteDatabase db, String date1, String date2){

        TreeMap<Integer, Float> visitList = new TreeMap<>();

        for(C_Laden laden : this.getAllLaeden(db)){
            visitList.put(laden.getId(), (float)0.0);
        }

        String query = date1 != null && date2 != null ?
                "SELECT gesamtpreis, ladenname FROM bon WHERE datum BETWEEN date('"+this.convertToDateISO8601(date1)+"') AND date('"+this.convertToDateISO8601(date2)+"')" :
                "SELECT gesamtpreis, ladenname FROM bon";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                float betrag = visitList.get(cursor.getInt(1));
                betrag += Float.parseFloat(cursor.getString(0).replace(",","."));
                visitList.put(cursor.getInt(1), betrag);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return entriesSortedByValues(visitList);

    }

    public ArrayList<String> getExpenditureLastWeek(SQLiteDatabase db){

        ArrayList<String> expList = new ArrayList<>();

        for(String date : S.getFullWeek()){
            expList.add(S.getWeekday(context.getResources(), S.getWeekdayNumber(date)) + "/" + S.dbHandler.getTotalExpenditure(S.db, date, date));
        }

        return expList;

    }


    /**
     * Gibt Artikel sortiert nach der Häufigkeit zurück
     * @param db Datenbank
     * @return sortierte Liste
     */
    public SortedSet<Hashtable.Entry<String, Integer>> getTopArticles(SQLiteDatabase db){
        return getTopArticles(db, null, null);
    }

    /**
     * Gibt Artikel sortiert nach der Häufigkeit zurück
     * @param db Datenbank
     * @param date1 Von - Datum
     * @param date2 Bis - Datum
     * @return sortierte Liste
     */
    public SortedSet<Hashtable.Entry<String, Integer>> getTopArticles(SQLiteDatabase db, String date1, String date2){

        TreeMap<String, Integer> buyList = new TreeMap<>();
        ArrayList<C_Bon> bonList;
        ArrayList<C_Artikel> articleList;

        for(C_Artikel article : this.getAllArticle(db)){
            buyList.put(article.getName(), 0);
        }

        bonList = date1 != null && date2 != null ?
                this.getBonsBetweenDate(db, date1, date2) :
                this.getAllBons(db);

        for(C_Bon bon : bonList){
            articleList = this.getAllArticleFromBon(db, bon);
            for(C_Artikel article : articleList){
                buyList.put(article.getName(), buyList.get(article.getName()) + 1);
            }
        }

        return entriesSortedByValues(buyList);
    }

    /**
     * Gibt die Anzahl sortiert der Bons pro Laden zurück
     * @param db Datenbank
     * @return Anzahl der Bons pro Laden
     */
    public SortedSet<Hashtable.Entry<String, Integer>> getBonsCountPerLaden(SQLiteDatabase db){
        return this.getBonsCountPerLaden(db, null, null);
    }

    /**
     * Gibt die Anzahl sortiert der Bons pro Laden zurück
     * @param db Datenbank
     * @param date1 Von - Datum
     * @param date2 Bis - Datum
     * @return Anzahl der Bons pro Laden
     */
    public SortedSet<Hashtable.Entry<String, Integer>> getBonsCountPerLaden(SQLiteDatabase db, String date1, String date2){

        TreeMap<String, Integer> ladenList = new TreeMap<>();
        ArrayList<C_Bon> bonList;

        for(C_Laden laden : this.getAllLaeden(db)){
            ladenList.put(laden.getName(), 0);
        }

        bonList = date1 != null && date2 != null ?
                this.getBonsBetweenDate(db, date1, date2) :
                this.getAllBons(db);

        for(String laden : ladenList.keySet()){
            for(C_Bon bon : bonList){
                if(bon.getShopName().equals(laden)){
                    ladenList.put(laden, ladenList.get(laden) + 1);
                }
            }
        }

        return entriesSortedByValues(ladenList);
    }

    /**
     * Sortiert eine Map
     * @param map Map die Sortiert werden soll
     * @param <K> Parameter 1
     * @param <V> Parameter 2
     * @return Sortierte Liste
     */
    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    /**
     * Gibt die Bar Daten für alle Laeden zurück
     * @param db Datenbank
     * @param number Den wievielten Wert aus der Plazierung
     * @return Liste mit BarEntrys
     */
    public List<BarEntry> getBarDataLaedenExpenditure(SQLiteDatabase db, int number){
        return this.getBarDataLaedenExpenditure(db, null, null, number);
    }

    /**
     * Gibt die Bar Daten für alle Laeden zurück
     * @param db Datenbank
     * @param date1 Von - Datum
     * @param date2 Bis - Datum
     * @param number Den wievielten Wert aus der Plazierung
     * @return Liste mit BarEntrys
     */
    public List<BarEntry> getBarDataLaedenExpenditure(SQLiteDatabase db, String date1, String date2, int number){

        List<BarEntry> dataList = new ArrayList<>();
        int count = 0;

        if(date1 != null && date2 != null){

            for(Hashtable.Entry<Integer, Float> entry : this.getExpenditureAllLaeden(db, date1, date2)){
                if(count == number){
                    dataList.add(new BarEntry(count+1, entry.getValue()));
                    this.setBarDataLaden(this.getLaden(db, entry.getKey()));
                    break;
                }
                count++;
            }

        } else {

            for(Hashtable.Entry<Integer, Float> entry : this.getExpenditureAllLaeden(db)){

                if(count == number){
                    dataList.add(new BarEntry(count+1, entry.getValue()));
                    this.setBarDataLaden(this.getLaden(db, entry.getKey()));
                    break;
                }
                count++;
            }
        }

        return dataList;
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

        Log.e("######### DB BUDGETS","#########################################");
        for(C_Budget budget : S.dbHandler.getAllBudgets(S.db)){
            Log.e("######### BUDGET: ", budget.toString() + "\n---------------------");
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
                "datum TEXT, " +
                "garantieende TEXT, " +
                "gesamtpreis VARCHAR(255), " +
                "favoriten BOOLEAN, " +
                "garantie BOOLEAN, " +
                "FOREIGN KEY (ladenname) REFERENCES laden(ladenid))";

        String CREATE_TABLE_Artikel = "CREATE TABLE IF NOT EXISTS artikel (artikelid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(100), " +
                "preis DECIMAL(6,2), " +
                "kategorie VARCHAR(50))";

        String CREATE_TABLE_Budget = "CREATE TABLE IF NOT EXISTS budget (budgetid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "budget INTEGER, " +
                "stand INTEGER, " +
                "von TEXT, " +
                "bis TEXT, " +
                "titel VARCHAR(255), " +
                "sonstiges VARCHAR(255))";

        String CREATE_TABLE_BonArtikel = "CREATE TABLE IF NOT EXISTS bonartikel (bonid INTEGER NOT NULL, " +
                "artikelid INTEGER NOT NULL, " +
                "PRIMARY KEY (bonid, artikelid), " +
                "FOREIGN KEY (bonid) REFERENCES bon(bonid), " +
                "FOREIGN KEY (artikelid) REFERENCES artikel(artikelid))";

        String CREATE_TABLE_BonBudget = "CREATE TABLE IF NOT EXISTS bonbudget (budgetid INTEGER NOT NULL, " +
                "bonid INTEGER NOT NULL, " +
                "PRIMARY KEY (budgetid, bonid), " +
                "FOREIGN KEY (budgetid) REFERENCES budget(budgetid), " +
                "FOREIGN KEY (bonid) REFERENCES bon(bonid))";

        db.execSQL(CREATE_TABLE_Laden);
        db.execSQL(CREATE_TABLE_Bon);
        db.execSQL(CREATE_TABLE_Artikel);
        db.execSQL(CREATE_TABLE_Budget);
        db.execSQL(CREATE_TABLE_BonArtikel);
        db.execSQL(CREATE_TABLE_BonBudget);

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
}