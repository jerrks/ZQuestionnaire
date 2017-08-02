package com.questionnaire.activity.media;

import android.content.Intent;
import android.os.Bundle;

import com.questionnaire.R;
import com.questionnaire.content.CameraManager;
import com.questionnaire.content.MediaInfoItem;
import com.questionnaire.content.MediaManager;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class ActivityVideoList extends ActivityMediaListBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        startAddMedia(REQUEST_VIDEO_CODE, MediaManager.getVideoPath());
    }

    @Override
    protected void previewMedia(MediaInfoItem item) {
        MediaManager.previewVideo(getApplicationContext(), item.getFilePath());
    }
}
