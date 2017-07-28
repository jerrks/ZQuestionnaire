package com.questionnaire.content;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhanghao on 2017/7/27.
 */

public class MediaManager {
    public static final String TAG = "MediaManager";

    public static String getMediaDir(String mediaType) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/questionnaire/" + mediaType + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取图片文件目录
     * @param fileName
     * @return
     */
    public static String getImagePath(String fileName) {
        return getMediaDir("images") + "/" + fileName + ".amr";
    }



    /**
     * 调用系统录音机，但是据说不能保存到指定目录，而是直接保存在系统录音机目录下
     *
     * @param activity
     * @param filePath
     */
    public static void callSystemRecordAudio(Activity activity, String filePath) {
        File imageFile = new File(filePath);
        Uri imageFileUri = Uri.fromFile(imageFile);
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 保存音频文件在指定目录下
     *
     * @param filePath
     */
    public static void startRecordAudio(String filePath) {

        MediaRecorder recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        recorder.setOutputFile(filePath);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {

            recorder.prepare();

        } catch (IOException e) {
            Log.e(TAG, "startRecordAudio: " + e, e);
        }
        recorder.start();
    }
}
