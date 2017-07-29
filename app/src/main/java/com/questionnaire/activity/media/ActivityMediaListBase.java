package com.questionnaire.activity.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterMedia;
import com.questionnaire.content.MediaInfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class ActivityMediaListBase extends ActivityBase implements View.OnClickListener {

    ListView mListView;
    AdapterMedia mAdapter;
    List<MediaInfoItem> mDataSet = new ArrayList<MediaInfoItem>();

    Context mContaxt;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mContaxt = getApplicationContext();
        setContentView(R.layout.activity_audio_list);
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    void initView() {
        View v = findViewById(R.id.title);
        TextView titleTv = (TextView) v.findViewById(R.id.title_center);
        titleTv.setText(R.string.audio_list);

        Button bt = (Button) v.findViewById(R.id.title_left);
        bt.setVisibility(View.VISIBLE);
        //bt.setText(R.string.cancel);
        bt.setOnClickListener(this);

        bt = (Button) v.findViewById(R.id.title_right);
        bt.setVisibility(View.VISIBLE);
        bt.setText(R.string.paper_author);
        bt.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.paper_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                addAudio();
                break;
        }
    }

    /**
     * 直接遍历音频目录下的文件列表，显示在音频列表界面
     */
    void initData() {
        mAdapter = new AdapterMedia(mContaxt, mDataSet);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 开始录制音频文件，保存在自己的目录下
     */
    void addAudio() {

    }
}
