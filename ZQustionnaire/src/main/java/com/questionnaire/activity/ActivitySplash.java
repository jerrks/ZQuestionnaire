package com.questionnaire.activity;


import com.questionnaire.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ActivitySplash extends Activity 
	implements Runnable{
	
	private static final long LAUNCH_DELAY_TIME = 1 * 1000;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(this)
        		.inflate(R.layout.activity_splash, null, false);
        v.postDelayed(this, LAUNCH_DELAY_TIME);
        setContentView(v);
    }

	public void run() {
		Intent i = new Intent(this, ActivityMain.class);
		startActivity(i);
		finish();
	}
}