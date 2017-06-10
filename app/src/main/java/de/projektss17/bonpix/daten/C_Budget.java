package de.projektss17.bonpix.daten;

import java.util.ArrayList;

/**
 * Created by JOhanns on 30.04.2017.
 */

public class C_Budget{

    private int budgetMax, budgetLost,id;
    private String zeitraumVon;
    private String zeitraumBis;
    private String title;
    private String sonstiges;
    private boolean favorite;
    private ArrayList<C_Bon> bons;       // Array-List für alle Kassenbons zu diesem Budget

    public C_Budget(int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite , ArrayList<C_Bon> bons){
        this(0, budgetMax, budgetLost, zeitraumVon, zeitraumBis, title, sonstiges, favorite, bons);
    }

    public C_Budget(int id, int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite){
        this(id, budgetMax, budgetLost, zeitraumVon, zeitraumBis, title, sonstiges, favorite, null);
    }

    public C_Budget(int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite){
        this(0, budgetMax, budgetLost, zeitraumVon, zeitraumBis, title, sonstiges, favorite,  null);
    }

    public C_Budget(int id, int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite, ArrayList<C_Bon> bons){
        this.id = id;
        this.budgetMax = budgetMax;
        this.budgetLost = budgetLost;
        this.zeitraumVon = zeitraumVon;
        this.zeitraumBis = zeitraumBis;
        this.title = title;
        this.sonstiges = sonstiges;
        this.favorite = favorite;
        this.bons = bons;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(int budgetMax) {
        this.budgetMax = budgetMax;
    }

    public int getBudgetLost() {
        return budgetLost;
    }

    public void setBudgetLost(int budgetLost) {
        this.budgetLost = budgetLost;
    }

    public String getZeitraumVon() {
        return zeitraumVon;
    }

    public String getZeitraumBis() {
        return zeitraumBis;
    }

    public void setZeitraumBis(String zeitraumBis) {
        this.zeitraumBis = zeitraumBis;
    }

    public void setZeitraumVon(String zeitraumVon) {
        this.zeitraumVon = zeitraumVon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSonstiges() {
        return sonstiges;
    }

    public void setSonstiges(String sonstiges) {
        this.sonstiges = sonstiges;
    }

    public ArrayList<C_Bon> getBons() {
        return bons;
    }

    public void setBons(ArrayList<C_Bon> bonBons) {
        this.bons = bonBons;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getMonthVon(){
        return getMonthName(zeitraumVon.split("\\.")[1]);
    }

    public String getMonthBis(){
        return getMonthName(zeitraumBis.split("\\.")[1]);
    }

    public String getYearVon(){
        return zeitraumVon.split("\\.")[2];
    }

    public String getYearBis(){
        return zeitraumBis.split("\\.")[2];
    }

    public String getMonthName(String month){

        switch (month) {
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAI";
            case "06":
                return "JUNI";
            case "07":
                return "JULI";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OKT";
            case "11":
                return "NOV";
            case "12":
                return "DEZ";
        }

        return "MONTH";

    }

    @Override
    public String toString() {
        String out = "ID: "+ this.id + ", \n" +
                "Budget: " + this.getBudgetMax() + ", \n" +
                "Stand: " + this.getBudgetLost() + ", \n" +
                "von: " + this.getZeitraumVon() + ", \n" +
                "bis: " + this.getZeitraumBis() + ", \n" +
                "titel: " + this.getTitle() + ", \n" +
                "sonstiges: " + this.getSonstiges();

        if(this.bons != null){
            out += "\nBons:";
            for(C_Bon bon : this.getBons()){
                out += "\n\t" + bon.getId() + " - " + bon.getShopName() + " - " + bon.getTotalPrice();
            }
        } else {
            out += "\nNO BONS FOUND!";
        }
        return out;
    }
}