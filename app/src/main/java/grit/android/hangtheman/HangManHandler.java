package grit.android.hangtheman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class HangManHandler {
    View view;
    Activity activity;
    ImageHandler imageHandler;
    TextView wrongGuessTextView;
    TextView correctWordTextView;
    EditText guessEditText;

    String word;
    String wrongGuesses = "";
    boolean[] rightGuesses;

    int timer = 30;

    public HangManHandler(Activity activity){
        Log.d("log lifecycle", "Constructor HangManHandler");

        //this.view = view;
        this.activity = activity;
        imageHandler = new ImageHandler((View) activity.findViewById(android.R.id.content));

        int size = activity.getResources().getStringArray(R.array.words).length;

        wrongGuessTextView = (TextView) activity.findViewById(R.id.wrongGuessTextView);
        correctWordTextView = (TextView) activity.findViewById(R.id.correctWordTextView);
        guessEditText = (EditText) activity.findViewById(R.id.guessEditText);
        word = activity.getResources().getStringArray(R.array.words)[(int) (Math.random() * size)];
        rightGuesses = new boolean[word.length()];

        updateState(false);
        updateTime();
    }

    public void handleInput(String input){
        boolean hasGuessedWrong = false;
        input = input.toUpperCase();
        for (char c : input.toCharArray()){
            if (wrongGuesses.indexOf(c) != -1){
                continue;
            } else if (word.toUpperCase().indexOf(c) != -1){
                for (int index = word.toUpperCase().indexOf(c); index >= 0;
                        index = word.toUpperCase().indexOf(c, index + 1)) {
                    rightGuesses[index] = true;
                }
                continue;
            } else {
                wrongGuesses += c + " ";
                hasGuessedWrong = true;
            }
        }
        updateState(hasGuessedWrong);
    }

    //Returns true if times up, false otherwise
    public boolean updateTime(){
        Log.d("log lifecycle", "HangManHandler.updateTime()");
        TextView timerTextView = (TextView) activity.findViewById(R.id.timerTextView);

        if (timer <= 0){
            startActivity(false);
            return true;
        } else {
            String text = String.valueOf(timer--);
            timerTextView.setText(text);
        }
        return false;
    }

    public void restoreGame(){
        Log.d("log action", "HangManHandler.restoreGame()");
        SharedPreferences preferences = activity.getApplicationContext()
                .getSharedPreferences("gameState", MODE_PRIVATE);
        if (preferences.getBoolean("stateSaved", false)) {
            word = preferences.getString("word", "");
            wrongGuesses = preferences.getString("wrongGuesses", "");
            rightGuesses = fromString(preferences.getString("rightGuessesString", ""));
            timer = preferences.getInt("timer", 30);
            updateState(false);
        }
    }

    public void saveGame(){
        Log.d("log action", "HangManHandler.saveGame()");
        SharedPreferences.Editor editor = activity.getApplicationContext()
                .getSharedPreferences("gameState", MODE_PRIVATE).edit();

        editor.putBoolean("stateSaved", true);
        editor.putString("word", word);
        editor.putString("wrongGuesses", wrongGuesses);
        editor.putString("rightGuessesString", toString(rightGuesses));
        editor.putInt("timer", timer);
        editor.apply();
        editor.commit();
    }

    private void updateState(boolean wrong){
        wrongGuessTextView.setText(wrongGuesses);
        int imageNumber = 0;
        if (wrong) {
            if (wrongGuesses.length() >= 12) {
                imageNumber = 6;

                startActivity(false);
            } else {
                imageNumber = (int) (wrongGuesses.length() / 2.0);
            }
            imageHandler.setImage(imageNumber);
        }

        StringBuffer rightString = new StringBuffer();
        for (int i = 0; i < rightGuesses.length; i++){
            rightString.append((rightGuesses[i] ? word.charAt(i) : "_") + " ");
        }

        if (numberLettersGuessed() == word.length()){
            Toast.makeText(activity, "You Win!", Toast.LENGTH_SHORT).show();

            /*Intent intent = new Intent(view.getContext(), ResultActivity.class);
            intent.putExtra("won", true);
            intent.putExtra("score", wrongGuesses.length() / 2);
            view.getContext().startActivity(intent);

             */

            startActivity(true);
        }
        correctWordTextView.setText(rightString.toString());

        guessEditText.setText("");
    }

    private int numberLettersGuessed(){
        int num = 0;
        for (boolean b : rightGuesses){
            //Unknown letters return false
            if (b) ++num;
        }
        //Here only if there were no unknown letters
        return num;
    }

    private void startActivity(boolean won){
        Toast.makeText(activity, "You " + (won ? "won!" : "Lose!"), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(activity, ResultActivity.class);
        intent.putExtra("won", won);
        intent.putExtra("score", won ? wrongGuesses.length() / 2 : numberLettersGuessed());

        try {
            activity.startActivityForResult(intent, 1);
        }catch (Exception e){

        }
    }

    private String toString(boolean[] bool){
        StringBuilder str = new StringBuilder();
        for (boolean b : bool){
            str.append( b ? "1" : "0");
        }
        return str.toString();
    }

    private boolean[] fromString(String str){
        boolean[] bool = new boolean[str.length()];
        for (int i = 0; i < str.length(); ++i){
            if (str.charAt(i) == '1') bool[i] = true;
        }
        return bool;
    }
}
