package de.techfak.gse.maheidemann.modell;

/**
 * ExceptionKlasse für invalid Positionen.
 */
public class InvalidPositionException extends Exception {

    public InvalidPositionException(String invalidPositionException) {
        super(invalidPositionException);
    }
}
