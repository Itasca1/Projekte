package de.techfak.gse.maheidemann.modell;

/**
 * Klasse um Spielfeldeingaben zu behandeln.
 */
public class CheckValidField {

    /**
     * Wert für die maximal erlaubte Zeilenanzahl eines Spielfeldes.
     */
    private final int maximumRows = 7;
    /**
     * Wert für die maximal erlaubte Spaltenanzahl eines Spielfeldes.
     */
    private final int maximumColumns = 15;

    /**
     * Checked ob Spielfeldeingabe valide ist, wirft ansonsten eine Exception.
     * @param field
     * @throws InvalidFieldException
     * @throws InvalidBoardLayoutException
     */
    public  void checkValidField(String[] field) throws InvalidFieldException, InvalidBoardLayoutException {
        for (int i = 0; i < field.length; i++) {
            if (field.length != maximumRows || field[i].length() != maximumColumns) {
                throw new InvalidBoardLayoutException("InvalidBoardLayout");
            }
            boolean valid = true;
            char[] line = field[i].toCharArray();
            for (char c : line) {
                valid = (c == 'b'
                        || c == 'B'
                        || c == 'g'
                        || c == 'G'
                        || c == 'o'
                        || c == 'O'
                        || c == 'r'
                        || c == 'R'
                        || c == 'y'
                        || c == 'Y'
                        || c == '\n');
                if (!valid) {
                    throw new InvalidFieldException("Invalied Field");
                }
            }
        }
    }
}
