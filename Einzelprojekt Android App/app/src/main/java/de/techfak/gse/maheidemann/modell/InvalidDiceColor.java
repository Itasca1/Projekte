package de.techfak.gse.maheidemann.modell;

/**
 * Exceptionklasse für falsche Würfelfarbe.
 */
public class InvalidDiceColor extends Exception {

    public InvalidDiceColor(String invalidDiceColor) {
        super(invalidDiceColor);
    }
}
