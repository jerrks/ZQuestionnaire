package com.questionnaire.activity.more;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.questionnaire.QuestApp;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;

public class ActivityAbout extends ActivityBase {

	private static final String TAG = "ActivityAbout";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initRes();
	}
	
	void initRes() {
		TextView headText = (TextView) findViewById(R.id.title_center);
		headText.setText(R.string.about_app);
		TextView ver = (TextView) findViewById(R.id.version_info);
		String version_info = String.format(getString(R.string.version_info), QuestApp.getApp().getVersion());
		Log.d(TAG, "version_info == " + version_info);
		ver.setText(version_info);
	}
}
