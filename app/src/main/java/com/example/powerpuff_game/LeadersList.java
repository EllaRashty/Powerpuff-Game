package com.example.powerpuff_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class LeadersList extends AppCompatActivity {
    private Button back_btn;
    public static final String SCORE_BOARD = "SCORE_BOARD";

    private FragmentList fragment_list;
    private Fragment_Map fragment_map;

    private TextView title;

    private CallBack_List callBack_list = new CallBack_List() {
        @Override
        public void sendScoreBoardId(int id) {
            fragment_map.showMarker(id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders_list);
        getSupportActionBar().hide();

        findViews();
        init();
        actions();
    }

    private void init() {
        fragment_list = new FragmentList();
        fragment_list.setCallBack_list(callBack_list);
        getSupportFragmentManager().beginTransaction().add(R.id.List, fragment_list).commit();
        getIntent().putExtra(SCORE_BOARD, openScoreBoard());

        fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().add(R.id.score_LAY_map, fragment_map).commit();
    }

    private void actions() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void findViews() {
        back_btn = findViewById(R.id.back);
        title = findViewById(R.id.title);
        title.setText("Leaderboard");
    }

    public TopTen openScoreBoard() {
        TopTen score_board;
        Gson gson = new Gson();
        MySPV.init(this);
        String topTen = MySPV.getInstance().getString(MySPV.KEYS.KEY_SCORE_BOARD, null);
        if (topTen == null)
            score_board = new TopTen();
        else
            score_board = gson.fromJson(topTen, TopTen.class);
        return score_board;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}