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

public class ActivityAudioList extends ActivityMediaListBase{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mContaxt = getApplicationContext();
        setContentView(R.layout.activity_media_list);
        initView();
        initData();
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.audio_list);
    }

    @Override
    protected String getBootomText() {
        return getString(R.string.audio_add);
    }

    @Override
    protected void addMedia() {
        addAudio();
    }

    /**
     * 开始录制音频文件，保存在自己的目录下
     */
    void addAudio() {

    }
}
