package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private final int MIN_WORD_LENGTH = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Log.d("inputStream", e.toString());
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        label.setText(COMPUTER_TURN);
        TextView text = (TextView) findViewById(R.id.ghostText);
        String userText = text.getText().toString();

        if (userText.length() >= MIN_WORD_LENGTH && dictionary.isWord(userText)) {
            // Computer Wins
            label.setText("Computer Wins");
        } else {
            String longerText = dictionary.getAnyWordStartingWith(userText);
            if (longerText == null) {
                // User bluffed
                // Computer Wins
                label.setText("Computer Wins");
            } else {
                String newText = userText + longerText.charAt(userText.length());
                text.setText(newText);
                if (dictionary.isWord(newText)) {
                    label.setText("You WON!");
                } else {
                    label.setText(USER_TURN);
                }
            }
        }
        userTurn = true;
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode < KeyEvent.KEYCODE_A || keyCode > KeyEvent.KEYCODE_Z) {
            return super.onKeyUp(keyCode, event);
        } else {
            TextView text = (TextView) findViewById(R.id.ghostText);
            String word = text.getText().toString();
            word = word + (char)event.getUnicodeChar();
            text.setText(word);
            // Call computerTurn
            computerTurn();
            if (dictionary.isWord(word)) {
                TextView label = (TextView) findViewById(R.id.gameStatus);
                label.setText("Game over");
            }
        }
        return true;
    }
}
