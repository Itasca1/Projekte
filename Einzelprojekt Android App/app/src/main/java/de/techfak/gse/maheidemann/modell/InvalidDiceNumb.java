package de.techfak.gse.maheidemann.modell;

/**
 * Exception Klasse für Falsche Würfelzahl.
 */
public class InvalidDiceNumb extends Exception {

    public InvalidDiceNumb(String invalidDiceNumb) {
        super(invalidDiceNumb);
    }
}
