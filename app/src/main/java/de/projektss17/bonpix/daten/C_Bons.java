package de.projektss17.bonpix.daten;

/**
 * Created by Marcus on 15.04.2017.
 */

public class C_Bons {
    private String bon_name;
    private String bon_id;

    public C_Bons(String name, String id) {
        this.bon_name = name;
        this.bon_id = id;
    }

    public String getName() {
        return bon_name;
    }

    public String getId() {
        return bon_id;
    }

    public void setName(String name){
        this.bon_name = name;
    }
}
