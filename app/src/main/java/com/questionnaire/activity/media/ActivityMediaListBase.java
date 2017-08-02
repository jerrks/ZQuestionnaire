package com.questionnaire.activity.media;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterMedia;
import com.questionnaire.content.CameraManager;
import com.questionnaire.content.MediaLoadTask;
import com.questionnaire.content.MediaInfoItem;
import com.questionnaire.content.MediaManager;
import com.questionnaire.utils.CopyFileUtil;
import com.questionnaire.utils.FileUtil;
import com.questionnaire.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/7/27.
 */

public abstract class ActivityMediaListBase extends ActivityBase implements View.OnClickListener {

    public static final String TAG = MediaManager.TAG + ".MediaList";

    public static final int REQUEST_IMAGE_CODE = 0x01;
    public static final int REQUEST_VIDEO_CODE = 0x02;
    public static final int REQUEST_AUDIO_CODE = 0x03;

    public static final int MSG_WHAT_LOAD_PROGRESS = 0x01;
    public static final int MSG_WHAT_LOAD_FINISHED = 0x02;

    public static final int MENU_DELETE = 0x01;

    public ListView mListView;
    ProgressBar mProgressBar;
    public AdapterMedia mAdapter;
    public List<MediaInfoItem> mDataSet = new ArrayList<MediaInfoItem>();
    public int mLongClickPosition = 0;

    public Context mContaxt;
    String mDestFilePath;//新建文件目标路径

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_LOAD_PROGRESS:
                    int progress = msg.arg1;
                    mProgressBar.setProgress(progress);
                    break;
                case MSG_WHAT_LOAD_FINISHED:
                    List<MediaInfoItem> dataSet = (List<MediaInfoItem>) msg.obj;
                    updateDataSet(dataSet);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        mContaxt = getApplicationContext();
        initView();
        initListDataAsync();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initListDataAsync();
    }

    public void initView() {
        intHeaderViews();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_hor);
        //TextView bootumBtn = (TextView) findViewById(R.id.media_add_tv);
        //bootumBtn.setText(getBottomText());
        //bootumBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.media_list_view);
        mAdapter = new AdapterMedia(mContaxt, mDataSet);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MediaInfoItem item = mDataSet.get(position);
                String filePath = item.getFilePath();
                if (FileUtil.isFileExist(filePath)) {
                    //previewMedia(item);
                    MediaManager.previewFile(mContaxt, filePath);
                } else {
                    Log.e(TAG, "onItemClick: not exists file: " + filePath);
                    ToastUtils.show(mContaxt, getString(R.string.file_not_exists, filePath));
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                mLongClickPosition = position;
                showLongClickDialog(position);
                return true;
            }
        });
    }

    void showLongClickDialog(final int position) {
        new AlertDialog.Builder(ActivityMediaListBase.this)
                //.setTitle("对Item进行操作")
                .setItems(R.array.media_long_click_option,
                        new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int which){
                if(which == 0) {
                    deleteItem(position);
                }
            }
        }).setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int which){
            }
        }).show();
    }

    protected void deleteItem(int position) {
        if (position < mDataSet.size()) {
            MediaInfoItem mediaItem = mDataSet.remove(position);
            if (mediaItem == null) {
                Log.w(TAG, "Delete failed mediaItem is null: " + position);
                return;
            }
            String filePath = mediaItem.getFilePath();
            File file = new File(filePath);
            boolean deleted = file.delete();
            Log.w(TAG, "Delete mediaItem: " + filePath + ", deleted= " + deleted);
            if (deleted) {
                List<MediaInfoItem> dataSet = new ArrayList<MediaInfoItem>();
                dataSet.addAll(mDataSet);
                updateDataSetAsync(dataSet);
            }
        }
    }

    protected void intHeaderViews() {
        View v = findViewById(R.id.title_layout);
        TextView titleTv = (TextView) v.findViewById(R.id.title_center);
        titleTv.setText(getTitleText());

        Button leftBtn = (Button) v.findViewById(R.id.title_left);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);

        Button rightBtn = (Button) v.findViewById(R.id.title_right);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText(R.string.add);
        rightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                addMedia();
                break;
            //case R.id.media_add_tv:
            //   addMedia();
             //   break;
            default:
                break;
        }
    }

    /**
     * 异步遍历音频目录下的文件列表，显示在音频列表界面
     */
    protected void initListDataAsync() {
        String mediaType = getMediaType();
        final String dir = MediaManager.getMediaDir(mediaType);
        MediaLoadTask task = new MediaLoadTask(mContaxt, mediaType, new MediaLoadTask.MediaLoadCallback() {
            @Override
            public void onLoadProgress(int progress, String filePath) {
                //Log.i(TAG, "Loaded progress= " + progress + ", filePath: " + filePath);
                updateLoadProgress(progress);
            }

            @Override
            public void onLoadFinished(int count, List<MediaInfoItem> dataSet) {
                Log.i(TAG, "onLoadFinished count= " + count + ", from dir: " + dir);
                updateDataSetAsync(dataSet);
            }
        });
        task.setThumbnail(getItemThumbnail());
        task.execute(dir);
    }

    protected void updateLoadProgress(int progress) {
        Message msg = mHandler.obtainMessage(MSG_WHAT_LOAD_PROGRESS, progress, 0);
        msg.sendToTarget();
    }

    protected void updateDataSetAsync(List<MediaInfoItem> dataSet) {
        Message msg = mHandler.obtainMessage(MSG_WHAT_LOAD_FINISHED, 100, 0, dataSet);
        msg.sendToTarget();
    }

    protected void updateDataSet(List<MediaInfoItem> dataSet) {
        if (mAdapter == null) {
            mAdapter = new AdapterMedia(mContaxt, dataSet);
            mListView.setAdapter(mAdapter);
        }
        mDataSet.clear();
        if (dataSet != null && !dataSet.isEmpty()) {
            mDataSet.addAll(dataSet);
        }
        mAdapter.updateDataSet(dataSet);
        mAdapter.notifyDataSetChanged();
    }

    protected void startAddMedia(int requestCode, String destFilePath) {
        mDestFilePath = destFilePath;
        switch (requestCode) {
            case REQUEST_IMAGE_CODE:
                CameraManager.startCameraForImage(this, destFilePath, REQUEST_IMAGE_CODE);
                break;
            case REQUEST_VIDEO_CODE:
                CameraManager.startCameraForVideo(this, destFilePath, REQUEST_VIDEO_CODE);
                break;
            case REQUEST_AUDIO_CODE:
                //to do record audio
                Intent intent = new Intent(mContaxt, ActivityAudioRecord.class);
                intent.putExtra("filePath", destFilePath);
                startActivityForResult(intent, REQUEST_AUDIO_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: requestCode=" + requestCode + ",resultCode=" + resultCode + ", data=" + data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Log.i(TAG, "onActivityResult uri: " + uri);
                if (uri != null) {
                    if (TextUtils.isEmpty(mDestFilePath)) {
                        mDestFilePath = MediaManager.convertUriToFilePath(mContaxt, uri);
                        Log.i(TAG, "convertUriToFilePath uri: " + uri);
                    }
                    if (TextUtils.isEmpty(mDestFilePath) && FileUtil.isFileExist(mDestFilePath)) {
                        long count = CopyFileUtil.coryFileFromUri(mContaxt, uri, mDestFilePath);
                        Log.i(TAG, "onActivityResult copied filePath: " + mDestFilePath + " from " + uri + ", count= " + count);
                    }
                } else {
                    Log.w(TAG, "onActivityResult Intent.data is null !! ");
                }
            }
            initListDataAsync();
        }
    }

    protected String formateString(int resId, Object arg){
        return String.format(mContaxt.getString(resId), arg);
    }

    protected abstract String getMediaType();

    protected abstract String getTitleText();

    protected abstract void addMedia();

    protected String getBottomText() {
        return null;
    }

    protected Bitmap getItemThumbnail() {
        return null;
    }

    protected void previewMedia(MediaInfoItem item) {
    }
}
