package com.example.hw1_ellarashty;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity{


    final int ANIM_DURATION = 1000;
//    private MediaPlayer opening;

    private ImageView bomba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        findViews();
        bomba.setVisibility(View.INVISIBLE);
        showViewSlideDown(bomba);

    }

    public void showViewSlideDown(final View v) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height / 2);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
//                        opening.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void animationDone() {
        openHomeActivity();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, Start_Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        bomba = findViewById(R.id.logo);
//        opening = MediaPlayer.create(this, R.raw.ppg_theme_chime);


    }
}
