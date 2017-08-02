package com.questionnaire.content;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/7/31.
 */

public class LoadDataTask extends AsyncTask<String, Integer, List<MediaInfoItem>> {

    public static final String TAG = MediaManager.TAG + ".LoadDataTask";
    public interface LoadDataCallback {
        void onLoadProgress(int progress, String filePath);
        void onLoadFinished(int count, List<MediaInfoItem> dataSet);
    }

    String mediaType;
    LoadDataCallback callback;

    public LoadDataTask(String mediaType, LoadDataCallback callback) {
        this.mediaType = mediaType;
        this.callback = callback;
    }

    @Override
    protected List<MediaInfoItem> doInBackground(String... dirs) {
        String dir = dirs[0];
        File dirFile = new File(dir);
        List<MediaInfoItem> dataSet = new ArrayList<MediaInfoItem>();
        File[] list = dirFile.listFiles();
        if (list != null && list.length > 0) {
            int count = list.length;
            int progress = 0;
            for (int i = 0; i < count; i++) {
                File file = list[i];
                dataSet.add(MediaInfoItem.fromFile(file, mediaType));
                if (callback != null) {
                    progress = (i + 1) * 100 / count;
                    callback.onLoadProgress(progress, file.getPath());
                }
            }
        } else {
            Log.w(TAG, "initDateSet empty in file dir: " + dir);
        }
        Log.i(TAG, "Loaded dataSet count= " + dataSet.size() + " in dir: " + dir);
        return dataSet;
    }

    @Override
    protected void onPostExecute(List<MediaInfoItem> dataSet) {
        if (callback != null) {
            callback.onLoadFinished(dataSet.size(), dataSet);
        }
    }
}
