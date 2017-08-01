package com.questionnaire.content;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
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
    public static final String TAG = "Naire";

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
            boolean mkDir = dir.mkdirs();
           if(!mkDir) Log.e(TAG, "Make dir failed: " + dir);
        }
        return dir.getAbsolutePath() + "/";
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

    public static void previewImage(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void previewVideo(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "video/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void previewAudio(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "audio/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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

    /**
     * 获取图片和视频的缩略图
     * @param filePath
     * @param mediaType  TYPE_IMAGE or TYPE_VIDEO
     * @return
     */
    public static Bitmap getThumbnail(String filePath, String mediaType) {
        if (!isFileExists(filePath)) {
            Log.e(TAG, mediaType + " >> get Thumbnail failed: file is not exists or not a file: " + filePath);
            return null;
        }
        Bitmap thumbnail = null;
        if (TYPE_IMAGE.equals(mediaType)) {
            thumbnail = extractImageThumbnail(filePath);
        } else if (TYPE_VIDEO.equals(mediaType)) {
            thumbnail = createVideoThumbnail(filePath);//获取最大的一帧
            //thumbnail = getVideoFrameAtTime(filePath);
        }
        Log.i(TAG, mediaType + " >> " + thumbnail + ", from file: " + filePath);
        return thumbnail;
    }

    public static Bitmap extractImageThumbnail(String filePath) {
        Bitmap source = BitmapFactory.decodeFile(filePath);
        if (source == null) {
            Log.e(TAG, "The Bitmap is null from: " + filePath);
            return null;
        }
        return ThumbnailUtils.extractThumbnail(source, 180, 180, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }

    /**
     * 获取视频缩略图：获取最大关键帧
     * @param filePath
     * @return
     */
    public static Bitmap createVideoThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
    }

    /**
     * 获取视频缩略图：第一个关键帧
     * @param filePath
     * @return
     */
    public static Bitmap getVideoFrameAtTime(String filePath) {
        return getVideoFrameAtTime(filePath, 0);// 获取第一个关键帧
    }

    /**
     * 获取视频缩略图：第timeUs个关键帧, timeUs表示第timeUs个关键帧
     * @param filePath
     * @param timeUs  w为0表示第一个关键帧
     * @return
     */
    public static Bitmap getVideoFrameAtTime(String filePath, long timeUs) {
        // 获取第timeUs个关键帧
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);
        return retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

    /**
     * 文件是否存在
     * @param filePath
     * @return
     */
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            Log.e(TAG, "getVideoFrameAtTime failed for invalid file: " + filePath);
            return true;
        }
        return false;
    }
}
