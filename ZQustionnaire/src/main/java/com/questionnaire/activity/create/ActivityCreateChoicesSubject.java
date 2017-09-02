package com.questionnaire.activity.create;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.view.SubjectOptionInputView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityCreateChoicesSubject extends Activity
	implements OnClickListener{
	private EditText mSubjectName;
	ViewGroup mOptionsContainer;
	View mOptionController;

	private Dialog mDialog;
	private Subject mSubject;
	int mType;
	private int mPaperNumber;
	private long mPaperId;
	int mMinHeight;

	String[] mOptionLabels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(initData()){
			initView();
		}
	}
	
	private void initView(){
		
		setContentView(R.layout.activity_create_choices_subject);
		
		mSubjectName = (EditText) findViewById(R.id.subject_name);
		View title = findViewById(R.id.title);
		View v = title.findViewById(R.id.title_left);
		v.setVisibility(View.VISIBLE);
		title.findViewById(R.id.title_left).setOnClickListener(this);
		TextView tv = (TextView) title.findViewById(R.id.title_center);
		String value;
		if(mType== Subject.TYPE_CHOICE_SINGLE) value = "创建单选题";
		else if(mType== Subject.TYPE_CHOICE_MUTILPE) value = "创建多选题";
		else value = "创建排序题" ;
		tv.setText(value);
		Button bt = (Button) title.findViewById(R.id.title_right);
		bt.setText(R.string.complete);
		bt.setOnClickListener(this);
		bt.setVisibility(View.VISIBLE);

		mOptionsContainer = (ViewGroup)findViewById(R.id.subject_options_container);
		mOptionController = findViewById(R.id.subject_option_control);

		mOptionController.findViewById(R.id.subject_add_option).setOnClickListener(this);
		mOptionController.findViewById(R.id.subject_delete_option).setOnClickListener(this);

		mOptionLabels = getResources().getStringArray(R.array.subject_label);
		mMinHeight = getResources().getDimensionPixelSize(R.dimen.paper_add_subject_heigth);

		String[] options = null;
		if(mSubject != null) options = mSubject.getOptions();

		SubjectOptionInputView inputView;

		int size;
		if(mType==Subject.TYPE_CHOICE_SINGLE) size = 4;
		else if(mType==Subject.TYPE_CHOICE_MUTILPE) size = 4;
		else size = 5;
		int length;

		for(int k=0; k<size; k++){
			length = mOptionsContainer.getChildCount();
			if(length>k){
				inputView = (SubjectOptionInputView) mOptionsContainer.getChildAt(k);
			}else{
				inputView = new SubjectOptionInputView(this);
				inputView.setMinimumHeight(mMinHeight);
			}
			inputView.getOptionLabelView().setText(mOptionLabels[k]);
			inputView.getOptionInputView().setHint(R.string.hit_subject_value);
			if(mSubject != null && k < options.length){
				inputView.getOptionInputView().setText(options[k]);
			}
		}
	}

	private boolean initData(){
		Intent i = getIntent();
		if(i.hasExtra(Conf.INTENT_DATA))
			mPaperId = i.getLongExtra(Conf.INTENT_DATA, -1);
		if(i.hasExtra(Conf.INTENT_EXTRA))
			mPaperNumber = i.getIntExtra(Conf.INTENT_EXTRA, -1);
		if(i.hasExtra(Conf.INTENT_SUBJECT))
			mSubject = (Subject) i.getSerializableExtra(Conf.INTENT_SUBJECT);
		if(mSubject!=null)
			mType = mSubject.getType();
		else if(i.hasExtra(Conf.INTENT_TYPE))
			mType = i.getIntExtra(Conf.INTENT_TYPE,Subject.TYPE_CHOICE_SINGLE);
		if(mPaperId < 0 || mPaperNumber < 0){
			Conf.displayToast(this, "data is empty or invalide...");
			finish();
			return false;
		}else{
			return true;
		}
	}
	
	private void showDaliog(){
		String topic = mSubjectName.getText().toString();
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

	void editOptionsView(boolean delete){
		final int type = mType;
		int min = type==Subject.TYPE_CHOICE_MUTILPE ? 3 : (type==Subject.TYPE_SORT ? 5 : 2);
		int max = type==Subject.TYPE_CHOICE_MUTILPE || type==Subject.TYPE_SORT ? 7 : 5;
		int count = mOptionsContainer.getChildCount();
		if(delete) count --;
		else count ++;
		if(count>=min && count<=max){
			if(delete){
				mOptionsContainer.removeViewAt(count);
			}else{
				SubjectOptionInputView inputView = new SubjectOptionInputView(this);
				inputView.setMinimumHeight(mMinHeight);
				inputView.getOptionLabelView().setText(mOptionLabels[count]);
				inputView.getOptionInputView().setHint(R.string.hit_subject_value);
				mOptionsContainer.addView(inputView);
			}
		}else{
			String error;
			if(delete) error = "最小答案选项是 " + min + " 个！";
			else error = "最大答案选项是 " + max + " 个！";
			Conf.displayToast(this, error);
		}
	}
	
	protected void saveSubject(){
		String topic = mSubjectName.getText().toString();
		if(TextUtils.isEmpty(topic)){
			mSubjectName.requestFocus();
			Conf.displayToast(this, "请输入题目!");
			return;
		}
		int length = mOptionsContainer.getChildCount();
		String[] options = new String[length];
		SubjectOptionInputView inputView;
		String str;
		for(int i=0; i<length; i++){
			inputView = (SubjectOptionInputView) mOptionsContainer.getChildAt(i);
			str = inputView.getOptionInputView().getText().toString();
			if(!TextUtils.isEmpty(str)){
				options[i] = str;
			}else{
				Conf.displayToast(this, "请输入备选答案!");
				inputView.getOptionInputView().requestFocus();
				return;
			}
		}

		if(mSubject == null){
			mSubject = new Subject();
			mSubject.setType(mType);
			mSubject.setPaperId(mPaperId);
			mSubject.setPaperNumber(mPaperNumber);
		}
		mSubject.setTopic(topic);
		mSubject.setOptions(options);
		long id = Dao.getDaoSubject().insert(mSubject);
		if(id > -1){
			Intent i = getIntent();
			mSubject.setId(id);
			i.putExtra(Conf.INTENT_DATA, mSubject);
			setResult(Conf.RESULT_CODE_DONE, i);
			finish();
		}else{
			Conf.displayToast(this, 
				R.string.tip_save_subject_failed_retry);
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

		case R.id.subject_add_option:
			editOptionsView(false);
			break;
		case R.id.subject_delete_option:
			editOptionsView(true);
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
