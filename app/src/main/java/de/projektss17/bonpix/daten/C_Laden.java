package de.projektss17.bonpix.daten;

/**
 * Created by Domi on 27.04.2017.
 */

public class C_Laden {

    private int id;
    private String name;

    public C_Laden(int id, String name){
        this.id = id;
        this.name = name;
    }

    public C_Laden(String name){
        this(0, name);
    }

    /**
     * Gibt die Id des Ladens zurück
     * @return Ladenid int
     */
    public int getId(){
        return this.id;
    }

    /**
     * Gibt den Namen des Ladens zurück
     * @return Laden Name String
     */
    public String getName(){
        return this.name;
    }

    /**
     * Setzt eine neue Id für den Laden
     * @param id Neue Id des Ladens
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Setzt einen neuen Namen für den Laden
     * @param name Neuer Laden Name
     */
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nID: " + this.getId() +
                "\nNAME: " + this.getName();
    }
}
