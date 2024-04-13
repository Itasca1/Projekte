package de.techfak.gse.maheidemann.modell;

/**
 * Klasse für Zahlenwürfel.
 */
public class CubeNumb {

    /**
     * Mögliche Zahlen.
     */
    private final int variation = 5;
    /**
     * Zahl eines Würfels.
     */
    private int cubeNumb;

    public CubeNumb() {
        cubeNumb = 0;
    }

    /**
     * Gibt die aktuelle Würfelzahl aus.
     * @return cubeNumb
     */
    public int getCubeNumb() {
        return cubeNumb;
    }

    /**
     * Setzt Würfelzahl auf gewünschten Wert.
     * @param cubeNumb
     */
    public void setCubeNumb(int cubeNumb) {
        this.cubeNumb = cubeNumb;
    }

    /**
     * Erzeugt eine zufällige Würfelzahl.
     */
    public void createCubeNumb() {
        cubeNumb = (int) (Math.random() * variation);
    }
}
