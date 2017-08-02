package com.questionnaire.activity.media;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.content.MediaManager;
import com.questionnaire.utils.DateTimeUtil;
import com.questionnaire.utils.ToastUtils;

/**
 * Created by zhanghao on 2017/8/2.
 */
public class ActivityAudioRecord extends ActivityBase implements View.OnClickListener {
    public static final String TAG = MediaManager.TAG + ".AudioRecord";

    private Context mContext;
    private Button mRecordBtn;
    private Button mStopBtn;
    private Button mPlayBtn;
    private Button mDeleteBtn;

    private ListView mListView;
    private TextView mInfoTv;

    private File myRecAudioFile;
    private File myRecAudioDir;

    private MediaRecorder mMediaRecorder01;
    private ArrayList<String> mRecordFileList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;// 用于ListView的适配器
    private boolean sdCardExit;
    private boolean isStopRecord;
    private MyTimer mMyTimer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.audio_create_activity);
        intHeaderViews();
        //主要是4个控制按钮(录制,停止,播放,删除)
        mRecordBtn = (Button) findViewById(R.id.record_btn);
        mStopBtn = (Button) findViewById(R.id.stop_btn);
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mDeleteBtn = (Button) findViewById(R.id.delete_btn);
        //列表出指定文件夹中所有amr格式音频文件
        mListView = (ListView) findViewById(R.id.audio_list_view);
        mInfoTv = (TextView) findViewById(R.id.info_tv);

        mStopBtn.setEnabled(false);
        mPlayBtn.setEnabled(false);
        mDeleteBtn.setEnabled(false);
        String filePath = getIntent().getStringExtra("filePath");
        Log.i(TAG, "Received filePath: " + filePath);
        myRecAudioFile = new File(filePath);

        sdCardExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (sdCardExit) {
            myRecAudioDir = myRecAudioFile.getParentFile();
        }

        loadRecordFiles();

        adapter = new ArrayAdapter<String>(this, R.layout.audio_record_item, mRecordFileList);

        mListView.setAdapter(adapter);

        mRecordBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        /* 当有点击档名时将删除及播放按钮Enable */
                        mPlayBtn.setEnabled(true);
                        mDeleteBtn.setEnabled(true);
                        String fileName = mRecordFileList.get(position);
                        String filePath = myRecAudioDir.getAbsolutePath() + "/" + fileName;
                        File file = new File(filePath);
                        if (file.exists()) {
                            MediaManager.previewFile(mContext, filePath);;
                        }
                    }
                });
    }

    protected void intHeaderViews() {
        View v = findViewById(R.id.title_layout);
        TextView titleTv = (TextView) v.findViewById(R.id.title_center);
        titleTv.setText(R.string.record_audio);

        Button leftBtn = (Button) v.findViewById(R.id.title_left);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.play_btn:
                play();
                break;
            case R.id.record_btn:
                startRecord();
                break;
            case R.id.stop_btn:
                stopRecord();
                break;
            case R.id.delete_btn:
                delete();
                break;
            default:
                break;
        }
    }

    void startRecord() {
        if (!sdCardExit) {
            ToastUtils.show(ActivityAudioRecord.this, R.string.sdcard_unmounted);
            return;
        }
        try {
            stopMediaRecorder();//release at first
            mMediaRecorder01 = new MediaRecorder();
            mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);/* 设置录音来源为麦克风 */
            mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);//可用DEFAULT
            mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);//可用DEFAULT
            mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());//文件保存位置

            mMediaRecorder01.prepare();
            mMediaRecorder01.start();

            mInfoTv.setText(getString(R.string.audio_recording, "......"));
            startTimer();//计时

            mRecordBtn.setEnabled(false);
            mStopBtn.setEnabled(true);
            mPlayBtn.setEnabled(false);
            mDeleteBtn.setEnabled(false);

            isStopRecord = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stopMediaRecorder() {
        if (mMediaRecorder01 != null) {
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
    }

    void stopRecord() {
        if (myRecAudioFile != null) {
            stopMediaRecorder();
            stopTimerIfNeed();
            //update UI
            adapter.add(myRecAudioFile.getName());
            mInfoTv.setText(getString(R.string.audio_record_stop, myRecAudioFile.getName()));
            mStopBtn.setEnabled(false);
            mPlayBtn.setEnabled(true);
            mDeleteBtn.setEnabled(true);
            mRecordBtn.setEnabled(true);

            isStopRecord = true;
        }
    }

    void play() {
        if(myRecAudioFile.exists()) {
            MediaManager.previewFile(mContext, myRecAudioFile.getAbsolutePath());
        }
    }

    void delete() {
        if (myRecAudioFile != null) {
            String fileName = myRecAudioFile.getName();
            adapter.remove(fileName);//update listview
            if (myRecAudioFile.exists()) {
                myRecAudioFile.delete();
                mInfoTv.setText(getString(R.string.file_deleted, fileName));
            }
        }
        mStopBtn.setEnabled(true);
        mPlayBtn.setEnabled(true);
        mDeleteBtn.setEnabled(true);
    }

    void startTimer() {
        stopTimerIfNeed();
        if (mMyTimer == null) {
            mMyTimer = new MyTimer(mContext);
        }
        mMyTimer.startTimer(System.currentTimeMillis());
    }

    void stopTimerIfNeed() {
        if (mMyTimer != null) {
            mMyTimer.stopTimer();
        }
    }

    @Override
    protected void onStop() {
        if (mMediaRecorder01 != null && !isStopRecord) {
            mMediaRecorder01.stop();
            mMediaRecorder01.release();
            mMediaRecorder01 = null;
        }
        stopTimerIfNeed();
        super.onStop();
    }

    // 存储一个音频文件数组到list当中
    private void loadRecordFiles() {
        mRecordFileList.clear();
        if (sdCardExit) {
            File files[] = myRecAudioDir.listFiles();
            if (files != null) {
                for (int i = files.length -1; i >= 0; i--) {
                    if (files[i].getName().indexOf(".") >= 0) {
                        String fileS = files[i].getName().substring(
                                files[i].getName().indexOf("."));
                        if (fileS.toLowerCase().equals(".amr"))
                            mRecordFileList.add(files[i].getName());
                    }
                }
            }
        }
    }

    private class MyTimer {
        public static final int COUNT_TIME_CODE = 0x1;
        private static final int UPDATE_INTERVAL = 1 * 1000;//倒计时间隔时间
        private Timer mTimer = new Timer();
        private TimerTask mTimerTask = null;
        private long mStartTime = 0;
        private long mTimeDelta = 0;
        private Handler mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case COUNT_TIME_CODE:
                        mTimeDelta = System.currentTimeMillis() - mStartTime;
                        long seconds = mTimeDelta / UPDATE_INTERVAL;
                        String timeText = DateTimeUtil.formatIntervalSeconds(seconds);
                        mInfoTv.setText(mContext.getString(R.string.audio_recording, timeText));
                        break;
                }
            }
        };

        Context mContext;

        public MyTimer(Context context) {
            mContext =  context;
        }

        public void startTimer(long startTime) {
            Log.i(TAG, "startTimer: " + new java.util.Date(startTime).toLocaleString());
            mStartTime = startTime;
            if (mTimerTask == null) {
                if (mTimerTask == null) {
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(COUNT_TIME_CODE);
                        }
                    };
                }
            }
            if (mTimer == null) {
                mTimer = new Timer();
            }
            mTimer.schedule(mTimerTask, 0, UPDATE_INTERVAL);
        }

        public void stopTimer() {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
                Log.i(TAG, "startTimer: " + new java.util.Date(System.currentTimeMillis()).toLocaleString() + ", mTimeDelta= " + mTimeDelta);
            }
            mStartTime = System.currentTimeMillis();
        }
    }
}


