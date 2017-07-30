package com.questionnaire.activity.media;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.questionnaire.R;
import com.questionnaire.content.CameraManager;
import com.questionnaire.content.MediaManager;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class ActivityVideoList extends ActivityMediaListBase {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected String getMediaType() {
        return MediaManager.TYPE_VIDEO;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.video_list);
    }

    @Override
    protected String getBottomText() {
        return getString(R.string.video_add);
    }

    @Override
    protected void addMedia() {
        CameraManager.startCameraForVideo(ActivityVideoList.this, MediaManager.getVideoPath(), REQUEST_VIDEO_CODE);
    }
}
