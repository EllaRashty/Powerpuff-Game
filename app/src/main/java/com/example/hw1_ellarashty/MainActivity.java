package com.example.hw1_ellarashty;



import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ImageView player;
    private ImageView[] hearts, enemy;
    private Button right, left;

    private ImageView [] path;
    private int playerIndex;


    private ValueAnimator[] animations;
    private int animationIndex, screenHeight;




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

        player = findViewById(R.id.player_1);

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
//                    if(isCollision(enemy[position],player)) {
//                        enemy[position].setY(-130);
//                        checkHit();
//                        updatedAnimation.start();
//                    }
//                    else updateScore(enemy[position],updatedAnimation,enemyMoney);
                }
            });
        }
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