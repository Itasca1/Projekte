package de.techfak.gse.maheidemann.modell;

import android.graphics.Color;

/**
 * Stellt die Felder des Spielfeldes dar.
 */
public class Field {

    /**
     * Wert für Blau.
     */
    public static final int COLORBLUE = Color.argb(100, 0, 0, 255);

    /**
     * Wert für Grün.
     */
    public static final int COLORGREEN = Color.argb(100, 0, 255, 0);

    /**
     * Wert für Gelb.
     */
    public static final int COLORYELLOW = Color.argb(100, 255, 255, 0);

    /**
     * Wert für Rot.
     */
    public static final int COLORRED = Color.argb(100, 255, 0, 0);

    /**
     * Wert für Orange.
     */
    public static final int COLORORANGE = Color.argb(100, 255, 128, 0);

    /**
     * Buchstabe für Farbe des Feldes.
     */
    char colorField;
    /**
     * ID des Feldes.
     */
    int id;

    /**
     * Wert für die Reihenposition.
     */
    int rowPos;

    /**
     * Wert für die Spaltenposition.
     */
    int columnPos;

    /**
     * Boolean der festhält ob Feld angekreuzt ist oder nicht.
     */
    boolean ticked;

    /**
     * Hält fest ob das Feld diesen Zug angekreuzt wurde.
     */
    boolean tickedThisTurn;

    /**
     * Integerwert um Farben zu erzeugen.
     */
    int color;

    /**
     * Konstruktor für die Feld-Objekte.
     * @param rowPosition
     * @param columnPosition
     * @param colorChar
     * @param idNumb
     */
    public Field(int rowPosition, int columnPosition, char colorChar, int idNumb) {
        colorField = colorChar;
        rowPos = rowPosition;
        columnPos = columnPosition;
        ticked = isTicked(colorChar);
        color = color(colorChar);
        tickedThisTurn = false;
        id = createID(idNumb);
    }

    /**
     * Konstruktor für die markField-Methode.
     * @param rowPosition
     * @param columnPosition
     * @param colorChar
     * @param idNumb
     * @param turnTicked
     */
    public Field(int rowPosition, int columnPosition, char colorChar, int idNumb, boolean turnTicked) {
        colorField = colorChar;
        rowPos = rowPosition;
        columnPos = columnPosition;
        ticked = isTicked(colorChar);
        color = color(colorChar);
        tickedThisTurn = turnTicked;
        id = createID(idNumb);
    }


    /**
     * Erstellt eine ID Nummer für das Feld.
     * @param id
     * @return id
     */
    public int createID(int id) {
        return id;
    }
    /**
     * Gibt die ID eines Feldes zurück.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt id auf gewünschten Wert.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    public boolean isTickedThisTurn() {
        return tickedThisTurn;
    }

    /**
     * setzt tickedThisTurn auf gewünschten Wert.
     * @param tickedThisTurn
     */
    public void setTickedThisTurn(boolean tickedThisTurn) {
        this.tickedThisTurn = tickedThisTurn;
    }

    /**
     * Setzt ticked auf gewünschten Wert.
     * @param ticked
     */
    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

    /**
     * Gibt die Farbe des Feldes zurück.
     * @return color
     */
    public int getColor() {
        return color;
    }

    /**
     * Ausgabe ob es sich um ein angekreuztes Feld handelt.
     * @return ticked
     */
    public boolean isTicked() {
        return ticked;
    }

    /**
     * Prüft ob Feld angekreuzt ist.
     * @param colorChar
     * @return ob Feld angekreuzt  ist.
     */
    private boolean isTicked(char colorChar) {
        switch (colorChar) {
            case 'B':
            case 'G':
            case 'Y':
            case 'R':
            case 'O':
                return true;
            default:
                return false;
        }
    }

    /**
     * Prüft welche Farbe dem Feld zugewiesen werden muss.
     * @param colorChar
     * @return Farbe für das Feld.
     */
    private int color(char colorChar) {
        switch (colorChar) {
            case 'b':
            case 'B':
                return COLORBLUE;
            case 'g':
            case 'G':
                return COLORGREEN;
            case 'y':
            case 'Y':
                return COLORYELLOW;
            case 'r':
            case 'R':
                return COLORRED;
            case 'o':
            case 'O':
                return COLORORANGE;
            default:
                return Color.WHITE;
        }
    }
}
