package de.projektss17.bonpix.daten;

/**
 * Created by JOhanns on 30.04.2017.
 */

public class C_Budget {

    private int budgetMax, budgetCurrently;
    private int processbar, progressPercentage; // NICHT RELEVANT FÜR DIE DB
    private String zeitraumVon, zeitraumBis, title, sonstiges;



    public C_Budget(int budgetMax, int budgetCurrently, int processbar, String zeitraumVon, String zeitraumBis, String title, String sonstiges){
        this.budgetMax = budgetMax;
        this.budgetCurrently = budgetCurrently;
        this.processbar = processbar;
        this.zeitraumVon = zeitraumVon;
        this.zeitraumBis = zeitraumBis;
        this.title = title;

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

    public void setProcessbar(int processbar) {
        this.processbar = processbar;
    }

    public int getProgressPercentage() {
        return budgetCurrently*100/budgetMax;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getZeitraumVon() {
        return zeitraumVon;
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

    public String getMonthVon(){
        return getMonthName(seperateMonth(zeitraumVon));
    }

    public String getMonthBis(){
        return getMonthName(seperateMonth(zeitraumBis));
    }

    public String getYearVon(){
        return seperateYear(zeitraumVon);
    }

    public String getYearBis(){
        return seperateYear(zeitraumBis);
    }


    public String seperateMonth(String date){

        return date.split("\\.")[1];

    }

    public String seperateYear(String date){

        return date.split("\\.")[2];
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