package com.questionnaire.content;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/7/31.
 */

public class MediaLoadTask extends AsyncTask<String, Integer, List<MediaInfoItem>> {

    public static final String TAG = MediaManager.TAG + ".LoadDataTask";
    public interface MediaLoadCallback {
        void onLoadProgress(int progress, String filePath);
        void onLoadFinished(int count, List<MediaInfoItem> dataSet);
    }

    Context context;
    String mediaType;
    Bitmap thumbnail;
    MediaLoadCallback callback;

    public MediaLoadTask(Context context, String mediaType, MediaLoadCallback callback) {
        this.context = context;
        this.mediaType = mediaType;
        this.callback = callback;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
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
            for (int i = count - 1; i >= 0; i--) {
                File file = list[i];
                MediaInfoItem item = MediaInfoItem.fromFile(file, mediaType);
                if (MediaManager.TYPE_AUDIO.equals(mediaType)) {
                    item.setThumbnail(thumbnail);
                }
                dataSet.add(item);
                if (callback != null) {
                    progress = (count - i) * 100 / count;
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
