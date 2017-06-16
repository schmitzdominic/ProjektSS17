package de.projektss17.bonpix.daten;

import java.util.ArrayList;

public class C_Budget{

    private int budgetMax, budgetLost,id;
    private String zeitraumVon;
    private String zeitraumBis;
    private String title;
    private String sonstiges;
    private boolean favorite;
    private ArrayList<C_Bon> bons;

    /**
     * Standard Constructor
     * @param budgetMax
     * @param budgetLost
     * @param zeitraumVon
     * @param zeitraumBis
     * @param title
     * @param sonstiges
     * @param favorite
     * @param bons
     */
    public C_Budget(int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite , ArrayList<C_Bon> bons){
        this(0, budgetMax, budgetLost, zeitraumVon, zeitraumBis, title, sonstiges, favorite, bons);
    }

    /**
     * 2nd Constructor with extra parameter id but without bons
     * @param id
     * @param budgetMax
     * @param budgetLost
     * @param zeitraumVon
     * @param zeitraumBis
     * @param title
     * @param sonstiges
     * @param favorite
     */
    public C_Budget(int id, int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite){
        this(id, budgetMax, budgetLost, zeitraumVon, zeitraumBis, title, sonstiges, favorite, null);
    }

    /**
     * 3rd Constructor without parameters id and bons
     * @param budgetMax
     * @param budgetLost
     * @param zeitraumVon
     * @param zeitraumBis
     * @param title
     * @param sonstiges
     * @param favorite
     */
    public C_Budget(int budgetMax, int budgetLost, String zeitraumVon, String zeitraumBis, String title, String sonstiges, boolean favorite){
        this(0, budgetMax, budgetLost, zeitraumVon, zeitraumBis, title, sonstiges, favorite,  null);
    }

    /**
     * 4th Constructor
     * @param id
     * @param budgetMax
     * @param budgetLost
     * @param zeitraumVon
     * @param zeitraumBis
     * @param title
     * @param sonstiges
     * @param favorite
     * @param bons
     */
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

    /**
     * Get Budget Id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set Budget Id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get Maximum of Budget
     * @return budgetMax
     */
    public int getBudgetMax() {
        return budgetMax;
    }

    /**
     * Set Maximum of Budget
     * @param budgetMax
     */
    public void setBudgetMax(int budgetMax) {
        this.budgetMax = budgetMax;
    }

    /**
     * Get lost amount of the budget
     * @return
     */
    public int getBudgetLost() {
        return budgetLost;
    }

    /**
     * Set lost amount of the budget
     * @param budgetLost
     */
    public void setBudgetLost(int budgetLost) {
        this.budgetLost = budgetLost;
    }

    /**
     * Get date when Budget starts
     * @return
     */
    public String getZeitraumVon() {
        return zeitraumVon;
    }

    /**
     * Get date when Budget ends
     * @return
     */
    public String getZeitraumBis() {
        return zeitraumBis;
    }

    /**
     * Set date when the budget ends
     * @param zeitraumBis
     */
    public void setZeitraumBis(String zeitraumBis) {
        this.zeitraumBis = zeitraumBis;
    }

    /**
     * Set date when the budget starts
     * @param zeitraumVon
     */
    public void setZeitraumVon(String zeitraumVon) {
        this.zeitraumVon = zeitraumVon;
    }

    /**
     * Get title of Budget
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title of Budget
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get other informations
     * @return other informations
     */
    public String getSonstiges() {
        return sonstiges;
    }

    /**
     * Set other informations
     * @param sonstiges
     */
    public void setSonstiges(String sonstiges) {
        this.sonstiges = sonstiges;
    }

    /**
     * Get BonsList
     * @return bonsList
     */
    public ArrayList<C_Bon> getBons() {
        return bons;
    }

    /**
     * Set Bons to passed Bons
     * @param bonBons
     */
    public void setBons(ArrayList<C_Bon> bonBons) {
        this.bons = bonBons;
    }

    /**
     * Get boolean of favorite
     * @return favorite - true/false
     */
    public boolean getFavorite() {
        return favorite;
    }

    /**
     * Set as favorite
     * @param favorite
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Get the month of the budget starting date
     * @return month name
     */
    public String getMonthVon(){
        return getMonthName(zeitraumVon.split("\\.")[1]);
    }

    /**
     * Get the month of the budget ending date
     * @return month name
     */
    public String getMonthBis(){
        return getMonthName(zeitraumBis.split("\\.")[1]);
    }

    /**
     * Get the year of the budget starting date
     * @return year
     */
    public String getYearVon(){
        return zeitraumVon.split("\\.")[2];
    }

    /**
     * Get the year of the budget ending date
     * @return year
     */
    public String getYearBis(){
        return zeitraumBis.split("\\.")[2];
    }

    /**
     * Get Month Name out of integer
     * @param month
     * @return Name of Month
     */
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