package de.projektss17.bonpix.daten;

/**
 * Created by Domi on 20.04.2017.
 */

public class C_Artikel {

    private int id;
    private double price;
    private String name, category;

    public C_Artikel(String name, double price){
        this(0, name, price, null);
    }

    public C_Artikel(String name, double price, String category){
        this(0, name, price, category);
    }

    public C_Artikel(int id, String name, double price, String category){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    /**
     * Gibt die ID des Artikels wieder
     * @return Name des Artikels String
     */
    public int getId(){
        return this.id;
    }

    /**
     * Gibt den Namen des Artikels wieder
     * @return Name des Artikels String
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gibt den Preis des Artikels wieder
     * @return Preis des Artikels
     */
    public double getPrice(){
        return this.price;
}

    /**
     * Gibt die Kategorie des Artikels wieder
     * @return Kategorie des Artikels
     */
    public String getCategory(){
        return this.category;
    }

    /**
     * Setzt eine id
     * @param id Neue ID des Artikels
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Setzt den Namen des Artikels
     * @param name Neuer Name des Artikels
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Setzt den Preis des Artikels
     * @param price Neuer Preis des Artikels
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * Setzt die Kategorie des Artikels
     * @param category Neue Kategorie des Artikels
     */
    public void setCategory(String category){
        this.category = category;
    }

    @Override
    public String toString() {
        return "\nID: " + this.getId() +
                "\nNAME: " + this.getName() +
                "\nPRICE: " + this.getPrice() +
                "\nKATEGORIE: " + this.getCategory();
    }
}
