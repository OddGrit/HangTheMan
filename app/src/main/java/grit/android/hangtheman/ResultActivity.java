package grit.android.hangtheman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.d("log lifecycle", "onCreate " + getLocalClassName());

        //Load result from intent
        Intent intent = getIntent();
        boolean won = intent.getBooleanExtra("won", false);
        int score = intent.getIntExtra("score", 0);

        //set TextviewText
        TextView scoreTextTextView = (TextView) findViewById(R.id.scoreTextTextView);
        if (won){
            scoreTextTextView.setText(R.string.score_text);
        } else {
            scoreTextTextView.setText(R.string.score_text_fail);
        }

        //Set result
        TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        scoreTextView.setText(score + "");

        //set Highscore
        SharedPreferences preferences = getSharedPreferences("highscore", MODE_PRIVATE);
        int highscoreResult = preferences.getInt("result", 0);

        TextView highscoreTextView = (TextView) findViewById(R.id.highscoreTextView);
        highscoreTextView.setText(highscoreResult + "");

        //Check if new highscore
        if (won && (score < highscoreResult || highscoreResult == 0)){
            SharedPreferences.Editor editor =
                    getApplicationContext().getSharedPreferences("highscore", MODE_PRIVATE).edit();

            editor.putInt("result", score);
            editor.apply();
            editor.commit();
        }

        //Set button
        Button restartButton = (Button) findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), MainActivity.class);
                setResult(2);
                finish();
            }
        });

        preferences = getApplicationContext().getSharedPreferences("gameState", MODE_PRIVATE);
        //removing stored game
        SharedPreferences.Editor editor =
                getApplicationContext().getSharedPreferences("gameState", MODE_PRIVATE).edit();
        editor.putBoolean("stateSaved", false);
        editor.apply();
        editor.commit();
    }
}
