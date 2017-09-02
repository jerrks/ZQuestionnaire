package com.questionnaire.activity;

import android.app.Activity;

public abstract class ActivityBase extends Activity {

	@Override
	protected void onResume() {
		super.onResume();
		refreshList();
	}
	
	/**
	 * refresh list
	 */
	public void refreshList() {
	}
}
