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
        String filePath = MediaManager.getImagePath();
        CameraManager.startCameraForVideo(ActivityVideoList.this, filePath, REQUEST_VIDEO_CODE);
    }

    @Override
    protected void performItemClick(MediaInfoItem item) {
        MediaManager.previewVideo(getApplicationContext(), item.getFilePath());
    }
}
