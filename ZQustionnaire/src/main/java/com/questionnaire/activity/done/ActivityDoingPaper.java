package com.questionnaire.activity.done;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterPaperDoing;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Tester;

public class ActivityDoingPaper extends ActivityBase 
	implements OnItemSelectedListener,OnClickListener{
	
	private ListView mListView;
	private View mHeader;
	private AdapterPaperDoing mAdapter;
	
	// data
	private Tester mTester;
	private Dialog mDialog;
	private Paper mPaper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doing);
		
		mPaper = (Paper) getIntent().getSerializableExtra(Conf.INTENT_DATA);
		if(mPaper == null){
			Conf.displayToast(this, 
					R.string.tip_data_is_invailde);
			finish();
		}else{
			init();
		}
	}
	
	private void init(){
		View back = findViewById(R.id.title_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		TextView tv = (TextView) findViewById(R.id.title_center);
		tv.setText(mPaper.getName());
		Button bt = (Button) findViewById(R.id.title_right);
		bt.setText(R.string.complete);
		bt.setVisibility(View.VISIBLE);
		bt.setOnClickListener(this);
		initTesterInformationView();
		initListView();
	}
	
	private void startDoingPaper(){
		if(mDialog == null)
			initTesterInformationView();
		if(!mDialog.isShowing()){
			mDialog.show();
		}
	}
	
	private void initTesterInformationView(){
		mTester = new Tester();
		View root = LayoutInflater.from(this)
			.inflate(R.layout.layout_personal_information, null,false);
		Spinner sp = (Spinner) root.findViewById(R.id.spinner_gender);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				R.layout.adapter_spinner_item_view,
				R.id.text_context, 
				getResources().getStringArray(R.array.gender));
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(this);
		
		root.findViewById(R.id.bt_cmplete).setOnClickListener(this);

		if(mDialog==null){
			mDialog = new Dialog(this,R.style.dialog);
			mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					//释放dialog时保存数据
					long id = Dao.getDaoTester().insert(mTester);
					if(id >-1){
						mAdapter.setTesterId(id);
						initListHeader();
					}else{
						Conf.displayToast(getBaseContext(),
								R.string.tip_save_subject_failed_retry);
					}
				}
			});
			mDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					//取消dialog. 关闭界面，不保存个人信息
					finish();
				}
			});
			//mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setContentView(root);
		}
		if(!mDialog.isShowing()) mDialog.show();
	}

	private void initListView(){
		mListView = (ListView) findViewById(R.id.paper_list_view);
		mAdapter = new AdapterPaperDoing(mListView,mPaper.getId());
		View root = LayoutInflater.from(this)
				.inflate(R.layout.layout_personal_information_header, null,false);
		mHeader = root.findViewById(R.id.header);
		mHeader.setVisibility(View.GONE);
		mListView.addHeaderView(root);
		
		mListView.setAdapter(mAdapter);
	}
	
	private void initListHeader(){
		TextView tv = (TextView) mHeader.findViewById(R.id.gender);
		String text = String.format(getString(R.string.label_gender), 
				getResources().getStringArray(R.array.gender)[mTester.getGender()]);
		tv.setText(text);
		
		tv = (TextView) mHeader.findViewById(R.id.age);
		text = String.format(getString(R.string.label_age), 
				mTester.getAge());
		tv.setText(text);
		
		tv = (TextView) mHeader.findViewById(R.id.nation);
		text = String.format(getString(R.string.label_nation), 
				mTester.getNation());
		tv.setText(text);
		
		tv = (TextView) mHeader.findViewById(R.id.profession);
		text = String.format(getString(R.string.label_profession), 
				mTester.getProfession());
		tv.setText(text);
		
		tv = (TextView) mHeader.findViewById(R.id.place);
		text = String.format(getString(R.string.label_place), 
				mTester.getPlace());
		tv.setText(text);
		
		tv = (TextView) mHeader.findViewById(R.id.contant);
		text = String.format(getString(R.string.label_contant), 
				mTester.getContant());
		tv.setText(text);
		mHeader.setVisibility(View.VISIBLE);
//		mFooter.setVisibility(View.VISIBLE);
	}
	
	private void onSaveTesterInformation(){
		EditText tv = (EditText) mDialog.findViewById(R.id.edit_age);
		String str = tv.getText().toString();
		if(TextUtils.isEmpty(str)){
			Conf.displayToast(this, 
				R.string.tip_complete_personal_information);
			return;
		}
		mTester.setAge(Integer.valueOf(str));
		
		tv = (EditText) mDialog.findViewById(R.id.edit_nation);
		str = tv.getText().toString();
		if(TextUtils.isEmpty(str)){
			Conf.displayToast(this, 
				R.string.tip_complete_personal_information);
			return;
		}
		mTester.setNation(str);
		
		tv = (EditText) mDialog.findViewById(R.id.edit_profession);
		str = tv.getText().toString();
		if(TextUtils.isEmpty(str)){
			Conf.displayToast(this, 
				R.string.tip_complete_personal_information);
			return;
		}
		mTester.setProfession(str);
		
		tv = (EditText) mDialog.findViewById(R.id.edit_place);
		str = tv.getText().toString();
		if(TextUtils.isEmpty(str)){
			Conf.displayToast(this, 
				R.string.tip_complete_personal_information);
			return;
		}
		mTester.setPlace(str);
		
		tv = (EditText) mDialog.findViewById(R.id.edit_contant);
		str = tv.getText().toString();
		if(TextUtils.isEmpty(str)){
			Conf.displayToast(this, 
				R.string.tip_complete_personal_information);
			return;
		}
		mTester.setContant(str);
		long id = Dao.getDaoTester().insert(mTester);
		if(id >-1){
			mAdapter.setTesterId(id);
			mDialog.dismiss();
			initListHeader();
		}else{
			Conf.displayToast(this, 
					R.string.tip_save_subject_failed_retry);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_cmplete:
			if( mDialog!=null && mDialog.isShowing())
				onSaveTesterInformation();
			break;
		case R.id.title_right:
			if(mAdapter.onTestComplete()){
				Conf.displayToast(this, 
					R.string.tip_thanks);
				finish();
			}
			break;
		case R.id.title_left:
			finish();
			break;
			
		default:
			break;
		}
	}

	public void onItemSelected(AdapterView<?> root, View v, int index,
			long id) {
		mTester.setGender(index);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	@Override
	protected void onResume() {
		startDoingPaper();
		super.onResume();
	}
}
