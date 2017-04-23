package de.projektss17.bonpix.auswerter;

/**
 * Created by Domi on 15.04.2017.
 */

public interface I_Auswerter {
    /**
     * Gibt alle Produkte als String Array zurück
     * @param txt Text der nach Produkten durchsucht werden soll
     * @return Array mit allen Produkten
     */
    String[] getProducts(String txt);

    /**
     * Gibt alle Preise als String Array zurück
     * @param txt Text der nach Preisen durchsucht werden soll
     * @return Array mit allen Preisen
     */
    String[] getPrices(String txt);
}
