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

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nID: " + this.getId() +
                "\nNAME: " + this.getName();
    }
}
