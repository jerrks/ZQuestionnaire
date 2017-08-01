package com.questionnaire.content;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * 图片、视频录制管理类
 * Created by hao on 2017/7/30.
 */

public class CameraManager {

    public static final String TAG = MediaManager.TAG + ".Camera";

    /**
     * 启动相机拍摄图片或者视频
     * <p>在Activity.onActivityResult()中可以接收到拍摄结束事件</p>
     * @param activity
     * @param action  MediaStore.ACTION_VIDEO_CAPTURE or MediaStore.ACTION_IMAGE_CAPTURE
     * @param filePath
     * @param requestCode
     */
    public static void startCamera(Activity activity, String action,  String filePath, int requestCode) {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件目录
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result) {
                Log.e(TAG, "Make dir failed: " + dir);
                return;
            }
        }
        if (file.exists()) {
            boolean deleted =  file.delete();
            Log.w(TAG, file + " is deleted: " + deleted);
        }
        // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        Log.i(TAG, "startCamera action: " + action + ":\n" + filePath + " >> " + uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startCameraForVideo(Activity activity, String filePath, int requestCode) {
        startCamera(activity, MediaStore.ACTION_VIDEO_CAPTURE, filePath, requestCode);
    }

    public static void startCameraForImage(Activity activity, String filePath, int requestCode) {
        startCamera(activity, MediaStore.ACTION_IMAGE_CAPTURE, filePath, requestCode);
    }

    /**
     * 打开相机，不指定文件路径，保存在gallery中
     * 在Activity.onActivityResult()中
     *  Uri uri = data.getData();
     *  String filePath = MediaManager.getAbsolutePath(context, uri)</p>
     * @param activity
     * @param requestCode
     */
    public static void startCameraInGallery(Activity activity, int requestCode) {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     *  由系统相册中获取已经存在图片
     *  <p>在Activity.onActivityResult()中
     *  Uri uri = data.getData();
     *  String filePath = MediaManager.getAbsolutePath(context, uri)</p>
     * @param activity
     * @param requestCode
     */
    public static void pickImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, requestCode);
    }
}
