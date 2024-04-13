package de.techfak.gse.maheidemann.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import de.techfak.gse.maheidemann.modell.CheckValidTurn;
import de.techfak.gse.maheidemann.modell.CubeColor;
import de.techfak.gse.maheidemann.modell.CubeNumb;
import de.techfak.gse.maheidemann.modell.DifferentColorsException;
import de.techfak.gse.maheidemann.modell.Field;
import de.techfak.gse.maheidemann.modell.Game;
import de.techfak.gse.maheidemann.R;
import de.techfak.gse.maheidemann.modell.InvalidDiceColor;
import de.techfak.gse.maheidemann.modell.InvalidDiceNumb;
import de.techfak.gse.maheidemann.modell.InvalidPositionException;
import de.techfak.gse.maheidemann.modell.RelatedFieldException;

/**
 * Zweite  Activity, hier wird das Spielfeld angezeigt.
 */
public class GameboardActivity extends AppCompatActivity  implements PropertyChangeListener {


    /**
     * Um Höhe und Breite anzupassen.
     */
    private final double displayAdjust = 0.7;

    /**
     * Array für die visuelle Darstellung der Zahlen.
     */
    private int[] cubeNumbs = {R.drawable.dieface1,
            R.drawable.dieface2, R.drawable.dieface3, R.drawable.dieface4, R.drawable.dieface5};
    /**
     * NeutralButton.
     */
    private String neutralButton = "OK";

    /**
     * GameApplication um auf Daten zuzugreifen.
     */
    private GameApplication gameApplication;

    /**
     * String zur Darstellung von fest markierten Feldern.
     */
    private String oldMarked = "X";

    /**
     * String zur Darstellung von momentan markierten Feldern.
     */
    private String newMarked = "(X)";

    /**
     * Margin zwischen den Buttons.
     */
     private final int margin = 1;

    /**
     * Tablelayout für das Spielfeld.
     */
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameboard);
        tableLayout = new TableLayout(this);
        gameApplication = (GameApplication) getApplication();
        gameApplication.getGame().addListener(this);

        LinearLayout linearLayout = findViewById(R.id.topLinearLayout);
        tableLayout = createGameboard(gameApplication.getGame());
        gameApplication.setTableLayout(tableLayout);
        linearLayout.addView(gameApplication.getTableLayout());

        TextView roundCounter = findViewById(R.id.roundCounter);
        roundCounter.setText("Sie befinden sich in der "
                + gameApplication.getGame().getRoundCounter()
                + ". Runde.");

        TextView diceResult = findViewById(R.id.diceResult);
        diceResult.setText("Würfelergebnis:");

        Button rollDice = findViewById(R.id.rollDice);
        rollDice.setPadding(margin, margin, margin, margin);
        rollDice.setText("Würfeln");
        rollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameApplication.getGame().rollDices();
                gameApplication.getGame().adjustDiceNumb();
            }
        });

        CheckValidTurn checkValidTurn = new CheckValidTurn();
        Button endTurn = findViewById(R.id.endTurn);
        endTurn.setPadding(margin, margin, margin, margin);
        endTurn.setText("Zug beenden");
        endTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameApplication.getGame().getRollDiceCounter() != 0) {
                    try {
                        checkValidTurn.differentColors(gameApplication.getGame());
                        checkValidTurn.checkValidTurn(gameApplication.getGame());
                        gameApplication.getGame().resetDiceThrow();
                        gameApplication.getGame().resetDices();
                        gameApplication.getGame().incRound();
                        gameApplication.getGame().createNewRound();
                    } catch (DifferentColorsException differentColorsException) {
                        Toast differentColor = Toast.makeText(getApplicationContext(),
                                "Die Farbe der angekreuzten "
                                        + "Felder ist verschieden, bitte erneut ankreuzen.",
                                Toast.LENGTH_SHORT);
                        differentColor.show();
                    } catch (RelatedFieldException relatedFieldException) {
                        Toast relatedField = Toast.makeText(getApplicationContext(),
                                "Die angekreuzten Felder stehen in "
                                        + "keiner 4er-Nachbarschaft, bitte erneut ankreuzen.",
                                Toast.LENGTH_SHORT);
                        relatedField.show();
                    } catch (InvalidPositionException invalidPositionException) {
                        Toast invalidPosition = Toast.makeText(getApplicationContext(),
                                "Mindestens eins der angekreuzten Felder"
                                        + " muss in Spalte H liegen"
                                        + "oder an ein altes angekreuztes Feld angrenzen.",
                                Toast.LENGTH_SHORT);
                        invalidPosition.show();
                    } catch (InvalidDiceColor invalidDiceColor) {
                        Toast diceColor = Toast.makeText(getApplicationContext(),
                                "Die angekreuzten Felder entsprechen nicht den Würfelerfarben.",
                                Toast.LENGTH_SHORT);
                        diceColor.show();
                    } catch (InvalidDiceNumb invalidDiceNumb) {
                        Toast diceNumb = Toast.makeText(getApplicationContext(),
                                "Die angekreuzten Felder entsprechen nicht den Würfelzahlen.",
                                Toast.LENGTH_SHORT);
                        diceNumb.show();
                    }
                } else {
                    Toast rollDice = Toast.makeText(getApplicationContext(),
                            "Sie müssen einmal Würfeln, bevor Sie die Runde beenden können.",
                            Toast.LENGTH_SHORT);
                    rollDice.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameboardActivity.this);
        builder = builder.setTitle("Möchten Sie das Spiel verlassen?");
        builder = builder.setMessage(
                "Wählen Sie Ja zum verlassen und Nein, wenn Sie weiterspielen möchten");
        builder = builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder = builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     * Erstellt ein Button.
     * @param field
     * @return Button
     */
    public Button createButton(Field field) {
        Button button = new Button(this);
        if (field.isTicked()) {
            button.setText(oldMarked);
        }
        button.setBackgroundColor(field.getColor());
        button.setId(field.getId());
        return button;
    }

    /**
     * Nimmt eine Spielfeldeingabe und erstellt daraus ein TableLayout, um das Spielfeld anzuzeigen.
     * @param game
     * @return tableLayout
     */
    public TableLayout createGameboard(Game game) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels / gameApplication.getGame().getGameboard().length;
        int width = displayMetrics.widthPixels / gameApplication.getGame().getGameboard()[0].length;
        TableLayout.LayoutParams tableLayoutParams =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams tableRowParams =
                new TableRow.LayoutParams((int) (width * displayAdjust), (int) (height * displayAdjust));
        tableRowParams.setMargins(margin, margin, margin, margin);
        tableRowParams.weight = margin;
        int counter = 0;
        for (int i = 0; i < game.getGameboard().length; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableLayoutParams);
            for (int j = 0; j < game.getGameboard()[i].length; j++) {
                Button button = createButton(game.getGameboard()[i][j]);
                button.setPadding(margin, margin, margin, margin);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      game.tickField(button.getId());
                    }
                });
                button.setLayoutParams(tableRowParams);
                tableRow.addView(button, tableRowParams);
            }
            tableLayout.addView(tableRow);
        }
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);
        return tableLayout;
    }

    /**
     * Erstellt ein neues Spielfeld, für die neue Runde.
     * @param game
     * @return tableLayout
     */
    public TableLayout createNewRound(Game game) {
        for (int i = 0; i < game.getGameboard().length; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < game.getGameboard()[i].length; j++) {
               if (game.getGameboard()[i][j].isTicked()) {
                   Button button = (Button) tableRow.getChildAt(j);
                   button.setText(oldMarked);
               }
            }
        }
        return tableLayout;
    }

    /**
     * Markiert den passenden Button.
     * @param field
     */
    public void tickButton(Field field) {
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
               if (tableRow.getChildAt(j).getId() == field.getId()) {
                   Button button = (Button) tableRow.getChildAt(j);
                   String marked = (String) button.getText();
                   if (!marked.equals(oldMarked) && !marked.equals(newMarked)) {
                       button.setText(newMarked);
                   }
                   if (marked.equals(newMarked)) {
                       button.setText("");
                   }
               }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String event = propertyChangeEvent.getPropertyName();
        switch (event) {
            case"roundCounter":
                TextView roundCounter = findViewById(R.id.roundCounter);
                roundCounter.setText((String) "Sie befinden sich "
                        + "in der "
                        + propertyChangeEvent.getNewValue()
                        + ". "
                        + "Runde.");
                break;
            case"gameboard":
                gameApplication.getGame().setGameboard((Field[][]) propertyChangeEvent.getNewValue());
                tableLayout = createNewRound(gameApplication.getGame());
                gameApplication.setTableLayout(tableLayout);
                break;
            case"markedField":
                tickButton((Field) propertyChangeEvent.getNewValue());
                break;
            case"newColorOne":
                ImageView diceColorOne = findViewById(R.id.diceColorOne);
                gameApplication.getGame().setCubeColorOne((CubeColor) propertyChangeEvent.getNewValue());
                CubeColor newCubeColorOne = gameApplication.getGame().getCubeColorOne();
                diceColorOne.setColorFilter(newCubeColorOne.getCubeColor());
                break;
            case"newColorTwo":
                ImageView diceColorTwo = findViewById(R.id.diceColorTwo);
                gameApplication.getGame().setCubeColorTwo((CubeColor) propertyChangeEvent.getNewValue());
                CubeColor newCubeColorTwo = gameApplication.getGame().getCubeColorTwo();
                diceColorTwo.setColorFilter(newCubeColorTwo.getCubeColor());
                break;
            case"newColorThree":
                ImageView diceColorThree = findViewById(R.id.diceColorThree);
                gameApplication.getGame().setCubeColorThree((CubeColor) propertyChangeEvent.getNewValue());
                CubeColor newCubeColorThree = gameApplication.getGame().getCubeColorThree();
                diceColorThree.setColorFilter(newCubeColorThree.getCubeColor());
                break;
            case"newNumbOne":
                ImageView diceNumbOne = findViewById(R.id.diceNumbOne);
                gameApplication.getGame().setCubeNumbOne((CubeNumb) propertyChangeEvent.getNewValue());
                CubeNumb newCubeNumbOne = gameApplication.getGame().getCubeNumbOne();
                diceNumbOne.setImageResource(cubeNumbs[newCubeNumbOne.getCubeNumb()]);
                diceNumbOne.setColorFilter(0);
                break;
            case"newNumbTwo":
                ImageView diceNumbTwo = findViewById(R.id.diceNumbTwo);
                gameApplication.getGame().setCubeNumbTwo((CubeNumb) propertyChangeEvent.getNewValue());
                CubeNumb newCubeNumbTwo = gameApplication.getGame().getCubeNumbTwo();
                diceNumbTwo.setImageResource(cubeNumbs[newCubeNumbTwo.getCubeNumb()]);
                diceNumbTwo.setColorFilter(0);
                break;
            case"newNumbThree":
                ImageView diceNumbThree = findViewById(R.id.diceNumbThree);
                gameApplication.getGame().setCubeNumbThree((CubeNumb) propertyChangeEvent.getNewValue());
                CubeNumb newCubeNumbThree = gameApplication.getGame().getCubeNumbThree();
                diceNumbThree.setImageResource(cubeNumbs[newCubeNumbThree.getCubeNumb()]);
                diceNumbThree.setColorFilter(0);
                break;
            case"resetDiceOne":
                ImageView colorOne = findViewById(R.id.diceColorOne);
                ImageView numbOne = findViewById(R.id.diceNumbOne);
                gameApplication.getGame().setCubeColorOne((CubeColor) propertyChangeEvent.getNewValue());
                CubeColor newColorOne = gameApplication.getGame().getCubeColorOne();
                colorOne.setColorFilter(newColorOne.getCubeColor());
                numbOne.setColorFilter(newColorOne.getCubeColor());
                break;
            case"resetDiceTwo":
                ImageView colorTwo = findViewById(R.id.diceColorTwo);
                ImageView numbTwo = findViewById(R.id.diceNumbTwo);
                gameApplication.getGame().setCubeColorTwo((CubeColor) propertyChangeEvent.getNewValue());
                CubeColor newColorTwo = gameApplication.getGame().getCubeColorTwo();
                colorTwo.setColorFilter(newColorTwo.getCubeColor());
                numbTwo.setColorFilter(newColorTwo.getCubeColor());
                break;
            case"resetDiceThree":
                ImageView colorThree = findViewById(R.id.diceColorThree);
                ImageView numbThree = findViewById(R.id.diceNumbThree);
                gameApplication.getGame().setCubeColorThree((CubeColor) propertyChangeEvent.getNewValue());
                CubeColor newColorThree = gameApplication.getGame().getCubeColorThree();
                colorThree.setColorFilter(newColorThree.getCubeColor());
                numbThree.setColorFilter(newColorThree.getCubeColor());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        gameApplication.getGame().removeListener(this);
        super.onDestroy();
    }
}
