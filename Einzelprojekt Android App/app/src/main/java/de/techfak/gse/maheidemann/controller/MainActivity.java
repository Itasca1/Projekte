package de.techfak.gse.maheidemann.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import de.techfak.gse.maheidemann.modell.CheckValidField;
import de.techfak.gse.maheidemann.modell.Game;
import de.techfak.gse.maheidemann.R;
import de.techfak.gse.maheidemann.modell.InvalidBoardLayoutException;
import de.techfak.gse.maheidemann.modell.InvalidFieldException;

/**
 * Entry point activity for the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * GameApplication zum Speichern von Daten.
     */
    GameApplication gameApplication;
    /**
     * Name für dne TAG.
     */
    private final String tag = "Info";

    /**
     * ErrorMessage für Exceptions.
     */
    private final String errorMessageInvalidField = "Überarbeiten Sie "
            + "bitte Ihre Spielfeldeingabe. Die Auswahl der Buchstaben "
            + "entpricht nicht b,B, g, G, y, Y, r, R, o, O.";

    private final String errorMessageInvalidBoardLayout = "Überarbeiten Sie bitte ihre Spielfeldeingabe."
            + " Die Anzahl von Zeilen und Spalten entpricht nicht dem Format 7*15";
    /**
     * String für NeutralButton.
     */
    private final String neutralButton = "Ok";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * OnClick Methode die die Textfeldeingabe liest und in dem Logcat wiedergibt.
     * @param view
     */
    public void onClickSpielStarten(View view) {
        gameApplication = (GameApplication) getApplication();
        EditText editText = findViewById(R.id.editText);
        Log.d(tag, editText.getText().toString());
        String[] spielFeld = editText.getText().toString().split("\n");
        try {
            CheckValidField checkValidField = new CheckValidField();
            checkValidField.checkValidField(spielFeld);
            Game game = new Game(spielFeld);
            gameApplication.setSpielfeldeingabe(spielFeld);
            gameApplication.setGame(game);
            Intent intent = new Intent(MainActivity.this, GameboardActivity.class);
            startActivity(intent);
        } catch (InvalidBoardLayoutException invalidBoardLayoutException) {
            AlertDialog.Builder invalidBoard = new AlertDialog.Builder(MainActivity.this);
            invalidBoard = invalidBoard.setTitle("InvaliedBoardLayout");
            invalidBoard = invalidBoard.setMessage(errorMessageInvalidBoardLayout);
            invalidBoard = invalidBoard.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            invalidBoard.show();
        } catch (InvalidFieldException invalidFieldException) {
            AlertDialog.Builder invalidField = new AlertDialog.Builder(MainActivity.this);
            invalidField = invalidField.setTitle("InvalidField");
            invalidField = invalidField.setMessage(errorMessageInvalidField);
            invalidField = invalidField.setNeutralButton(neutralButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
            });
            invalidField.show();
        }
    }
}
