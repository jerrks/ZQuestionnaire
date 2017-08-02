package com.questionnaire.content;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * 录音管理类
 * Created by hao on 2017/7/30.
 */

public class RecordManager {
    public static final String TAG = MediaManager.TAG + ".Record";

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
