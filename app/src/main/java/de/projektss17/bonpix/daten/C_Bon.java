package de.projektss17.bonpix.daten;

import java.util.ArrayList;

public class C_Bon {

    private int id;
    private String path;
    private String shopName;
    private String adress;
    private String otherInformations;
    private String date;
    private String guaranteeEnd;
    private String totalPrice;
    private ArrayList<C_Artikel> articles;
    private boolean favourite,
            guarantee;

    /**
     * Fauler Konstruktor 1
     * @param path
     * @param shopName
     * @param adress
     * @param otherInformations
     * @param date
     * @param guaranteeEnd
     * @param favourite
     * @param guarantee
     * @param articles
     */
    public C_Bon(String path, String shopName, String adress, String otherInformations,
                 String date, String guaranteeEnd, String totalPrice, boolean favourite, boolean guarantee, ArrayList<C_Artikel> articles) {
        this(0, path, shopName, adress, otherInformations, date, guaranteeEnd, totalPrice, favourite, guarantee, articles);
    }

    /**
     * Fauler Konstruktor 2
     * @param id
     * @param path
     * @param shopName
     * @param adress
     * @param otherInformations
     * @param date
     * @param guaranteeEnd
     * @param favourite
     * @param guarantee
     */
    public C_Bon(int id, String path, String shopName, String adress, String otherInformations,
                 String date, String guaranteeEnd, String totalPrice, boolean favourite, boolean guarantee) {
        this(id, path, shopName, adress, otherInformations, date, guaranteeEnd, totalPrice, favourite, guarantee, null);
    }

    /**
     * Fleißiger Konstruktor (weißt alle werte entsprechend zu!)
     * @param id
     * @param path
     * @param shopName
     * @param adress
     * @param otherInformations
     * @param date
     * @param guaranteeEnd
     * @param favourite
     * @param guarantee
     * @param articles
     */
    public C_Bon(int id, String path, String shopName, String adress, String otherInformations,
                 String date, String guaranteeEnd, String totalPrice, boolean favourite, boolean guarantee, ArrayList<C_Artikel> articles){
        this.id = id;
        this.path = path;
        this.shopName = shopName;
        this.adress = adress;
        this.otherInformations = otherInformations;
        this.date = date;
        this.guaranteeEnd = guaranteeEnd;
        this.totalPrice = totalPrice;
        this.favourite = favourite;
        this.guarantee = guarantee;
        this.articles = articles;
    }

    /**
     * Gibt die Id zurück
     * @return id int
     */
    public int getId(){
        return this.id;
    }

    /**
     * Gibt den Pfad des Bildes (Kassenzettel) zurück
     * @return Pfad String
     */
    public String getPath() {
        return path;
    }

    /**
     * Gibt den Namen des Ladens zurück
     * @return Laden name String
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * Gibt die Adresse zurück
     * @return Adresse String
     */
    public String getAdress() {
        return adress;
    }

    /**
     * Gibt alle weiteren Informationen zurück
     * @return andere Informationen String
     */
    public String getOtherInformations() {
        return otherInformations;
    }

    /**
     * Gibt das Datum des Bons zurück
     * @return Bondatum String
     */
    public String getDate() {
        return date;
    }

    /**
     * Gibt true zurück wenn der Kassenzettel ein Favorit ist, sonst false
     * @return true - Favorit, false - kein Favorit
     */
    public boolean getFavourite(){
        return this.favourite;
    }

    /**
     * Gibt true zurück wenn der Kassenzettel sich in der Garantie befindet, sonst false
     * @return true - Garantie, false - keine Garantie
     */
    public boolean getGuarantee(){
        return this.guarantee;
    }

    /**
     * Gibt das Garantie Ende zurück
     * @return Garantieende String
     */
    public String getGuaranteeEnd(){
        return this.guaranteeEnd;
    }

    /**
     * Gibt eine List mit dem Kassenzettel zugehörigen Artikeln wieder
     * @return Liste mit Artikeln C_Artikel Array List
     */
    public ArrayList<C_Artikel> getArticles(){
        return this.articles;
    }

    /**
     * Gibt den Gesamtpreis zurück
     * @return String als Gesamtpreis
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * Setzt eine neue Id
     * @param id int
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Setzt einen neuen Pfad
     * @param path String
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Setzt einen neuen Ladenname
     * @param shopName String
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * Setzt eine neue Adresse
     * @param adress String
     */
    public void setAdress(String adress) {
        this.adress = adress;
    }

    /**
     * Setzt weitere Informationen
     * @param otherInformations String
     */
    public void setOtherInformations(String otherInformations) {
        this.otherInformations = otherInformations;
    }

    /**
     * Setzt das Datum des Bons
     * @param date String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Setzt den Status ob es sich um einen Favoriten handelt
     * @param favourite boolean
     */
    public void setFavourite(boolean favourite){
        this.favourite = favourite;
    }

    /**
     * Setzt den Status ob es sich um einen Garantiefall handelt
     * @param guarantee boolean
     */
    public void setGuarantee(boolean guarantee){
        this.guarantee = guarantee;
    }

    /**
     * Setzt das Garantieende Datum
     * @param guaranteeEnd String
     */
    public void setGuaranteeEnd(String guaranteeEnd){
        this.guaranteeEnd = guaranteeEnd;
    }

    /**
     * Weißt dem Bon eine neue Liste mit Artikeln zu
     * @param articles ArrayList<C_Artikel>
     */
    public void setArticles(ArrayList<C_Artikel> articles){
        this.articles = articles;
    }

    /**
     * Weißt dem Bon einen neuen Gesamtpreis zu
     * @param totalPrice Gesamtpreis
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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
                "totalPrice: " + this.totalPrice + ", \n" +
                "Favourite: " + this.getFavourite() + ", \n" +
                "Guarantee: " +this.getGuarantee();

        if(this.articles != null){
            out += "\nArticles:";
            for(C_Artikel artikel : this.getArticles()){
                out += "\n\t" + artikel.getName() + " - " + artikel.getPrice() + " - " + artikel.getCategory();
            }
        } else {
            out += "\nNO ARTICLES FOUND!";
        }
        return out;
    }
}