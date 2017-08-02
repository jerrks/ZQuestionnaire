package com.questionnaire;

import com.questionnaire.db.impl.Dao;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class QuestApp extends Application {


	private static final String TAG = "QuestApp";
	static QuestApp sQuestApp = null;
	@Override
	public void onCreate() {
		super.onCreate();
		sQuestApp = this;
		Dao.init(this);
	}

	public static QuestApp getApp() {
		return sQuestApp;
	}
	
	public static Context getContext() {
		return sQuestApp.getApplicationContext();
	}

	@Override
	public void onTerminate() {
		Dao.onDestory();
		super.onTerminate();
	}
 
	public String getVersion() {
		String version = "1.0";
		PackageManager packageManager = sQuestApp.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(sQuestApp.getPackageName(),0);
			if(packInfo != null){
				version = packInfo.versionName;
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "getVersion NameNotFoundException: " + e);
			e.printStackTrace();
		}
        return version;
    }
}
