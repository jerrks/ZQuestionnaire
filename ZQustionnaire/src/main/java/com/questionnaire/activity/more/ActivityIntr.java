package com.questionnaire.activity.more;

import android.os.Bundle;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;

public class ActivityIntr extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intr);
		initRes();
	}
	
	void initRes() {
		TextView headText = (TextView) findViewById(R.id.title_center);
		headText.setText(R.string.intr_app);
	}
}
