package com.questionnaire.activity.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public abstract class ActivityMediaListBase extends ActivityBase implements View.OnClickListener {

    public ListView mListView;
    public AdapterMedia mAdapter;
    public List<MediaInfoItem> mDataSet = new ArrayList<MediaInfoItem>();

    public  Context mContaxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        mContaxt = getApplicationContext();
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    public void initView() {
        intHeaderViews();

        TextView bootumBtn = (TextView) findViewById(R.id.media_add_tv);
        bootumBtn.setVisibility(View.VISIBLE);
        bootumBtn.setText(getBootomText());
        bootumBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.media_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
    }

    protected void intHeaderViews() {
        View v = findViewById(R.id.title_layout);
        TextView titleTv = (TextView) v.findViewById(R.id.title_center);
        titleTv.setText(getTitleText());

        Button leftBtn = (Button) v.findViewById(R.id.title_left);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);

        Button rightBtn = (Button) v.findViewById(R.id.title_right);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText(R.string.add);
        rightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                addMedia();
                break;
        }
    }

    /**
     * 直接遍历音频目录下的文件列表，显示在音频列表界面
     */
    protected void initData() {
        mAdapter = new AdapterMedia(mContaxt, mDataSet);
        mListView.setAdapter(mAdapter);
    }

    protected void updateDataSet(List<MediaInfoItem> dataSet) {
        if (mAdapter == null) {
            mAdapter = new AdapterMedia(mContaxt, mDataSet);
        }
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        mAdapter.updateDataSet(dataSet);
        mAdapter.notifyDataSetChanged();
    }

    protected  abstract String getTitleText();

    protected  abstract String getBootomText();

    /**
     * 开始录制音频文件，保存在自己的目录下
     */
    protected void addMedia() {

    }
}
