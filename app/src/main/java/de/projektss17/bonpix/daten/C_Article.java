package de.projektss17.bonpix.daten;

/**
 * Created by Domi on 20.04.2017.
 */

public class C_Article {

    private String name;
    private String price;

    public C_Article(String name, String price){
        this.name = name;
        this.price = price;
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
    public String getPrice(){
        return this.price;
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
    public void setPrice(String price){
        this.price = price;
    }
}
