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
        return getString(R.string.image_list);
    }

    @Override
    protected String getBootomText() {
        return getString(R.string.image_add);
    }

    @Override
    protected void addMedia() {
        super.addMedia();
    }
}
