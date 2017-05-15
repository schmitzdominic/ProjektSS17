package de.projektss17.bonpix.daten;

import java.util.ArrayList;

/**
 * Created by JOhanns on 30.04.2017.
 */

public class C_Budget {

    private int budgetMax, budgetCurrently,id;
    private int processbar, progressPercentage; // NICHT RELEVANT FÜR DIE DB
    private String zeitraumVon, zeitraumBis, title, sonstiges;
    private ArrayList<C_Bon> bons;       // Array-List für alle Kassenbons zu diesem Budget


    public C_Budget(int id, int budgetMax, int budgetCurrently, int processbar, String zeitraumVon, String zeitraumBis, String title, String sonstiges, ArrayList<C_Bon> bons){
        this.id = id;
        this.budgetMax = budgetMax;
        this.budgetCurrently = budgetCurrently;
        this.processbar = processbar;
        this.zeitraumVon = zeitraumVon;
        this.zeitraumBis = zeitraumBis;
        this.title = title;
        this.sonstiges = sonstiges;
        this.bons = bons;

    }


    public C_Budget(int budgetMax, int budgetCurrently, int processbar, String zeitraumVon, String zeitraumBis, String title, String sonstiges){
        this.budgetMax = budgetMax;
        this.budgetCurrently = budgetCurrently;
        this.processbar = processbar;
        this.zeitraumVon = zeitraumVon;
        this.zeitraumBis = zeitraumBis;
        this.title = title;
        this.sonstiges = sonstiges;
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

    public int getBudgetCurrently() {
        return budgetCurrently;
    }

    public void setBudgetCurrently(int budgetCurrently) {
        this.budgetCurrently = budgetCurrently;
    }

    public int getProcessbar() {
        return processbar;
    }


    public int getProgressPercentage() {
        return budgetCurrently*100/budgetMax;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
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

    public ArrayList<C_Bon> getBonBons() {
        return bons;
    }

    public void setBonBons(ArrayList<C_Bon> bonBons) {
        this.bons = bonBons;
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
}