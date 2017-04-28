package de.projektss17.bonpix.daten;

/**
 * Created by Carnivorus on 28.04.2017.
 */

public class C_Budget {

    private String budget, processbar, turnus;


    public C_Budget(String budget, String processbar, String turnus){
        this.budget = budget;
        this.processbar = processbar;
        this.turnus = turnus;
    }

    public String getBudget() {
        return budget;
    }

    public String getTurnus() {
        return turnus;
    }

    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    public String getProcessbar() {

        return processbar;
    }

    public void setProcessbar(String processbar) {
        this.processbar = processbar;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
