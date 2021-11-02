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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ImageView[] hearts, enemy;
    private Button right, left;

    private ImageView [] path;
    private int playerIndex;


    private ValueAnimator[] animations;
    private int animationIndex, screenHeight,hearts_count = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        enemy = new ImageView[]{
                findViewById(R.id.enemy_1), findViewById(R.id.enemy_2), findViewById(R.id.enemy_3)
        };

        hearts = new ImageView[]{
                findViewById(R.id.heart_1), findViewById(R.id.heart_2), findViewById(R.id.heart_3)
        };


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


        findViews();

        start();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void start() {
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenHeight=size.y;
        setUpEnemyAnimations();

    }


    public void setUpEnemyAnimations(){
        animations = new ValueAnimator[enemy.length];

        for (animationIndex = 0 ; animationIndex < enemy.length ; animationIndex++){
            SetEnemyAnimParameters();
            animations[animationIndex].start();
            animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int position = animationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue =(int) updatedAnimation.getAnimatedValue();
//                    if (toResume == true)
//                        enemy[position].setTranslationY(enemy[position].getTranslationY());
//                    else
                    enemy[position].setTranslationY(animatedValue);
                    if(isCollision(enemy[position],path[playerIndex])) {
                        enemy[position].setY(-130);
                        checkCrash();
                        updatedAnimation.start();
                    }
//                    else updateScore(enemy[position],updatedAnimation,enemyMoney);
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

    private void checkCrash() {
        vibrate();
        if(hearts_count == 1) {
            hearts[hearts_count - 1].setVisibility(View.INVISIBLE);
//            pauseGame();
            finish();
        }
        else {
            hearts[hearts_count - 1].setVisibility(View.INVISIBLE);
            hearts_count--;
        }
    }

    private boolean isCollision(ImageView objectCol, ImageView playerCol) {
        int[] object_locate = new int[2];
        int[] player_locate = new int[2];

        objectCol.getLocationOnScreen(object_locate);
        playerCol.getLocationOnScreen(player_locate);
        Log.d("pttt", "B player_locate=" + player_locate );


        Rect rect1=new Rect(object_locate[0],object_locate[1],(int)(object_locate[0]+ objectCol.getWidth()),(int)(object_locate[1]+objectCol.getHeight()));
        Rect rect2=new Rect(player_locate[0],player_locate[1],(int)(player_locate[0]+ playerCol.getWidth()),(int)(player_locate[1]+playerCol.getHeight()));

        return Rect.intersects(rect1,rect2);
    }

    private void SetEnemyAnimParameters() {
        final int initialHeight = -(500 + (int) (Math.random() * 1000));
        animations[animationIndex] = ValueAnimator.ofInt(initialHeight ,screenHeight + 400);
        animations[animationIndex].setDuration(7000 + (long) (Math.random() * 7000 - 1));
        animations[animationIndex].setStartDelay((long) (Math.random() * 1));
        animations[animationIndex].setRepeatCount(Animation.INFINITE);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


//    for view
    private void findViews() {
        path = new ImageView[]{
                findViewById(R.id.player_0), findViewById(R.id.player_1), findViewById(R.id.player_2)
        };
        playerIndex = 1;


    }

//    public void moveRight(View view) {
//        if (player.getX() < (getResources().getDisplayMetrics().widthPixels * 2.0 / enemy.length))
//            player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / enemy.length);
//
//    }

//    public void moveLeft(View view) {
//        if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 0.5 / enemy.length))
//            player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / enemy.length);
//    }

//    public void moveRight(View view) {
//            player.setLayoutParams(player.getLayoutParams());
//
//    }

    public void moveRight(View view) {
//        if (player.getX() < (getResources().getDisplayMetrics().widthPixels * 2.0 / enemy.length))
//            player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / enemy.length);
        path[playerIndex].setImageResource(0);
        path[playerIndex+1].setImageResource(R.drawable.img_buttercup);
        playerIndex++;

    }

    public void moveLeft(View view) {
//        if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 0.5 / enemy.length))
//            player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / enemy.length);
        path[playerIndex].setImageResource(0);
        path[playerIndex-1].setImageResource(R.drawable.img_buttercup);
        playerIndex--;
    }
}