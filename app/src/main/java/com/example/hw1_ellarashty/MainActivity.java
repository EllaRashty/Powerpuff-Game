package com.example.hw1_ellarashty;



import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button right, left;

    private ImageView [] path, hearts;
    private int playerIndex, hearts_count = 3;;

    private ImageView[] enemy;
    private ValueAnimator[] enemy_animations;
    private int animationIndex, screenHeight;

    private TextView duration_time;
    private Timer timer ;
    int clock = 0;

    private Animation anima;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        duration_time = findViewById(R.id.duration_time);

        findViews();
        initView();
        startAnimations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void initView() {
        right = findViewById(R.id.right_BTN);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerIndex<2)
                    moveRight(right);
            }
        });

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
    }


    public void setUpEnemy(){
        enemy_animations = new ValueAnimator[enemy.length];
        for (animationIndex = 0 ; animationIndex < enemy.length ; animationIndex++){
            SetEnemyAnimationsParameters();
            enemy_animations[animationIndex].start();
            enemy_animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int enemyIndex = animationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue =(int) updatedAnimation.getAnimatedValue();
                    enemy[enemyIndex].setTranslationY(animatedValue);
                    if(isCollision(enemy[enemyIndex],path[playerIndex])) {
                        enemy[enemyIndex].setY(-120);
                        updateCrash();
                        updatedAnimation.start();
                    }
                }
            });
        }
    }

    private void SetEnemyAnimationsParameters() {
        final int initialHeight = -(500 + (int) (Math.random() * 100));
        enemy_animations[animationIndex] = ValueAnimator.ofInt(initialHeight ,screenHeight + 400);
        enemy_animations[animationIndex].setDuration(6000 + (long) (Math.random() * 6000 - 1));
        enemy_animations[animationIndex].setStartDelay((long) (Math.random() * 1));
        enemy_animations[animationIndex].setRepeatCount(Animation.INFINITE);
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
        if (hearts_count == 0)
            finish();
        else if (hearts_count == 2) {
            path[playerIndex].setImageResource(R.drawable.img_blossom);
            anima = AnimationUtils.loadAnimation(this, R.anim.rotate);
            path[playerIndex].startAnimation(anima);
        }
        else {
            path[playerIndex].setImageResource(R.drawable.img_bubbles);
            anima = AnimationUtils.loadAnimation(this, R.anim.rotate);
            path[playerIndex].startAnimation(anima);
        }
    }

    private boolean isCollision(ImageView objectCol, ImageView playerCol) {
        int[] object_locate = new int[path.length];
        int[] player_locate = new int[path.length];

        objectCol.getLocationOnScreen(object_locate);
        playerCol.getLocationOnScreen(player_locate);

        Rect rect1=new Rect(object_locate[0],object_locate[1],(int)(object_locate[0]+ objectCol.getWidth()),(int)(object_locate[1]+objectCol.getHeight()));
        Rect rect2=new Rect(player_locate[0],player_locate[1],(int)(player_locate[0]+ playerCol.getWidth()),(int)(player_locate[1]+playerCol.getHeight()));

        return Rect.intersects(rect1,rect2);
    }



    private void findViews() {
        path = new ImageView[]{
                findViewById(R.id.player_0), findViewById(R.id.player_1), findViewById(R.id.player_2)
        };
        enemy = new ImageView[]{
                findViewById(R.id.enemy_1), findViewById(R.id.enemy_2), findViewById(R.id.enemy_3)
        };

        hearts = new ImageView[]{
                findViewById(R.id.heart_1), findViewById(R.id.heart_2), findViewById(R.id.heart_3)
        };
        playerIndex = 1;
    }

    public void moveRight(View view) {
        path[playerIndex].setImageResource(0);
        if(hearts_count==3)
            path[playerIndex+1].setImageResource(R.drawable.img_buttercup);
        else if(hearts_count==2)
            path[playerIndex+1].setImageResource(R.drawable.img_blossom);
        else
            path[playerIndex+1].setImageResource(R.drawable.img_bubbles);
        playerIndex++;

    }

    public void moveLeft(View view) {
        path[playerIndex].setImageResource(0);
        if(hearts_count==3)
            path[playerIndex - 1].setImageResource(R.drawable.img_buttercup);
        else if(hearts_count==2)
            path[playerIndex - 1].setImageResource(R.drawable.img_blossom);
        else
            path[playerIndex - 1].setImageResource(R.drawable.img_bubbles);
        playerIndex--;
    }

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
        if(clock%20==0){
            anima = AnimationUtils.loadAnimation(this, R.anim.sample_anim);
            duration_time.startAnimation(anima);
        }
    }


}