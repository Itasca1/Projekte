package de.techfak.gse.maheidemann.modell;

/**
 * Exception Klasse für das ankreuzen verschiedener Farben.
 */
public class DifferentColorsException extends Exception {

    public DifferentColorsException(String invalidColorException) {
        super(invalidColorException);
    }
}
