package com.questionnaire.activity.more;

import android.os.Bundle;
import android.widget.TextView;

import com.questionnaire.QuestApp;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;

public class ActivityUpdate extends ActivityBase {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		initRes();
	}
	
	void initRes() {
		TextView headText = (TextView) findViewById(R.id.title_center);
		headText.setText(R.string.check_update);
		TextView version_info = (TextView) findViewById(R.id.version_info);
		String text = String.format(getString(R.string.current_ver), QuestApp.getApp().getVersion());
		version_info.setText(text);
	}
}
