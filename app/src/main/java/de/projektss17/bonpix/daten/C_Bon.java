package de.projektss17.bonpix.daten;

import java.util.ArrayList;

/**
 * Created by Marcus on 15.04.2017.
 */

public class C_Bon {

    private int id;
    private String Bildpfad,
            Ladenname,
            Anschrift,
            SonstigeInfos,
            Datum,
            garantieEnde;
    private ArrayList<C_Artikel> artikel;
    private boolean favorite,
            garantie;

    public C_Bon(String Bildpfad, String Ladenname, String Anschrift, String SonstigeInfos,
                 String Datum, String garantieEnde, boolean favorite, boolean garantie, ArrayList<C_Artikel> artikel) {
        this(0, Bildpfad, Ladenname, Anschrift, SonstigeInfos, Datum, garantieEnde, favorite, garantie, artikel);
    }

    public C_Bon(int id, String Bildpfad, String Ladenname, String Anschrift, String SonstigeInfos,
                 String Datum, String garantieEnde, boolean favorite, boolean garantie) {
        this(0, Bildpfad, Ladenname, Anschrift, SonstigeInfos, Datum, garantieEnde, favorite, garantie, null);
    }

    public C_Bon(int id, String Bildpfad, String Ladenname, String Anschrift, String SonstigeInfos,
                 String Datum, String garantieEnde, boolean favorite, boolean garantie, ArrayList<C_Artikel> artikel){
        this.Bildpfad = Bildpfad;
        this.Ladenname = Ladenname;
        this.Anschrift = Anschrift;
        this.SonstigeInfos = SonstigeInfos;
        this.Datum = Datum;
        this.garantieEnde = garantieEnde;
        this.favorite = favorite;
        this.garantie = garantie;
        this.artikel = artikel;
    }

    public int getId(){
        return this.id;
    }

    public String getBildpfad() {
        return Bildpfad;
    }

    public String getLadenname() {
        return Ladenname;
    }

    public String getAnschrift() {
        return Anschrift;
    }

    public String getSonstigeInfos() {
        return SonstigeInfos;
    }

    public String getDatum() {
        return Datum;
    }

    public boolean getFavorite(){
        return this.favorite;
    }

    public boolean getGarantie(){
        return this.garantie;
    }

    public String getGarantieEnde(){
        return this.garantieEnde;
    }

    public ArrayList<C_Artikel> getArtikel(){
        return this.artikel;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setBildpfad(String bildpfad) {
        Bildpfad = bildpfad;
    }

    public void setLadenname(String ladenname) {
        Ladenname = ladenname;
    }

    public void setAnschrift(String anschrift) {
        Anschrift = anschrift;
    }

    public void setSonstigeInfos(String sonstigeInfos) {
        SonstigeInfos = sonstigeInfos;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    public void setFavorite(boolean favorite){
        this.favorite = favorite;
    }

    public void setGarantie(boolean garantie){
        this.garantie = garantie;
    }

    public void setGarantieEnde(String garantieEnde){
        this.garantieEnde = garantieEnde;
    }

    public void setArtikel(ArrayList<C_Artikel> artikel){
        this.artikel = artikel;
    }
}