package com.example.hw1_ellarashty;



import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer song, leveUp;

    //Buttons for moving the player
    private Button right, left;

    private ImageView [] path, hearts;
    private int playerIndex, hearts_count = 3;;

    //Enemy movement
    private ImageView[] enemy , bonus;
//    private ValueAnimator[] enemy_animations, bonus_animations;
    private int animationIndex, screenHeight, bonusAnimationIndex;

    //Duration of the play
    private TextView duration_time;
    private Timer timer ;
    int clock = 0;

    private Animation anima;

    public static boolean sensors_is_on;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        findViews();
        initView();
        startAnimations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
        song.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void initView() {
        if (Start_Menu.sensors_switch != null){
            if(Start_Menu.sensors_switch.isChecked())
                sensors_is_on =true;
            else
                sensors_is_on =false;
        }
        //Movement right
        right = findViewById(R.id.right_BTN);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerIndex<4)
                    moveRight(right);
            }
        });

        //Movement left
        left = findViewById(R.id.left_BTN);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerIndex>0)
                    moveLeft(left);
            }
        });
    }

    private void startAnimations() {
        WindowManager wm = getWindowManager();
        Display dsp = wm.getDefaultDisplay();
        Point size = new Point();
        dsp.getSize(size);
        screenHeight=size.y;
        setUpEnemy();
        setUpBonus();
        if (sensors_is_on)
            hide_arrows();
    }

    private void hide_arrows() {
        right.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);
    }

    private void setUpEnemy(){
        ValueAnimator[] enemy_animations;
        enemy_animations = new ValueAnimator[enemy.length];
        for (animationIndex = 0 ; animationIndex < enemy.length ; animationIndex++){
            SetAnimationsParameters(enemy_animations,animationIndex,500);
            enemy_animations[animationIndex].start();
            enemy_animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int enemyIndex = animationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue =(int) updatedAnimation.getAnimatedValue();
                    //Movement of the enemy
                    enemy[enemyIndex].setTranslationY(animatedValue);
                    //Checks if there is a collision
                    if(checkCrash(enemy[enemyIndex],path[playerIndex])) {
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
        animations[animIndex] = ValueAnimator.ofInt(initialHeight ,screenHeight + 400);
        animations[animIndex].setDuration(6000 + (long) (Math.random() * 6000 - 1));
        animations[animIndex].setStartDelay((long) (Math.random() * 1));
        animations[animIndex].setRepeatCount(Animation.INFINITE);
    }

    private void setUpBonus() {
        ValueAnimator[] bonus_animations;
        bonus_animations = new ValueAnimator[bonus.length];
        for (bonusAnimationIndex = 0 ; bonusAnimationIndex < bonus.length ; bonusAnimationIndex++){
            SetAnimationsParameters(bonus_animations, bonusAnimationIndex,3000);
            bonus_animations[bonusAnimationIndex].start();
            bonus_animations[bonusAnimationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int bonusIndex = bonusAnimationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue =(int) updatedAnimation.getAnimatedValue();
                    //Movement of the bonus
                    bonus[bonusIndex].setTranslationY(animatedValue);
                    //Checks if there is a collision
                    if(checkCrash(bonus[bonusIndex],path[playerIndex])) {
                        bonus[bonusIndex].setY(-120);
//                        updateCrash();
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

    private void updateCrash() {
        vibrate();
        hearts[hearts_count - 1].setVisibility(View.INVISIBLE);
        hearts_count--;
        //To know which Power Puff girl is playing
        if (hearts_count == 0)
            finish();
        else if (hearts_count == 2) {
            path[playerIndex].setImageResource(R.drawable.img_blossom);
            //Animation for a player replacement
            anima = AnimationUtils.loadAnimation(this, R.anim.rotate);
            path[playerIndex].startAnimation(anima);
        }
        else {
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

        Rect rect1=new Rect(object_locate[0],object_locate[1],(int)(object_locate[0]+ enemy_col.getWidth()),(int)(object_locate[1]+enemy_col.getHeight()));
        Rect rect2=new Rect(player_locate[0],player_locate[1],(int)(player_locate[0]+ player_col.getWidth()),(int)(player_locate[1]+player_col.getHeight()));

        return Rect.intersects(rect1,rect2);
    }

    private void findViews() {
        duration_time = findViewById(R.id.duration_time);
        //The path of the player
        path = new ImageView[]{
                findViewById(R.id.player_0), findViewById(R.id.player_1), findViewById(R.id.player_2),
                findViewById(R.id.player_3),findViewById(R.id.player_4)};

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

        song = MediaPlayer.create(this,R.raw.cover_song);
        leveUp = MediaPlayer.create(this, R.raw.blossom_level_up_1);

    }


    //Move the player to the left
    private void moveRight(View view) {
        path[playerIndex].setImageResource(0);
        //To know which Power Puff girl is playing
        if(hearts_count==3)
            path[playerIndex+1].setImageResource(R.drawable.img_buttercup);
        else if(hearts_count==2)
            path[playerIndex+1].setImageResource(R.drawable.img_blossom);
        else
            path[playerIndex+1].setImageResource(R.drawable.img_bubbles);
        playerIndex++;

    }

    //Move the player to the right
    private void moveLeft(View view) {
        path[playerIndex].setImageResource(0);
        //To know which Power Puff girl is playing
        if(hearts_count==3)
            path[playerIndex - 1].setImageResource(R.drawable.img_buttercup);
        else if(hearts_count==2)
            path[playerIndex - 1].setImageResource(R.drawable.img_blossom);
        else
            path[playerIndex - 1].setImageResource(R.drawable.img_bubbles);
        playerIndex--;
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
        duration_time.setText("Time: " + clock);

        //After every 20 seconds an animation will be activated
        if(clock%20==0){
            anima = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            duration_time.startAnimation(anima);
        }
    }


}