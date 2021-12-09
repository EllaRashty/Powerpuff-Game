package com.example.powerpuff_game;


import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer song, leveUp;

    //Buttons for moving the player
    private Button right, left;

    private ImageView[] path, hearts;
    private int playerIndex, hearts_count = 3;
    ;

    //Enemy movement
    private ImageView[] enemy, bonus;
    private int animationIndex, screenHeight, bonusAnimationIndex;

    //Duration of the play & score
    private TextView duration_time, score_view;
    private Timer timer;
    int clock = 0, score = 0;

    private Animation anima;

    public static boolean sensors_is_on;
    private float x_sensor;

    private SensorManager sensorManager;
    private Sensor accSensor;

    private Leader winner;
    private LocationManager locationManager;
    private LocationListener listener;
    private double l1, l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        findViews();
        initView();
        startAnimations();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l1= location.getLongitude();
                l2=location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
        song.start();
        song.setLooping(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        song.pause();
        stopTicker();
    }

    private void initView() {
        score_view.setText("SCORE: " + score);
        initSensor();

        //Movement right
        right = findViewById(R.id.right_BTN);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight();
            }
        });

        //Movement left
        left = findViewById(R.id.left_BTN);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();
            }
        });
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (Start_Menu.sensors_switch != null) {
            if (Start_Menu.sensors_switch.isChecked()) {
                sensors_is_on = true;
            } else {
                sensors_is_on = false;
            }
        }

    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (sensors_is_on) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    x_sensor -= event.values[0];
                    if (x_sensor > 1.7) {
                        moveRight();
                        x_sensor = 1;
                    } else if (x_sensor < -1.7) {
                        moveLeft();
                        x_sensor = -1;
                    } else if (-0.9 < x_sensor && x_sensor < 0.9) {
                        x_sensor = 0;
                    }
                    DecimalFormat df = new DecimalFormat("##.##");
                    duration_time.setText(
                            df.format(x_sensor) + "\n"
                    );
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accSensorEventListener);
        song.pause();
    }

//    public boolean isSensorExist(int sensorType) {
//        return (sensorManager.getDefaultSensor(sensorType) != null);
//    }

    private void startAnimations() {
        WindowManager wm = getWindowManager();
        Display dsp = wm.getDefaultDisplay();
        Point size = new Point();
        dsp.getSize(size);
        screenHeight = size.y;
        setUpEnemy();
        setUpBonus();
        if (sensors_is_on)
            hide_arrows();
    }

    private void hide_arrows() {
        right.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);
    }

    private void setUpEnemy() {
        ValueAnimator[] enemy_animations;
        enemy_animations = new ValueAnimator[enemy.length];
        for (animationIndex = 0; animationIndex < enemy.length; animationIndex++) {
            SetAnimationsParameters(enemy_animations, animationIndex, 500);
            enemy_animations[animationIndex].start();
            enemy_animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int enemyIndex = animationIndex;

                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue = (int) updatedAnimation.getAnimatedValue();
                    //Movement of the enemy
                    enemy[enemyIndex].setTranslationY(animatedValue);
                    //Checks if there is a collision
                    if (checkCrash(enemy[enemyIndex], path[playerIndex])) {
                        enemy[enemyIndex].setY(-120);
                        updateCrash();
                        updatedAnimation.start();
                    }
                }
            });
        }
    }

    private void SetAnimationsParameters(ValueAnimator[] animations, int animIndex, int num) {
        final int initialHeight = -(num + (int) (Math.random() * 100));
        animations[animIndex] = ValueAnimator.ofInt(initialHeight, screenHeight + 400);
        animations[animIndex].setDuration(6000 + (long) (Math.random() * 6000 - 1));
        animations[animIndex].setStartDelay((long) (Math.random() * 1));
        animations[animIndex].setRepeatCount(Animation.INFINITE);
    }

    private void setUpBonus() {
        ValueAnimator[] bonus_animations;
        bonus_animations = new ValueAnimator[bonus.length];
        for (bonusAnimationIndex = 0; bonusAnimationIndex < bonus.length; bonusAnimationIndex++) {
            SetAnimationsParameters(bonus_animations, bonusAnimationIndex, 3000);
            bonus_animations[bonusAnimationIndex].start();
            bonus_animations[bonusAnimationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int bonusIndex = bonusAnimationIndex;

                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue = (int) updatedAnimation.getAnimatedValue();
                    //Movement of the bonus
                    bonus[bonusIndex].setTranslationY(animatedValue);
                    //Checks if there is a collision
                    if (checkCrash(bonus[bonusIndex], path[playerIndex])) {
                        bonus[bonusIndex].setY(-120);
//                        updateCrash();
                        updateScore(5);
                        updatedAnimation.start();
                    }
                }
            });
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void enterToLeadersList() {
        Intent intent = new Intent(MainActivity.this, LeadersList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void updateCrash() {
        vibrate();
        hearts[hearts_count - 1].setVisibility(View.INVISIBLE);
        hearts_count--;
        //To know which Power Puff girl is playing
        if (hearts_count == 0) {
            winner = new Leader(Start_Menu.playerName, score,l2, l1);
            updateScoreBoard();
            enterToLeadersList();
        } else if (hearts_count == 2) {
            path[playerIndex].setImageResource(R.drawable.img_blossom);
            //Animation for a player replacement
            anima = AnimationUtils.loadAnimation(this, R.anim.rotate);
            path[playerIndex].startAnimation(anima);
        } else {
            path[playerIndex].setImageResource(R.drawable.img_bubbles);
            //Animation for a player replacement
            anima = AnimationUtils.loadAnimation(this, R.anim.rotate);
            path[playerIndex].startAnimation(anima);
        }
        leveUp.start();

    }

    //Checks if two objects in the same position (collision)
    private boolean checkCrash(ImageView enemy_col, ImageView player_col) {
        int[] object_locate = new int[path.length];
        int[] player_locate = new int[path.length];

        enemy_col.getLocationOnScreen(object_locate);
        player_col.getLocationOnScreen(player_locate);

        Rect rect1 = new Rect(object_locate[0], object_locate[1], (int) (object_locate[0] + enemy_col.getWidth()), (int) (object_locate[1] + enemy_col.getHeight()));
        Rect rect2 = new Rect(player_locate[0], player_locate[1], (int) (player_locate[0] + player_col.getWidth()), (int) (player_locate[1] + player_col.getHeight()));

        return Rect.intersects(rect1, rect2);
    }

    private void findViews() {
        score_view = findViewById(R.id.score);

        duration_time = findViewById(R.id.duration_time);
        //The path of the player
        path = new ImageView[]{
                findViewById(R.id.player_0), findViewById(R.id.player_1), findViewById(R.id.player_2),
                findViewById(R.id.player_3), findViewById(R.id.player_4)};

        enemy = new ImageView[]{
                findViewById(R.id.enemy_1), findViewById(R.id.enemy_2), findViewById(R.id.enemy_3),
                findViewById(R.id.enemy_4), findViewById(R.id.enemy_5)};

        bonus = new ImageView[]{
                findViewById(R.id.bonus_1), findViewById(R.id.bonus_2), findViewById(R.id.bonus_3),
                findViewById(R.id.bonus_4), findViewById(R.id.bonus_5)};

        hearts = new ImageView[]{
                findViewById(R.id.heart_1), findViewById(R.id.heart_2), findViewById(R.id.heart_3)};

        //Initial position of the player
        playerIndex = 2;

        song = MediaPlayer.create(this, R.raw.cover_song);
        leveUp = MediaPlayer.create(this, R.raw.blossom_level_up_1);

    }


    //Move the player to the left
    private void moveRight() {
        if (playerIndex < 4) {
            path[playerIndex].setImageResource(0);
            //To know which Power Puff girl is playing
            if (hearts_count == 3)
                path[playerIndex + 1].setImageResource(R.drawable.img_buttercup);
            else if (hearts_count == 2)
                path[playerIndex + 1].setImageResource(R.drawable.img_blossom);
            else
                path[playerIndex + 1].setImageResource(R.drawable.img_bubbles);
            playerIndex++;
        }
    }

    //Move the player to the right
    private void moveLeft() {
        if (playerIndex > 0) {
            path[playerIndex].setImageResource(0);
            //To know which Power Puff girl is playing
            if (hearts_count == 3)
                path[playerIndex - 1].setImageResource(R.drawable.img_buttercup);
            else if (hearts_count == 2)
                path[playerIndex - 1].setImageResource(R.drawable.img_blossom);
            else
                path[playerIndex - 1].setImageResource(R.drawable.img_bubbles);
            playerIndex--;
        }
    }


    // Duration of the play
    private void startTicker() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateClock();
                    }
                });
            }
        }, 10, 1000);
    }


    private void stopTicker() {
        timer.cancel();
    }

    private void updateClock() {
        clock++;
//        duration_time.setText("Time: " + clock);

        //After every 20 seconds an animation will be activated and the player will receive points
        if (clock % 20 == 0) {
            anima = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            duration_time.startAnimation(anima);
            updateScore(10);
            score_view.startAnimation(anima);
        }
    }

    private void updateScore(int points) {
        score += points;
        anima = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
        score_view.setText("SCORE: " + score);
    }

    private void updateScoreBoard() {
        TopTen score_board;
        Gson gson = new Gson();

        String topTen = MySPV.getInstance().getString(MySPV.KEYS.KEY_SCORE_BOARD, null);
        if (topTen == null) {
            score_board = new TopTen();
        } else {
            score_board = gson.fromJson(topTen, TopTen.class);
        }
        if (score_board.addLeader(winner)) {
            String ttJson = gson.toJson(score_board);
            MySPV.getInstance().putString(MySPV.KEYS.KEY_SCORE_BOARD, ttJson);
            Toast.makeText(this, "You are added to TOP-TEN", Toast.LENGTH_SHORT).show();
        }
    }


}