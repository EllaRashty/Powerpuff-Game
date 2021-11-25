package com.example.hw1_ellarashty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class Start_Menu extends AppCompatActivity {

    private Button startGame, exit;

    public static  Switch sensors_switch;

    private MediaPlayer opening;


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
        exit = findViewById(R.id.exit);
        sensors_switch = findViewById(R.id.sensors);
        opening = MediaPlayer.create(this, R.raw.ppg_theme_chime);
    }

    private void start() {
        opening.start();
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });

        sensors_switch.setChecked(false);
    }

    public void startNewGame(){
        Intent newGameIntent = new Intent(this, MainActivity.class);
        startActivity(newGameIntent);
    }

}