package de.techfak.gse.maheidemann.modell;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Erstellt ein neues Spielfeld und managed das markieren von Feldern, sowei das starten neuer Runden.
 */
public class Game {
    /**
     * Um das Modell beobachtbar zu machen.
     */
    private PropertyChangeSupport support;
    /**
     * 2D-Array zur Darstellung des Spielfeldes.
     */
    private Field[][] gameboard;
    /**
     * Zähler für die Spielrunden.
     */
    private int roundCounter;
    /**
     * Zählt wie oft gewürfelt wurde.
     */
    private int rollDiceCounter;
    /**
     * Erster Farbwürfel.
     */
    private CubeColor cubeColorOne;
    /**
     * Zweiter Farbwürfel.
     */
    private CubeColor cubeColorTwo;
    /**
     * Dritter Farbwürfel.
     */
    private CubeColor cubeColorThree;
    /**
     * Erster Zahlenwürfel.
     */
    private CubeNumb cubeNumbOne;
    /**
     * Zweiter Zahlenwürfel.
     */
    private CubeNumb cubeNumbTwo;
    /**
     * Dritter Zahlenwürfel.
     */
    private CubeNumb cubeNumbThree;
    /**
     * Konstruktor für ein Spiel.
     * @param gameboardInput
     */
    public Game(String[] gameboardInput) {
        gameboard = createGameboard(gameboardInput);
        roundCounter = 1;
        rollDiceCounter = 0;
        this.support = new PropertyChangeSupport(this);
        cubeColorOne = new CubeColor();
        cubeColorTwo = new CubeColor();
        cubeColorThree = new CubeColor();
        cubeNumbOne = new CubeNumb();
        cubeNumbTwo = new CubeNumb();
        cubeNumbThree = new CubeNumb();
    }

    /**
     * Erstellt ein Spielfeld aus einem String-Array.
     * @param gameboardInput
     * @return gameboard
     */
    public Field[][] createGameboard(String[] gameboardInput) {
        Field[][] gameboard = new Field[gameboardInput.length][gameboardInput[0].length()];
        int idField = 0;
        for (int i = 0; i < gameboardInput.length; i++) {
            char[] line = gameboardInput[i].toCharArray();
            for (int j = 0; j < line.length; j++) {
                Field field = new Field(i, j, line[j], idField);
                gameboard[i][j] = field;
                idField++;
            }
        }
        return gameboard;
    }

    /**
     * Setzt den wurfzählerzurück.
     */
    public void resetDiceThrow() {
        this.rollDiceCounter = 0;
    }

    /**
     * Erhöht den Rundenzähler.
     */
    public void incRound() {
        int roundCounterInc = roundCounter + 1;
        support.firePropertyChange("roundCounter", roundCounter, roundCounterInc);
        this.roundCounter = roundCounterInc;
    }

    /**
     * Erstellt eine neue Runde.
     */
    public void createNewRound()  {
        Field[][] newGameboard = new Field[gameboard.length][gameboard[0].length];
        for (int i = 0; i < newGameboard.length; i++) {
            for (int j = 0; j < newGameboard[i].length; j++) {
                Field newField = new Field(i,  j, gameboard[i][j].colorField, gameboard[i][j].getId());
                if (gameboard[i][j].isTickedThisTurn()) {
                    newField.setTickedThisTurn(true);
                }
                if (gameboard[i][j].isTicked()) {
                    newField.setTickedThisTurn(true);
                }
                newGameboard[i][j] = newField;
                if (newGameboard[i][j].isTickedThisTurn()) {
                    newGameboard[i][j].setTicked(true);
                    newGameboard[i][j].setTickedThisTurn(false);
                }
            }
        }
        support.firePropertyChange("gameboard", gameboard, newGameboard);
        this.gameboard = newGameboard;
    }

    /**
     * MArkiert ein Feld. Ist das Feld bereits markiert, so wird diese Markierung entfernt.
     * @param markPos
     */
    public void tickField(int markPos) {

        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[i].length; j++) {
                if (gameboard[i][j].getId() == markPos) {
                    Field newField = new Field(i, j, gameboard[i][j].colorField,
                            gameboard[i][j].getId(), gameboard[i][j].tickedThisTurn);
                    if (!newField.isTickedThisTurn()) {
                        newField.setTickedThisTurn(true);
                    } else {
                        newField.setTickedThisTurn(false);
                    }
                    support.firePropertyChange("markedField", gameboard[i][j], newField);
                    gameboard[i][j] = newField;
                }
            }
        }
    }

    /**
     * Erzeugt einen neuen Würfelwurf.
     */
    public void rollDices() {
        if (rollDiceCounter == 0) {
            CubeColor newCubeColorOne =  new CubeColor();
            newCubeColorOne.createCubeColor();
            this.support.firePropertyChange("newColorOne", cubeColorOne, newCubeColorOne);
            cubeColorOne = newCubeColorOne;

            CubeColor newCubeColorTwo = new CubeColor();
            newCubeColorTwo.createCubeColor();
            this.support.firePropertyChange("newColorTwo", cubeColorTwo, newCubeColorTwo);
            cubeColorTwo = newCubeColorTwo;

            CubeColor newCubeColorThree = new CubeColor();
            newCubeColorThree.createCubeColor();
            this.support.firePropertyChange("newColorThree", cubeColorThree, newCubeColorThree);
            cubeColorThree = newCubeColorThree;

            CubeNumb newCubeNumbOne = new CubeNumb();
            newCubeNumbOne.createCubeNumb();
            this.support.firePropertyChange("newNumbOne", cubeNumbOne, newCubeNumbOne);
            cubeNumbOne = newCubeNumbOne;

            CubeNumb newCubeNumbTwo = new CubeNumb();
            newCubeNumbTwo.createCubeNumb();
            this.support.firePropertyChange("newNumbTwo", cubeNumbTwo, newCubeNumbTwo);
            cubeNumbTwo = newCubeNumbTwo;

            CubeNumb newCubeNumbThree = new CubeNumb();
            newCubeNumbThree.createCubeNumb();
            this.support.firePropertyChange("newNumbThree", cubeNumbThree, newCubeNumbThree);
            cubeNumbThree = newCubeNumbThree;

            rollDiceCounter++;
        }
    }

    /**
     * Passt die Würfelnummer so an das es passt.
     */
    public void adjustDiceNumb() {
        cubeNumbOne.setCubeNumb(cubeNumbOne.getCubeNumb() + 1);
        cubeNumbTwo.setCubeNumb(cubeNumbTwo.getCubeNumb() + 1);
        cubeNumbThree.setCubeNumb(cubeNumbThree.getCubeNumb() + 1);
    }

    /**
     * Setzt die Würfel beim start einer neuen Runde zurück.
     */
    public void resetDices() {
        CubeColor startCubeColorOne = new CubeColor();
        this.support.firePropertyChange("resetDiceOne", cubeColorOne, startCubeColorOne);
        cubeColorOne = startCubeColorOne;

        CubeColor startCubeColorTwo = new CubeColor();
        this.support.firePropertyChange("resetDiceTwo", cubeColorTwo, startCubeColorTwo);
        cubeColorTwo = startCubeColorTwo;

        CubeColor startCubeColorThree = new CubeColor();
        this.support.firePropertyChange("resetDiceThree", cubeColorThree, startCubeColorThree);
        cubeColorThree = startCubeColorThree;

    }

    /**
     * Gibt aus wie oft gewürfelt wurde.
     * @return rollDiceCounter
     */
    public int getRollDiceCounter() {
        return rollDiceCounter;
    }

    /**
     * Holt den Wert von cubeNumbOne.
     * @return cubeNumbOne
     */
    public CubeNumb getCubeNumbOne() {
        return cubeNumbOne;
    }

    /**
     * Holt den Wert von cubeNumbTwo.
     * @return cubeNumbTwo
     */
    public CubeNumb getCubeNumbTwo() {
        return cubeNumbTwo;
    }

    /**
     * Holt den Wert von cubeNumbThree.
     * @return cubeNumbThree
     */
    public CubeNumb getCubeNumbThree() {
        return cubeNumbThree;
    }

    /**
     * Setzt cubeNumbOne auf gewünschten Wert.
     * @param cubeNumbOne
     */
    public void setCubeNumbOne(CubeNumb cubeNumbOne) {
        this.cubeNumbOne = cubeNumbOne;
    }

    /**
     * Setzt cubeNumbTwo auf gewüschten Wert.
     * @param cubeNumbTwo
     */
    public void setCubeNumbTwo(CubeNumb cubeNumbTwo) {
        this.cubeNumbTwo = cubeNumbTwo;
    }

    /**
     * Setzt cubeNumbThree auf gewünschtenWert.
     * @param cubeNumbThree
     */
    public void setCubeNumbThree(CubeNumb cubeNumbThree) {
        this.cubeNumbThree = cubeNumbThree;
    }

    /**
     * Setzt cubeColorOne auf gewünschten Wert.
     * @param cubeColorOne
     */
    public void setCubeColorOne(CubeColor cubeColorOne) {
        this.cubeColorOne = cubeColorOne;
    }

    /**
     * Holt den Wert von cubeColorOne.
     * @return cubeColorOne
     */
    public CubeColor getCubeColorOne() {
        return cubeColorOne;
    }

    /**
     * Setzt cubeColorTwo auf gewünschten Wert.
     * @param cubeColorTwo
     */
    public void setCubeColorTwo(CubeColor cubeColorTwo) {
        this.cubeColorTwo = cubeColorTwo;
    }

    /**
     * Holt den Wert von cubeColorTwo.
     * @return cubeColorTwo
     */
    public CubeColor getCubeColorTwo() {
        return cubeColorTwo;
    }

    /**
     * Setzt cubeColorThree auf gewünschten Wert.
     * @param cubeColorThree
     */
    public void setCubeColorThree(CubeColor cubeColorThree) {
        this.cubeColorThree = cubeColorThree;
    }

    /**
     * Holt den Wert von cubeColorThree.
     * @return cubeColorThree
     */
    public CubeColor getCubeColorThree() {
        return cubeColorThree;
    }

    /**
     * Fügt einen Listener hinzu.
     * @param listener
     */
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Entfernt einen Listener.
     * @param listener
     */
    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Gibt ein Spielfeld zurück.
     * @return gameboard
     */
    public Field[][] getGameboard() {
        return gameboard;
    }

    /**
     * Setzt Spielfeld auf gewünschte Form.
     * @param gameboard
     */
    public void setGameboard(Field[][] gameboard) {
        this.gameboard = gameboard;
    }

    /**
     * Gibt die aktuelle Runde zurück.
     * @return roundCounter
     */
    public int getRoundCounter() {
        return roundCounter;
    }
}
