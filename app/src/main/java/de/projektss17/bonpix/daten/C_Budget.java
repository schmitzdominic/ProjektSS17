package de.projektss17.bonpix.daten;

/**
 * Created by JOhanns on 30.04.2017.
 */

public class C_Budget {

    private int budgetMax, budgetCurrently;
    private int processbar, progressPercentage;
    private String turnus;
    private String title;
    private String year;


    public C_Budget(int budgetMax, int budgetCurrently, int processbar, String turnus, String title, String year){
        this.budgetMax = budgetMax;
        this.budgetCurrently = budgetCurrently;
        this.processbar = processbar;
        this.turnus = turnus;
        this.title = title;
        this.year = year;
    }

    /**
     *
     * @return Rückgabe des Budgets welches die Obergrenze ist
     */
    public int getBudgetMax() {
        return budgetMax;
    }

    /**
     *
     * @return Rückgabe des eingegebenen Monats
     */
    public String getTurnus() {
        return turnus;
    }

    /**
     *
     * @param turnus Setzten des Monats
     */
    public void setTurnus(String turnus) {
        this.turnus = turnus;
    }

    /**
     *
     * @return Rückgabe des Prozensatzes (von budgetCurrently)
     */
    public int getProcessbar() {return processbar;    }

    /**
     *
     * @param processbar Setzten des Prozensatzes (von budgetCurrently)
     */
    public void setProcessbar(int processbar) {
        this.processbar = processbar;
    }

    /**
     *
     * @param budget Setzten der Obergrenze für den Budget
     */
    public void setBudget(int budget) {
        this.budgetMax = budget;
    }

    /**
     *
     * @return Rückgabe des Titels
     */
    public String getTitle() {        return title;    }

    /**
     *
     * @param title Setzen des Titels
     */
    public void setTitle(String title) {        this.title = title;    }

    /**
     *
      * @return Rückgabe des gegenwärtigen Budgets
     */
    public int getBudgetCurrently() {        return budgetCurrently;    }

    /**
     *
     * @param budgetCurrently Setzen des gegenwärtigen Budgets
     */
    public void setBudgetCurrently(int budgetCurrently) {        this.budgetCurrently = budgetCurrently;    }

    /**
     *
     * @return Rückgabe der Prozentsatzen von budgetCurrently gegenüber des budgetMax
     */
    public int getProcessPercentage() {        return (int)(budgetCurrently*100/budgetMax);   }

    /**
     *
     * @return Rückgabe des Jahres
     */
    public String getYear() {return year;    }

    /**
     *
     * @param year Setzen des Jahres
     */
    public void setYear(String year) {        this.year = year;    }

}
