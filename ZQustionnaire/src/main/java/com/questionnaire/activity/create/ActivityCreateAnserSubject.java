package com.questionnaire.activity.create;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityCreateAnserSubject extends Activity 
	implements OnClickListener{

	private EditText mEdit;
	private Dialog mDialog;
	private Subject mSubject;
	private int mPaperNumber;
	private long mPaperId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView(){
		setContentView(R.layout.activity_create_answer_subject);

		mEdit = (EditText) findViewById(R.id.subject_name);
		View title = findViewById(R.id.title);
		View v = title.findViewById(R.id.title_left);
		v.setVisibility(View.VISIBLE);
		title.findViewById(R.id.title_left).setOnClickListener(this);
		TextView tv = (TextView) title.findViewById(R.id.title_center);
		tv.setText(R.string.dilog_create_subject_title);
		Button bt = (Button) title.findViewById(R.id.title_right);
		bt.setText(R.string.complete);
		bt.setOnClickListener(this);
		bt.setVisibility(View.VISIBLE);
	}

	private void initData(){
		Intent i = getIntent();
		if(i.hasExtra(Conf.INTENT_DATA))
			mPaperId = i.getLongExtra(Conf.INTENT_DATA, -1);
		if(i.hasExtra(Conf.INTENT_EXTRA))
			mPaperNumber = i.getIntExtra(Conf.INTENT_EXTRA, -1);
		if(i.hasExtra(Conf.INTENT_SUBJECT)) 
			mSubject = (Subject) i.getSerializableExtra(Conf.INTENT_SUBJECT);
		if(mPaperId < 0 || mPaperNumber < 0){
			Conf.displayToast(this, "data is empty or invalide...");
			finish();
		}
		if(mSubject != null)
			mEdit.setText(mSubject.getTopic());
	}
	
	protected void saveSubject(){
		String topic = mEdit.getText().toString();
		if(TextUtils.isEmpty(topic)) return;
		if(mSubject == null){
			mSubject = new Subject();
			mSubject.setType(Subject.TYPE_ANSWER);
			mSubject.setPaperId(mPaperId);
			mSubject.setPaperNumber(mPaperNumber);
		}
		mSubject.setTopic(topic);
		long id = Dao.getDaoSubject().insert(mSubject);
		if(id > -1){
			Intent i = getIntent();
			i.putExtra(Conf.INTENT_DATA, mSubject);
			setResult(Conf.RESULT_CODE_DONE, i);
			finish();
		}else{
			Conf.displayToast(this, 
				R.string.tip_save_subject_failed_retry);
		}
	}
	
	
	private void showDaliog(){
		String topic = mEdit.getText().toString();
		if(TextUtils.isEmpty(topic)){ 
			finish();
			return;
		}else{
			if(mDialog == null){
				mDialog = new Dialog(this,R.style.dialog);
				View root = LayoutInflater.from(this)
					.inflate(R.layout.dialog_comfirm, null);
				TextView tv = (TextView) root.findViewById(R.id.dilog_title);
				tv.setText(R.string.tip_app);
				tv = (TextView) root.findViewById(R.id.dilog_message);
				tv.setText(String.format(getString(R.string.tip_save_subject), topic));
				Button bt = (Button) root.findViewById(R.id.dilog_left);
				bt.setText(R.string.cancel);
				bt.setOnClickListener(this);
				bt = (Button) root.findViewById(R.id.dilog_right);
				bt.setText(R.string.save);
				bt.setOnClickListener(this);
				mDialog.setContentView(root);
			}
			mDialog.show();
		}
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			showDaliog();
			break;
			
		case R.id.title_right:
		case R.id.dilog_right:
			saveSubject();
			break;
			
		case R.id.dilog_left:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			showDaliog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
