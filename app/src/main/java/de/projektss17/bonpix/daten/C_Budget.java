package de.projektss17.bonpix.daten;

/**
 * Created by JOhanns on 30.04.2017.
 */

public class C_Budget {

    private int budgetMax, budgetCurrently;
    private int processbar, progressPercentage;
    private String monatVon, monatBis;
    private String jahrVon, jahrBis;
    private String title;



    public C_Budget(int budgetMax, int budgetCurrently, int processbar, String monatVon, String jahrVon, String monatBis, String jahrBis, String title){
        this.budgetMax = budgetMax;
        this.budgetCurrently = budgetCurrently;
        this.processbar = processbar;
        this.monatVon = monatVon;
        this.jahrVon = jahrVon;
        this.monatBis = monatBis;
        this.jahrBis = jahrBis;
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

    public String getMonatVon() {
        return monatVon;
    }

    public void setMonatVon(String monatVon) {
        this.monatVon = monatVon;
    }

    public String getMonatBis() {
        return monatBis;
    }

    public void setMonatBis(String monatBis) {
        this.monatBis = monatBis;
    }

    public String getJahrVon() {
        return jahrVon;
    }

    public void setJahrVon(String jahrVon) {
        this.jahrVon = jahrVon;
    }

    public String getJahrBis() {
        return jahrBis;
    }

    public void setJahrBis(String jahrBis) {
        this.jahrBis = jahrBis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}