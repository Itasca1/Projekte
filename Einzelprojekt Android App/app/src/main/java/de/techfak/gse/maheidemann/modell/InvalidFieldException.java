package de.techfak.gse.maheidemann.modell;

/**
 * Exceptionklasse für InvaliedField.
 */
public class InvalidFieldException extends Exception {

    public InvalidFieldException() {
        super("InvaliedFieldException");
    }

    public InvalidFieldException(String fehlermeldung) {
        super(fehlermeldung);
    }
}
