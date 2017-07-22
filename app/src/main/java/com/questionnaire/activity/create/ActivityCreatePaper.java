package com.questionnaire.activity.create;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterSubject;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Subject;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.listener.OnModifySubjectListener;

public class ActivityCreatePaper extends ActivityBase 
	implements OnClickListener,OnModifySubjectListener{
	
	private ListView mListView;
	private View mHeader;
	private TextView mTitle;
	private AdapterSubject mAdapter;
	
	// data 
	private Paper mPaper;
	private Dialog mDialog;
	private int mSubjectNumber;
	private boolean isEditSubject = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_paper);
		initView();
	}
	
	private void initView(){
		
		View v = findViewById(R.id.title);
		mTitle = (TextView) v.findViewById(R.id.title_center);
		
		Button bt = (Button) v.findViewById(R.id.title_left);
		bt.setVisibility(View.VISIBLE);
		//bt.setText(R.string.cancel);
		bt.setTag(ActivityCreate.TITLE_CANCEL);
		bt.setOnClickListener(this);
		
		bt = (Button) v.findViewById(R.id.title_right);
		bt.setVisibility(View.VISIBLE);
		bt.setText(R.string.complete);
		bt.setTag(ActivityCreate.TITLE_COMPLETE);
		bt.setOnClickListener(this);
		
		findViewById(R.id.paper_add_subject).setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.paper_list_view);
		
		View header = LayoutInflater.from(this)
				.inflate(R.layout.paper_header, null, false);
		mHeader = header.findViewById(R.id.header);
		mListView.addHeaderView(header);
		mHeader.setVisibility(View.GONE);
		mAdapter = new AdapterSubject(this, this);
		mListView.setAdapter(mAdapter);
		mAdapter.setEditable(true);
		
		long paperId = getIntent().getLongExtra(Conf.INTENT_DATA,-1);
		if(paperId != -1){
			mPaper = Dao.getDaoPaper().get(paperId);
			if(mPaper == null){
				android.util.Log.e("TAG", "== error: data is null or empty ==");
				finish();
				return;
			}
		}
		
		if(mPaper != null){
			initHeader();
			mAdapter.setDataSet(Dao.getDaoSubject().getAll(paperId));
		}else{
			mAdapter.setDataSet(new ArrayList<Subject>());
			createPaper();
		}
	}
	
	protected void createPaper(){
		mDialog = new Dialog(this,R.style.dialog);
		View root = LayoutInflater.from(this)
				.inflate(R.layout.paper_create_dilog, null);
		TextView tv = (TextView) root.findViewById(R.id.dilog_title);
		tv.setText(R.string.dilog_create_paper_title);
		
		View v = null;
		int[] items = {
				R.id.dilog_item_1,
				R.id.dilog_item_2,
				R.id.dilog_item_3,
				R.id.dilog_item_4};
		String[] titles = getResources()
				.getStringArray(R.array.dilog_create_paper);
		String[] hits = getResources()
				.getStringArray(R.array.dilog_create_paper_hit);
		EditText et = null;
		for(int i=0; i<items.length; i++){
			v = root.findViewById(items[i]);
			tv = (TextView) v.findViewById(R.id.paper_left);
			tv.setText(titles[i]);
			et = (EditText) v.findViewById(R.id.paper_edit_text);
			et.setHint(hits[i]);
		}
		
		root.findViewById(R.id.dilog_bt_left).setOnClickListener(this);
		root.findViewById(R.id.dilog_bt_right).setOnClickListener(this);
		mDialog.setContentView(root);
		mDialog.show();
	}
	
	
	
	protected void createSubjectForPaper(){
		if(mPaper == null){
			createPaper();
		}else{
			isEditSubject = false;
			mDialog = new Dialog(this,R.style.dialog);
			View root = LayoutInflater.from(this)
					.inflate(R.layout.paper_create_subject_choice_typt_dilog, null);
			ListView list = (ListView) root.findViewById(R.id.subject_list_view);
			list.setAdapter(new ArrayAdapter<String>(this, 
					R.layout.adapter_subject_choice_item_view, 
					R.id.adapter_subject, 
					getResources().getStringArray(R.array.dilog_subject_type)));
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapter, View v,
						int index, long id) {
					onModifySubject(null, index);
				}
			});
			mDialog.setContentView(root);
			mDialog.show();
		}
	}
	
	private void onModifySubject(Subject subj, int type){

		Intent i = new Intent();
		switch (type) {
		case Subject.TYPE_CHOICE_SINGLE:
			i.setClass(ActivityCreatePaper.this,ActivityCreateSigleSubject.class);
			if(subj != null) i.putExtra(Conf.INTENT_SUBJECT, subj);
			i.putExtra(Conf.INTENT_DATA, mPaper.getId());
			i.putExtra(Conf.INTENT_EXTRA, mSubjectNumber);
			startActivityForResult(i, Conf.REQEST_CODE_CREATE);
			break;
			
		case Subject.TYPE_CHOICE_MUTILPE:
			i.setClass(ActivityCreatePaper.this,ActivityCreateMultipleSubject.class);
			if(subj != null) i.putExtra(Conf.INTENT_SUBJECT, subj);
			i.putExtra(Conf.INTENT_DATA, mPaper.getId());
			i.putExtra(Conf.INTENT_EXTRA, mSubjectNumber);
			i.putExtra(Conf.INTENT_TYPE, Subject.TYPE_CHOICE_MUTILPE);
			startActivityForResult(i, Conf.REQEST_CODE_CREATE);
			break;
			
		case Subject.TYPE_SORT:
			i.setClass(ActivityCreatePaper.this,ActivityCreateMultipleSubject.class);
			if(subj != null) i.putExtra(Conf.INTENT_SUBJECT, subj);
			i.putExtra(Conf.INTENT_DATA, mPaper.getId());
			i.putExtra(Conf.INTENT_EXTRA, mSubjectNumber);
			i.putExtra(Conf.INTENT_TYPE, Subject.TYPE_SORT);
			startActivityForResult(i, Conf.REQEST_CODE_CREATE);
			break;
			
		case Subject.TYPE_ANSWER:
			i.setClass(ActivityCreatePaper.this,ActivityCreateAnserSubject.class);
			if(subj != null) i.putExtra(Conf.INTENT_SUBJECT, subj);
			i.putExtra(Conf.INTENT_DATA, mPaper.getId());
			i.putExtra(Conf.INTENT_EXTRA, mSubjectNumber);
			startActivityForResult(i, Conf.REQEST_CODE_CREATE);
			break;
		default:
			break;
		}
		if(mDialog != null) mDialog.dismiss();
	
	}
	
	protected void onCreatePaperComplete(){
		if(mPaper != null){
			setResult(Conf.RESULT_CODE_DONE);
			finish();
		}else{
			Conf.displayLongToast(ActivityCreatePaper.this, 
					R.string.tip_save_subject_failed_retry);
		}
	}
	
	private void initHeader(){
		
		mTitle.setText(mPaper.getName());
		TextView tv = (TextView) mHeader.findViewById(R.id.header_title);
			tv.setText(mPaper.getName());
			
		tv = (TextView) mHeader.findViewById(R.id.header_description);
			tv.setText(mPaper.getDescription());
			
		tv = (TextView) mHeader.findViewById(R.id.header_marks);
			tv.setText(String.format(getString(R.string.paper_marks),
					mPaper.getMarkes()));
			
		tv = (TextView) mHeader.findViewById(R.id.header_auther);
			tv.setText(String.format(getString(R.string.paper_author), 
				mPaper.getAuthor()));
			
		tv = (TextView) mHeader.findViewById(R.id.header_create_time);
			tv.setText(String.format(getString(R.string.paper_create_time), 
				Conf.formatTime(mPaper.getDate())));
			
		mHeader.setVisibility(View.VISIBLE);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.paper_add_subject:
			createSubjectForPaper();
			break;
			
			// for dilog event
		case R.id.dilog_bt_left:
			savePaperBaseInfo();
			break;
		case R.id.dilog_bt_right:
			if(mDialog.isShowing()){
				mDialog.dismiss();
				mDialog = null;
				finish();
			}
			break;
		default:
			if(v.getTag() != null){
				int tag = Integer.valueOf(v.getTag().toString());
				switch (tag) {
				case ActivityCreate.TITLE_COMPLETE:
				case ActivityCreate.TITLE_CANCEL:
					onCreatePaperComplete();
					break;
				default:
					break;
				}
			}
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Conf.REQEST_CODE_CREATE &&
		   resultCode == Conf.RESULT_CODE_DONE && data != null){
			if(data.hasExtra(Conf.INTENT_DATA)){
				Subject subj = (Subject) data.getSerializableExtra(Conf.INTENT_DATA);
				if(subj == null) return;
				if(!isEditSubject){
					mAdapter.add(subj);
					mSubjectNumber ++;
					isEditSubject = false;
				}else{
					mAdapter.update(subj, mSubjectNumber);
				}
			}
		}
	}
	
	
	private void savePaperBaseInfo(){
		if(mDialog != null && mDialog.isShowing()){
			int[] items = {
					R.id.dilog_item_1,
					R.id.dilog_item_2,
					R.id.dilog_item_3,
					R.id.dilog_item_4};
			View view = null;
			EditText ed = null;
			String str = null;
			boolean isReady = true;
			if(mPaper == null){
				mPaper = new Paper();
			}
			for(int i=0;i<items.length;i++){
				view = mDialog.findViewById(items[i]);
				ed = (EditText) view.findViewById(R.id.paper_edit_text);
				str = ed.getEditableText().toString();
				if(!TextUtils.isEmpty(str)){
					switch (i) {
					case 0:
						mPaper.setName(str);
						break;
					case 1:
						mPaper.setDescription(str);
						break;
					case 2:
						mPaper.setAuthor(str);
						break;
					case 3:
						mPaper.setMarkes(str);
						break;
					default:
						break;
					}
				}
			}
			mPaper.setDate(System.currentTimeMillis());
			long id = Dao.getDaoPaper().insert(mPaper);
			if(isReady && id>-1){
				initHeader();
				mDialog.dismiss();
				mDialog = null;
			}else{
				Conf.displayToast(this, 
						R.string.tip_error_information_uncomplete);
			}
		}
	}

	
	public void onEditSubject(Subject data, int extra) {
		if(data == null)
			return;
		isEditSubject = true;
		onModifySubject(data, data.getType());
	}
	
	public void onDeleteSubject(final Subject base,final int extra){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.tip_app);
		builder.setMessage(String.format(getString(R.string.message_delete), extra+1))
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					boolean deleted = Dao.getDaoSubject().delete(base.getId());
					if(deleted){
						mAdapter.delete(extra);
					}else{
						Conf.displayToast(ActivityCreatePaper.this,
								String.format(getString(R.string.tip_save_subject_failed_retry), 
								extra));
					}
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create().show();
	}
}
