package de.projektss17.bonpix.daten;

import java.util.ArrayList;

/**
 * Created by Carnivorus on 19.05.2017.
 */

public class C_Statistik {

    String  ausgaben;   // Gesamtbetrag aller Bons in einem ausgewählten Zeitraum (Könnte theoretisch auch außerhalb der Klasse erfolgen)
    private ArrayList<C_Laden> laeden;  // Sammlung aller Läden in einem ausgewählten Zeitraum
    private ArrayList<C_Bon> bons;  // Sammlung aller Bons (inkl. Artikel) in einem ausgewählten Zeitraum


    public C_Statistik(String ausgaben, ArrayList<C_Laden> laeden, ArrayList<C_Bon> bons){

        this.ausgaben = ausgaben;
        this.laeden = laeden;
        this.bons = bons;
    }

    /**
     * Gibt den Ausgabebn aller in einem Zeitraum ausgewählten Bons zurück
     * @return rückgabe des Attributs ausgaben
     */
    public String getAusgaben() {
        return ausgaben;
    }

    /**
     * Setzt die Ausgaben aller ausgewählten Bons
     */
    public void setAusgaben(String ausgaben) {
        this.ausgaben = ausgaben;
    }

    /**
     * Gibt eine ArrayList aller in einem Zeitraum ausgewählten Läden zurück
     * @return rückgabe des Attributs laeden
     */
    public ArrayList<C_Laden> getLaeden() {
        return laeden;
    }

    /**
     * Setzt die Läden die in einem Zeitraum ausgewählt wurden
     */
    public void setLaeden(ArrayList<C_Laden> laeden) {
        this.laeden = laeden;
    }

    /**
     * Gibt eine ArrayList aller in einem Zeitraum ausgewählten Bons zurück
     * @return rückgabe des Attributs bons
     */
    public ArrayList<C_Bon> getBons() {
        return bons;
    }

    /**
     * Setzt die Bons die in einem Zeitraum ausgewählt wurden
     */
    public void setBons(ArrayList<C_Bon> products) {
        this.bons = products;
    }
}
