package com.questionnaire.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.util.Log;

import com.questionnaire.content.MediaManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 复制文件或文件夹
 * <p>
 * zww
 */
public class CopyFileUtil {

    private static String TAG = MediaManager.TAG + ".CopyFile";

    /**
     * 复制单个文件
     *
     * @param srcFilePath  待复制的文件名
     * @param destFilePath 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFilePath, String destFilePath, boolean overlay) {
        File srcFile = new File(srcFilePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            Log.w(TAG, "Copy file failed, " + srcFilePath + " not exists !! ");
            return false;
        } else if (!srcFile.isFile()) {
            Log.w(TAG, "Copy file failed, srcFileName: " + srcFilePath + " is not a file !! ");
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFilePath);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                boolean deleted = new File(destFilePath).delete();
                Log.w(TAG, "destFile: " + destFile + " is deleted= " + deleted);
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            File destParent = destFile.getParentFile();
            if (!destFile.exists()) {
                // 目标文件所在目录不存在
                if (!destFile.mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    Log.w(TAG, "Copy file failed, parent mkdirs failed: " + destParent);
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Copy file failed: " + e, e);
        } catch (IOException e) {
            Log.e(TAG, "Copy file failed: " + e, e);
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  待复制目录的目录名
     * @param destDirName 目标目录名
     * @param overlay     如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String destDirName, boolean overlay) {
        // 判断源目录是否存在
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            Log.w(TAG, "Copy dir failed, srcDir: " + srcDir + " not exists !! ");
            return false;
        } else if (!srcDir.isDirectory()) {
            Log.w(TAG, "Copy dir failed, srcDir: " + srcDir + " is not a dir !! ");
            return false;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录
            if (overlay) {
                new File(destDirName).delete();
            } else {
                Log.w(TAG, "Copy dir failed, destDir: " + destDirName + " is existed !! ");
                return false;
            }
        } else {
            // 创建目的目录
            if (!destDir.mkdirs()) {
                Log.w(TAG, "Copy dir failed, destDir: " + destDirName + " create failed !! ");
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件
            if (files[i].isFile()) {
                flag = CopyFileUtil.copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = CopyFileUtil.copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            Log.e(TAG, "Copy dir from " + srcDirName + " to " + destDirName + " failed ！");
            return false;
        } else {
            return true;
        }
    }

    public static long coryFileFromUri(Context context, Uri uri, String destFilePath) {
        try {
            AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            FileInputStream fis = videoAsset.createInputStream();
            FileOutputStream fos = new FileOutputStream(new File(destFilePath));
            byte[] buf = new byte[1024];
            long count = 0;
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
                count += len;
            }
            fis.close();
            fos.close();
            Log.i(TAG, "saveFileFromUri success from " + uri + " to " + destFilePath + ", size count= " + count);
            return count;
        } catch (IOException e) {
            Log.e(TAG, "saveFileFromUri: " + e, e);
        }
        return 0;
    }
}