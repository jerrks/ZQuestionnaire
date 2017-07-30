package com.questionnaire.activity.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterMedia;
import com.questionnaire.content.MediaInfoItem;
import com.questionnaire.content.MediaManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/7/27.
 */

public abstract class ActivityMediaListBase extends ActivityBase implements View.OnClickListener {

    public static final String TAG = MediaManager.TAG + ".Activity";

    public static final int REQUEST_IMAGE_CODE = 0x01;
    public static final int REQUEST_VIDEO_CODE = 0x02;
    public static final int REQUEST_AUDIO_CODE = 0x03;

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
        initListData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initListData();
    }

    public void initView() {
        intHeaderViews();

        TextView bootumBtn = (TextView) findViewById(R.id.media_add_tv);
        bootumBtn.setVisibility(View.VISIBLE);
        bootumBtn.setText(getBottomText());
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
            case R.id.media_add_tv:
                addMedia();
                break;
            default:
                break;
        }
    }

    /**
     * 直接遍历音频目录下的文件列表，显示在音频列表界面
     */
    protected void initListData() {
        initDateSet("initListData");
        mAdapter = new AdapterMedia(mContaxt, mDataSet);
        mListView.setAdapter(mAdapter);
    }

    protected void initDateSet(String remark) {
        String mediaType = getMediaType();
        String dir = MediaManager.getMediaDir(mediaType);
        File dirFile = new File(dir);
        File[] list = dirFile.listFiles();
        List<MediaInfoItem> dataSet = new ArrayList<MediaInfoItem>();
        if (list != null && list.length > 0) {
            for (File file : list) {
                dataSet.add(MediaInfoItem.fromFile(file));
            }
        } else {
            Log.w(TAG, "initDateSet empty dir: " + dir);
        }
        Log.i(TAG, "Init data set  mediaType= " + mediaType + ", count= " + dataSet.size() + ",: " + remark);
        mDataSet.clear();
        if (!dataSet.isEmpty()) {
            mDataSet.addAll(dataSet);
        }
    }

    protected void updateDataSet(List<MediaInfoItem> dataSet) {
        if (mAdapter == null) {
            mAdapter = new AdapterMedia(mContaxt, dataSet);
            mListView.setAdapter(mAdapter);
        }
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        mAdapter.updateDataSet(dataSet);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initDateSet("onActivityResult");
            updateDataSet(mDataSet);
        }
    }

    protected  abstract String getMediaType();

    protected  abstract String getTitleText();

    protected  abstract String getBottomText();

    /**
     * 开始录制音频文件，保存在自己的目录下
     */
    protected void addMedia() {

    }
}
