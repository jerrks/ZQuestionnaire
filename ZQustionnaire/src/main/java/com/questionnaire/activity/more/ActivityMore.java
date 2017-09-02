package com.questionnaire.activity.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.view.OptionItem;

public class ActivityMore extends ActivityBase implements OnClickListener {

	OptionItem mIntr, mUpdate, mAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		initRes();
	}

	void initRes() {
		TextView headText = (TextView) findViewById(R.id.title_center);
		headText.setText(R.string.more);
		mIntr = (OptionItem) findViewById(R.id.intr);
		mUpdate = (OptionItem) findViewById(R.id.update);
		mAbout = (OptionItem) findViewById(R.id.about);
		mIntr.setText(getString(R.string.intr_app));
		mAbout.setText(getString(R.string.about));
		mUpdate.setText(getString(R.string.check_update));
		mIntr.setOnClickListener(this);
		mAbout.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.intr:
			startActivity(new Intent(ActivityMore.this, ActivityIntr.class));
			break;
		case R.id.update:
			startActivity(new Intent(ActivityMore.this, ActivityUpdate.class));
			break;
		case R.id.about:
			startActivity(new Intent(ActivityMore.this, ActivityAbout.class));
			break;

		default:
			break;
		}
	}
}
