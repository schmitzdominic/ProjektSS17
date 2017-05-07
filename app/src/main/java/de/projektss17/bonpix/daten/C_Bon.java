package de.projektss17.bonpix.daten;

import java.util.ArrayList;

/**
 * Created by Marcus on 15.04.2017.
 */

public class C_Bon {

    private int id;
    private String path,
            shopName,
            adress,
            otherInformations,
            date,
            guaranteeEnd;
    private ArrayList<C_Artikel> articles;
    private boolean favourite,
            guarantee;

    public C_Bon(String path, String shopName, String adress, String otherInformations,
                 String date, String guaranteeEnd, boolean favourite, boolean guarantee, ArrayList<C_Artikel> articles) {
        this(0, path, shopName, adress, otherInformations, date, guaranteeEnd, favourite, guarantee, articles);
    }

    public C_Bon(int id, String path, String shopName, String adress, String otherInformations,
                 String date, String guaranteeEnd, boolean favourite, boolean guarantee) {
        this(id, path, shopName, adress, otherInformations, date, guaranteeEnd, favourite, guarantee, null);
    }

    public C_Bon(int id, String path, String shopName, String adress, String otherInformations,
                 String date, String guaranteeEnd, boolean favourite, boolean guarantee, ArrayList<C_Artikel> articles){
        this.id = id;
        this.path = path;
        this.shopName = shopName;
        this.adress = adress;
        this.otherInformations = otherInformations;
        this.date = date;
        this.guaranteeEnd = guaranteeEnd;
        this.favourite = favourite;
        this.guarantee = guarantee;
        this.articles = articles;
    }

    public int getId(){
        return this.id;
    }

    public String getPath() {
        return path;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAdress() {
        return adress;
    }

    public String getOtherInformations() {
        return otherInformations;
    }

    public String getDate() {
        return date;
    }

    public boolean getFavourite(){
        return this.favourite;
    }

    public boolean getGuarantee(){
        return this.guarantee;
    }

    public String getGuaranteeEnd(){
        return this.guaranteeEnd;
    }

    public ArrayList<C_Artikel> getArticles(){
        return this.articles;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setOtherInformations(String otherInformations) {
        this.otherInformations = otherInformations;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFavourite(boolean favourite){
        this.favourite = favourite;
    }

    public void setGuarantee(boolean guarantee){
        this.guarantee = guarantee;
    }

    public void setGuaranteeEnd(String guaranteeEnd){
        this.guaranteeEnd = guaranteeEnd;
    }

    public void setArticles(ArrayList<C_Artikel> articles){
        this.articles = articles;
    }

    @Override
    public String toString() {
        String out = "ID: "+ this.id + ", \n" +
                "Path: " + this.getPath() + ", \n" +
                "Shopname: " + this.getShopName() + ", \n" +
                "Adress: " + this.getAdress() + ", \n" +
                "Other Informations: " + this.getOtherInformations() + ", \n" +
                "Date: " + this.getDate() + ", \n" +
                "Guaranteeend: " + this.getGuaranteeEnd() + ", \n" +
                "Favourite: " + this.getFavourite() + ", \n" +
                "Guarantee: " +this.getGuarantee();

        if(this.articles != null){
            out += "\nArticles:";
            for(C_Artikel artikel : this.getArticles()){
                out += "\n\t" + artikel.getName() + " - " + artikel.getPrice() + " - " + artikel.getCategorie();
            }
        } else {
            out += "\nNO ARTICLES FOUND!";
        }
        return out;
    }
}