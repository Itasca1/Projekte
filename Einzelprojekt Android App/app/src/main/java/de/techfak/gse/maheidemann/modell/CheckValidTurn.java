package de.techfak.gse.maheidemann.modell;

import java.util.ArrayList;

/**
 * Klasse um einen Zug bezüglich seiner angekreuzten Felder zu überprüfen.
 */
public class CheckValidTurn {

    /**
     * Int für die Spalte H.
     */
    private final int columnH = 7;

    /**
     * Int für die Umgebung eines Feldes.
     */
    private final int neighbour = 1;

    /**
     * Überprüft ob ein Zug valide ist.
     * @param game
     */
    public void checkValidTurn(Game game) throws  RelatedFieldException,
            InvalidPositionException, InvalidDiceColor, InvalidDiceNumb {
        Field[][] gameboard = game.getGameboard();
        ArrayList<Field> markedFields = new ArrayList<>();
        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[i].length; j++) {
                if (gameboard[i][j].isTickedThisTurn()) {
                    markedFields.add(gameboard[i][j]);
                }
            }
        }
        positionCheck(markedFields, gameboard);
        differentDiceColor(markedFields, game);
        diceNumb(markedFields, game);
        ArrayList<Field> availableFields;
        availableFields = (ArrayList<Field>) markedFields.clone();
        if (!markedFields.isEmpty()) {
            if (markedFields.size() != relatedField(availableFields, availableFields.get(0), game)) {
                throw new RelatedFieldException("RelatedFieldException");
            }
        }
    }

    /**
     * Überprüft angekreuzte Felder auf gültige Position.
     * @param markedFields
     * @param gameboard
     * @throws InvalidPositionException
     */
    public void positionCheck(ArrayList<Field> markedFields, Field[][] gameboard) throws InvalidPositionException {
        Boolean hasValidPosition = false;
        ArrayList<Field> tickedFields = new ArrayList<>();
        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[i].length; j++) {
                if (gameboard[i][j].isTicked()) {
                    tickedFields.add(gameboard[i][j]);
                }
            }
        }
        if (markedFields.isEmpty()) {
            hasValidPosition = true;
        }
        for (int i = 0; i < markedFields.size(); i++) {
            if (markedFields.get(i).columnPos == columnH) {
                hasValidPosition = true;
                break;
            }
            if (!hasValidPosition && tickedFields.size() != 0) {
                for (int j = 0; j < tickedFields.size(); j++) {
                    if (markedFields.get(i).rowPos + neighbour == tickedFields.get(j).rowPos
                            && markedFields.get(i).columnPos == tickedFields.get(j).columnPos) {
                        hasValidPosition = true;
                        break;
                    } else if (markedFields.get(i).rowPos - neighbour == tickedFields.get(j).rowPos
                            && markedFields.get(i).columnPos == tickedFields.get(j).columnPos) {
                        hasValidPosition = true;
                        break;
                    } else if (markedFields.get(i).rowPos == tickedFields.get(j).rowPos
                            && markedFields.get(i).columnPos + neighbour == tickedFields.get(j).columnPos) {
                        hasValidPosition = true;
                        break;
                    } else if (markedFields.get(i).rowPos == tickedFields.get(j).rowPos
                            && markedFields.get(i).columnPos - neighbour == tickedFields.get(j).columnPos) {
                        hasValidPosition = true;
                        break;
                    }
                }
            }

        }
        if (!hasValidPosition) {
            throw new InvalidPositionException("InvalidPositionexception");
        }
    }

    /**
     * Überprüft die 4er Nachbarschaft.
     * @param markedFields
     * @param field
     * @param game
     * @return
     */
    public int  relatedField(ArrayList<Field> markedFields, Field field, Game game) {
        markedFields.remove(field);
        ArrayList<Field> neighbours = new ArrayList<>();
        for (int i = 0; i < game.getGameboard().length; i++) {
            for (int j = 0; j < game.getGameboard()[i].length; j++) {
                if (field.rowPos + 1 == game.getGameboard()[i][j].rowPos
                && field.columnPos == game.getGameboard()[i][j].columnPos) {
                    neighbours.add(game.getGameboard()[i][j]);
                }
                if (field.rowPos - 1 == game.getGameboard()[i][j].rowPos
                        && field.columnPos == game.getGameboard()[i][j].columnPos) {
                    neighbours.add(game.getGameboard()[i][j]);
                }
                if (field.rowPos == game.getGameboard()[i][j].rowPos
                        && field.columnPos + 1 == game.getGameboard()[i][j].columnPos) {
                    neighbours.add(game.getGameboard()[i][j]);
                }
                if (field.rowPos == game.getGameboard()[i][j].rowPos
                        && field.columnPos - 1 == game.getGameboard()[i][j].columnPos) {
                    neighbours.add(game.getGameboard()[i][j]);
                }
            }
        }

        int neighbourCount = 0;
        for (Field currentField : neighbours) {
            if (markedFields.contains(currentField)) {
                neighbourCount += relatedField(markedFields, currentField, game);
            }
        }
        return neighbourCount + neighbour;
    }

    /**
     * Überprüft ob die neu angekreuzten Felder die selbe Farbe haben.
     * @param game
     * @throws DifferentColorsException
     */
    public void differentColors(Game game) throws DifferentColorsException {
        Field[][] gameboard = game.getGameboard();
        ArrayList<Field> markedFields = new ArrayList<>();
        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[i].length; j++) {
                if (gameboard[i][j].isTickedThisTurn()) {
                    markedFields.add(gameboard[i][j]);
                }
            }
        }
        for (int i = 0; i < markedFields.size(); i++) {
            if (markedFields.get(0).getColor() != markedFields.get(i).getColor()) {
                throw new DifferentColorsException("DifferentColorsException");
            }
        }
    }

    /**
     * Überprüft ob die angekreuzten Felder einer Würfelfarbe entsprechen.
     * @param markedFields
     * @param game
     * @throws InvalidDiceColor
     */
    public void differentDiceColor(ArrayList<Field> markedFields, Game game) throws InvalidDiceColor {
        for (int i = 0; i < markedFields.size(); i++) {
            if (markedFields.get(i).getColor() != game.getCubeColorOne().getCubeColor()
                    && markedFields.get(i).getColor() != game.getCubeColorTwo().getCubeColor()
                    && markedFields.get(i).getColor() != game.getCubeColorThree().getCubeColor()) {
                throw new  InvalidDiceColor("InvalidDiceColor");
            }
        }
    }

    /**
     * Überprüft ob die angekreuzten Felder einer Würfelfarbe entsprechen.
     * @param markedFields
     * @param game
     * @throws InvalidDiceNumb
     */
    public void diceNumb(ArrayList<Field> markedFields, Game game) throws InvalidDiceNumb {
        if (markedFields.size() != 0) {
            if (markedFields.size() != game.getCubeNumbOne().getCubeNumb()
                    && markedFields.size() != game.getCubeNumbTwo().getCubeNumb()
                    && markedFields.size() != game.getCubeNumbThree().getCubeNumb()) {
                throw new InvalidDiceNumb("InvalidDiceNumb");
            }
        }
    }
}
