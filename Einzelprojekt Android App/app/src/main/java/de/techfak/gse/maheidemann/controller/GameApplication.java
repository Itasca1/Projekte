package de.techfak.gse.maheidemann.controller;

import android.app.Application;
import android.widget.TableLayout;

import de.techfak.gse.maheidemann.modell.Game;

/**
 * Speicherklasse um Informationen auszutauschen.
 */
public class GameApplication extends Application {

    /**
     * Speichert ein String[], was für die Spielfeldeingabe steht.
     */
    private String[] spielfeldeingabe;

    /**
     * Speichert ein TableLayout.
     */
    private TableLayout tableLayout;

    /**
     * Speichert ein Spiel.
     */
    private Game game;

    /**
     * Gibt ein Game aus.
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setzt Game auf gewünschten Wert.
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gibt ein String-Array aus.
     * @return spielfeldeingabe.
     */
    public String[] getSpielfeldeingabe() {
        return spielfeldeingabe;
    }

    /**
     * Setzt das String-Array auf gewünschtes Array.
     * @param spielfeldeingabe
     */
    public void setSpielfeldeingabe(String[] spielfeldeingabe) {
        this.spielfeldeingabe = spielfeldeingabe;
    }

    /**
     * Gibt ein TableLayout aus.
     * @return tablelayout
     */
    public TableLayout getTableLayout() {
        return tableLayout;
    }

    /**
     * Setzt ein TableLayout auf bestimmtes TableLayout.
     * @param tableLayout
     */
    public void setTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }
}
