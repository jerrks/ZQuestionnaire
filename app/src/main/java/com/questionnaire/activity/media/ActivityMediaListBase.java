package com.questionnaire.activity.media;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterMedia;
import com.questionnaire.content.LoadDataTask;
import com.questionnaire.content.MediaInfoItem;
import com.questionnaire.content.MediaManager;
import com.questionnaire.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/7/27.
 */

public abstract class ActivityMediaListBase extends ActivityBase implements View.OnClickListener {

    public static final String TAG = MediaManager.TAG + ".Activity";

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
    public int mLongClickItemPosition = 0;

    public Context mContaxt;

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
        TextView bootumBtn = (TextView) findViewById(R.id.media_add_tv);
        bootumBtn.setText(getBottomText());
        bootumBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.media_list_view);
        mAdapter = new AdapterMedia(mContaxt, mDataSet);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MediaInfoItem item = mDataSet.get(position);
                String filePath = item.getFilePath();
                if (MediaManager.isFileExists(filePath)) {
                    performItemClick(item);
                } else {
                    Log.e(TAG, "onItemClick failed, not exists file: " + filePath);
                    ToastUtils.show(mContaxt, formateString(R.string.file_not_exists, filePath));
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                mLongClickItemPosition = position;
                showLongClickDialog(position);
                return true;
            }
        });
        /*mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
                final AdapterView.AdapterContextMenuInfo info;
                try {
                    info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                } catch (ClassCastException e) {
                    Log.e(TAG, "CreateContextMenu bad menuInfo: " + menuInfo);
                    return;
                }
                mLongClickItemPosition = info.position;
                menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, R.string.delete);
            }
        });*/
    }

    /*@Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                deleteItem(mLongClickItemPosition);
                break;
            default:
                break;
        }
        return true;
    }*/

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
            case R.id.media_add_tv:
                addMedia();
                break;
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
        LoadDataTask task = new LoadDataTask(mediaType, new LoadDataTask.LoadDataCallback() {
            @Override
            public void onLoadProgress(int progress, String filePath) {
                Log.i(TAG, "Loaded progress= " + progress + ", filePath: " + filePath);
                updateLoadProgress(progress);
            }

            @Override
            public void onLoadFinished(int count, List<MediaInfoItem> dataSet) {
                Log.i(TAG, "onLoadFinished count= " + count + ", from dir: " + dir);
                updateDataSetAsync(dataSet);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initListDataAsync();
        }
    }

    protected String formateString(int resId, Object arg){
        return String.format(mContaxt.getString(resId), arg);

    }

    protected  abstract String getMediaType();

    protected  abstract String getTitleText();

    protected  abstract String getBottomText();

    /**
     * 开始录制音频文件，保存在自己的目录下
     */
    protected void addMedia() {
    }

    protected void performItemClick(MediaInfoItem item) {
    }
}
