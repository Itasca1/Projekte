package de.techfak.gse.maheidemann.modell;

/**
 * Exceptionklasse fpr InvalidBoardLayout.
 */
public class InvalidBoardLayoutException extends Exception {

    public InvalidBoardLayoutException() {
        super("InvalidBoardLayoutException");
    }

    public InvalidBoardLayoutException(String invalidBoardLayout) {
        super(invalidBoardLayout);
    }
}
