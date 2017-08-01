package com.questionnaire.activity.media;

import android.os.Bundle;

import com.questionnaire.R;
import com.questionnaire.content.MediaManager;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class ActivityAudioList extends ActivityMediaListBase{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getMediaType() {
        return MediaManager.TYPE_AUDIO;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.audio_list);
    }

    @Override
    protected String getBottomText() {
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
