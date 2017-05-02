package de.projektss17.bonpix.daten;

/**
 * Created by Domi on 20.04.2017.
 */

public class C_Artikel {

    private int id;
    private double preis;
    private String name, kategorie;

    public C_Artikel(String name, double preis){
        this(0, name, preis, null);
    }

    public C_Artikel(String name, double preis, String kategorie){
        this(0, name, preis, kategorie);
    }

    public C_Artikel(int id, String name, double preis, String kategorie){
        this.id = id;
        this.name = name;
        this.preis = preis;
        this.kategorie = kategorie;
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
    public double getPreis(){
        return this.preis;
    }

    /**
     * Gibt die Kategorie des Artikels wieder
     * @return Kategorie des Artikels
     */
    public String getKategorie(){
        return this.kategorie;
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
    public void setPreis(double preis){
        this.preis = preis;
    }

    /**
     * Setzt die Kategorie des Artikels
     * @param kategorie Neue Kategorie des Artikels
     */
    public void setKategorie(String kategorie){
        this.kategorie = kategorie;
    }

    @Override
    public String toString() {
        return "\nID: " + this.getId() +
                "\nNAME: " + this.getName() +
                "\nPRICE: " + this.getPreis() +
                "\nKATEGORIE: " + this.getKategorie();
    }
}
