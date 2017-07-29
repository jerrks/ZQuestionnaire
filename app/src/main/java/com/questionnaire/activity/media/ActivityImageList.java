package com.questionnaire.activity.media;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.questionnaire.R;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class ActivityImageList extends ActivityMediaListBase {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.audio_list);
    }

    /**
     * 开始录制音频文件，保存在自己的目录下
     */
    @Override
    protected void addMidia() {

    }
}
