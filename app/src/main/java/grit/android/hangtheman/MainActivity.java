package grit.android.hangtheman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    HangManHandler handler;
    EditText guessEditText;
    Button guessButton;
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("log lifecycle", "onCreate " + getLocalClassName());

        handler = new HangManHandler(this);

        TextView wrongGuessTextView = (TextView) findViewById(R.id.wrongGuessTextView);

        guessButton = (Button) findViewById(R.id.guessButton);
        guessEditText = (EditText) findViewById(R.id.guessEditText);

        guessEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                guessButton.callOnClick();
                return false;
            }
        });

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.handleInput(guessEditText.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("log lifecycle", "onActivityResult() " + getLocalClassName());
        Toast.makeText(this, String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
        handler = new HangManHandler(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("log lifecycle", "onStart() " + getLocalClassName());
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("log lifecycle", "onResume() " + getLocalClassName());
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(handler.updateTime())
                            timer.cancel();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        handler.restoreGame();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("log lifecycle", "onPause() " + getLocalClassName());
        timer.cancel();
        handler.saveGame();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("log lifecycle", "onStop() " + getLocalClassName());

    }
}
