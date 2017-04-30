package de.projektss17.bonpix.daten;

/**
 * Created by JOhanns on 30.04.2017.
 */

public class C_Budget {

    private int budgetMax, budgetCurrently;
    private int processbar, progressPercentage;
    private String turnus;
    private String title;


    public C_Budget(int budgetMax, int budgetCurrently, int processbar, String turnus, String title){
        this.budgetMax = budgetMax;
        this.budgetCurrently = budgetCurrently;
        this.processbar = processbar;
        this.turnus = turnus;
        this.title = title;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public String getTurnus() {
        return turnus;
    }

    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    public int getProcessbar() {return processbar;    }

    public void setProcessbar(int processbar) {
        this.processbar = processbar;
    }

    public void setBudget(int budget) {
        this.budgetMax = budget;
    }

    public String getTitle() {        return title;    }

    public void setTitle(String title) {        this.title = title;    }


    public int getBudgetCurrently() {        return budgetCurrently;    }

    public void setBudgetCurrently(int budgetCurrently) {        this.budgetCurrently = budgetCurrently;    }

    public int getProcessPercentage() {        return (int)(budgetCurrently*100/budgetMax);   }

}
