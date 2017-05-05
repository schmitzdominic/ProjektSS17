package de.projektss17.bonpix.daten;

/**
 * Created by Marcus on 15.04.2017.
 */

public class C_Bons {
    private String Bildpfad, Ladenname, Anschrift, SonstigeInfos, Datum;
    private boolean isFav;


    // Constructor
    public C_Bons(String Bildpfad, String Ladenname, String Anschrift, String SonstigeInfos, String Datum) {
        this.Bildpfad = Bildpfad;
        this.Ladenname = Ladenname;
        this.Anschrift = Anschrift;
        this.SonstigeInfos = SonstigeInfos;
        this.Datum = Datum;
    }

    public String getBildpfad() {
        return Bildpfad;
    }

    public void setBildpfad(String bildpfad) {
        Bildpfad = bildpfad;
    }

    public String getLadenname() {
        return Ladenname;
    }

    public void setLadenname(String ladenname) {
        Ladenname = ladenname;
    }

    public String getAnschrift() {
        return Anschrift;
    }

    public void setAnschrift(String anschrift) {
        Anschrift = anschrift;
    }

    public String getSonstigeInfos() {
        return SonstigeInfos;
    }

    public void setSonstigeInfos(String sonstigeInfos) {
        SonstigeInfos = sonstigeInfos;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    /**
     * Set as Favorited
     */
    public void setFav(){
        isFav = true;
    }

    /**
     * Set Favorited to false - Delete Favorited
     */
    public void deleteFav(){
        isFav = false;
    }

    /**
     * Get boolean if item is favorited (true or false)
     * @return
     */
    public boolean getFav(){
        return isFav;
    }
}