package com.example.powerpuff_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Start_Menu extends AppCompatActivity {

    private Button startGame, exit, scores;

    public static  Switch sensors_switch;

    private MediaPlayer opening;
    public static EditText name;
    public static String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        getSupportActionBar().hide();

        findViews();
        start();
    }


    private void findViews() {
        startGame = findViewById(R.id.startGame);
        scores = findViewById(R.id.scores);
        exit = findViewById(R.id.exit);
        sensors_switch = findViewById(R.id.sensors);
        opening = MediaPlayer.create(this, R.raw.ppg_theme_chime);
        name = findViewById(R.id.editName);
    }

    private void start() {
        opening.start();
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterToLeadersList();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });

        sensors_switch.setChecked(false);
    }

    public void startNewGame(){
        if(name.getText().toString().isEmpty())
            name.setBackgroundColor(Color.WHITE);
        else {
            playerName = name.getText().toString();
            Intent newGameIntent = new Intent(this, MainActivity.class);
            startActivity(newGameIntent);
        }
    }

    public void enterToLeadersList(){
        Intent newGameIntent2 = new Intent(this, LeadersList.class);
        startActivity(newGameIntent2);
    }

}