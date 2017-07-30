package com.questionnaire.content;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.questionnaire.utils.DateTimeUtil;

import java.io.File;

/**
 * 多媒体管理类
 * Created by zhanghao on 2017/7/27.
 */

public class MediaManager {
    public static final String TAG = "NaireMedia";

    public static final String ROOT_DIR = "/questionnaire/media/";

    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_AUDIO = "audio";

    public static boolean checkSDcardMounted() {
        String state = Environment.getExternalStorageState();
        return state != Environment.MEDIA_MOUNTED;
    }

    public static String getSDcardRootDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getMediaDir(String mediaType) {
        File dir = new File(getSDcardRootDir() + ROOT_DIR + mediaType + "/");
        if (!dir.exists()) {
            boolean made = dir.mkdirs();
           if(!made) Log.e(TAG, "Make dir failed: " + dir);
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取图片文件目录 fileName: ""IMG_yyyyMMdd_HHmmss"
     *
     * @return
     */
    public static String getImagePath() {
        return getMediaDir(TYPE_IMAGE) + getFileName(TYPE_IMAGE);
    }

    /**
     * 获取视频文件目录 fileName: ""IMG_yyyyMMdd_HHmmss"
     * @return
     */
    public static String getVideoPath() {
        return getMediaDir(TYPE_VIDEO) + getFileName(TYPE_VIDEO);
    }

    /**
     * 获取录音文件目录
     * @return
     */
    public static String getAudioPath() {
        return getMediaDir(TYPE_AUDIO) + getFileName(TYPE_AUDIO);
    }

    public static String getFileName(String mediaType) {
        String timeStr = DateTimeUtil.getFormatTime(System.currentTimeMillis());
        return mediaType + "_" + timeStr  + "." + getExtension(mediaType);
    }

    public static String getExtension(String mediaType) {
        if (TYPE_IMAGE.equals(mediaType)) {
            return "jpg";
        } else if (TYPE_VIDEO.equals(mediaType)) {
            return "3gp";//mp4 size  more
        } else if (TYPE_AUDIO.equals(mediaType)) {
            return "amr";//mp4 size  more
        }
        return "";
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
     * 由uri转化为系统文件路径
     * @param context
     * @param uri
     * @return
     */
    public static String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String filePath = null;
        if (scheme == null)
            filePath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            filePath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        filePath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return filePath;
    }
}
