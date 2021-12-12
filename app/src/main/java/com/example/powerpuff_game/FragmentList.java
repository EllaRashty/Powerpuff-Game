package com.example.powerpuff_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class FragmentList extends Fragment {

    private ListView list_LV_score;
    private TopTen scoreBoard = new TopTen();
    private CallBack_List callBack_list;


    public void setCallBack_list(CallBack_List callBack_list) {
        this.callBack_list = callBack_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        list_LV_score = view.findViewById(R.id.list_LV_score);

        initView();
        return view;
    }

    private void initView() {
        Intent i = getActivity().getIntent();
        scoreBoard = (TopTen) i.getSerializableExtra("SCORE_BOARD");

        //create new adapter
        Adapter_Score recordAdapter = new Adapter_Score(this.getActivity(), R.layout.fragment_list_adapter, scoreBoard.getLeaders());
        list_LV_score.setAdapter(recordAdapter);

        //zoom to map when click on item in score board
        list_LV_score.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callBack_list != null){
                    callBack_list.sendScoreBoardId(position);
                }
            }
        });
    }


}
