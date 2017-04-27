package de.projektss17.bonpix.daten;

/**
 * Created by Domi on 20.04.2017.
 */

public class C_Artikel {

    private int id;
    private String name,
            preis;

    public C_Artikel(String name, String price){
        this(0, name, price);
    }

    public C_Artikel(int id, String name, String price){
        this.id = id;
        this.name = name;
        this.preis = price;
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
    public String getPreis(){
        return this.preis;
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
     * @param preis Neuer Preis des Artikels
     */
    public void setPreis(String preis){
        this.preis = preis;
    }
}
