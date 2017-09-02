package com.questionnaire.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.questionnaire.Conf;

import android.content.Context;
import android.util.Log;

public class FileUtil {

	private static final String TAG = null;

	/**
	 * copy file to destination path from assets dir
	 */
	public static void copyFileFromAssets(Context context, String destPath, String fileName) throws IOException {

		// if no the path, create it.
		File dir_file = new File(destPath);
		if (!dir_file.exists()) {
			dir_file.mkdir();
		}

		InputStream input = context.getAssets().open(fileName);

		String outFileName = destPath + fileName;
		OutputStream output = new FileOutputStream(outFileName);

		//read src path to dest path
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}

		// Close the streams
		output.flush();
		output.close();
		input.close();

		if(Conf.DEBUG)
			Log.d(TAG, "copydb success DB_PATH : " + destPath + fileName);
	}
	
	/**
	 * check path  Exist 
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkExist(String path) {

		File f = new File(path);
		if (f!=null && f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			Log.w(TAG, "Not exists real file: " + filePath);
			return false;
		}
		return true;
	}

	/**
	 * 文件夹是否存在
	 * @param dir
	 * @return
	 */
	public static boolean isDirExist(String dir) {
		File file = new File(dir);
		if (!file.exists() || file.isFile()) {
			Log.w(TAG, "Not exists dir: " + dir);
			return false;
		}
		return true;
	}
}
