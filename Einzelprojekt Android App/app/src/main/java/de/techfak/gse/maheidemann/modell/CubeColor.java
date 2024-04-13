package de.techfak.gse.maheidemann.modell;

import android.graphics.Color;

/**
 * Klasse für Farbenwürfel.
 */
public class CubeColor {

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
     * Anzahl der Variationen.
     */
    private static final int NUMB = 5;

    /**
     * Erster Case.
     */
    private static final int ZERO = 0;

    /**
     * Zweiter Case.
     */
    private static  final int ONE = 1;

    /**
     * Dritter Case.
     */
    private static final int TWO = 2;

    /**
     * Vierter Case.
     */
    private static final int THREE = 3;

    /**
     * Fünfter Case.
     */
    private static final int FOUR = 4;

    /**
     * Farbe des Würfels.
     */
    private int cubeColor;

    /**
     * Erstellt einen Würfel mit gewünschter Farbe.
     */
    public CubeColor() {
        cubeColor = Color.WHITE;
    }

    /**
     * Setzt die Farbe des Würfels auf gewünschten Wert.
     * @param cubeColor
     */
    public void setCubeColor(int cubeColor) {
        this.cubeColor = cubeColor;
    }

    /**
     * Gibt die Farbe des Würfels aus.
     * @return cubeColor
     */
    public int getCubeColor() {
        return cubeColor;
    }

    /**
     * Erzeugt zufällig eine Würfelfarbe.
     */
    public void createCubeColor() {
        int color = (int) (Math.random() * NUMB);
        switch (color) {
            case (ZERO):
                cubeColor = COLORBLUE;
                break;
            case (ONE):
                cubeColor = COLORGREEN;
                break;
            case (TWO):
                cubeColor = COLORYELLOW;
                break;
            case (THREE):
                cubeColor = COLORRED;
                break;
            case (FOUR):
                cubeColor = COLORORANGE;
                break;
            default:
                cubeColor = Color.WHITE;
        }
    }
}
