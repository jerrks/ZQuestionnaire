package com.questionnaire.activity.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.questionnaire.R;
import com.questionnaire.content.MediaInfoItem;
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
        return getString(R.string.record_audio_list);
    }

    @Override
    protected String getBottomText() {
        return getString(R.string.record_audio_add);
    }

    @Override
    protected Bitmap getItemThumbnail() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_audio_record);
    }

    @Override
    protected void addMedia() {
        startAddMedia(REQUEST_AUDIO_CODE, MediaManager.getAudioPath());
    }

    @Override
    protected void previewMedia(MediaInfoItem item) {
        MediaManager.previewAudio(mContaxt, item.getFilePath());
    }
}
