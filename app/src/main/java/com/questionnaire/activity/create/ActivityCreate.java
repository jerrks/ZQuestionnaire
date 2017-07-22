package com.questionnaire.activity.create;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterPaper;
import com.questionnaire.db.Paper;
import com.questionnaire.db.impl.Dao;

public class ActivityCreate extends ActivityBase 
	implements OnItemClickListener,OnItemLongClickListener,OnClickListener{

	static final int TITLE_CANCEL = 0;
	static final int TITLE_COMPLETE = 1;
	
	private ListView mPaperList = null;
	private AdapterPaper mAdapter;
	private Button mEditButton;
	private boolean mIsEidt = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		initList();
	}
	private void initList(){
		
		View v = findViewById(R.id.title);
		TextView tv = (TextView) v.findViewById(R.id.title_center);
		tv.setText(R.string.title_create_paper);
		
		mEditButton = (Button) v.findViewById(R.id.title_right);
		mEditButton.setVisibility(View.VISIBLE);
		mEditButton.setText(R.string.create);
		mEditButton.setOnClickListener(this);
		
		mEditButton = (Button) v.findViewById(R.id.title_left);
		mEditButton.setVisibility(View.VISIBLE);
		mEditButton.setBackgroundResource(R.drawable.bt_top_left_bg);
		mEditButton.setText(R.string.edit);
		mEditButton.setOnClickListener(this);
		
		
		findViewById(R.id.paper_add_subject).setOnClickListener(this);
		findViewById(R.id.paper_add_subject).setVisibility(View.GONE);
		
		mPaperList = (ListView) findViewById(R.id.paper_list_view);
		mAdapter = new AdapterPaper(this);
		mPaperList.setAdapter(mAdapter);
		mPaperList.setOnItemLongClickListener(this);
		mPaperList.setOnItemClickListener(this);
		//refreshList();
	}
	
	@Override
	public void refreshList() {
		if(mAdapter == null){
			initList();
		}
		mAdapter.refreshDataSet();
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		if(!mIsEidt)
			return;
		Paper paper = mAdapter.getItem(position);
		if(paper != null){
			Intent i = new Intent(this,ActivityCreatePaper.class);
			i.putExtra(Conf.INTENT_DATA, paper.getId());
			startActivityForResult(i, Conf.REQEST_CODE_CREATE);
		}
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			mIsEidt = !mIsEidt;
			if(mIsEidt){
				mEditButton.setText(R.string.complete);
			}else{
				mEditButton.setText(R.string.edit);
			}
			break;
		case R.id.title_right:
			addPaper();
			break;
		default:
			break;
		}
	}
	
	private void addPaper() {
		Intent i = new Intent();
		i.setClass(this, ActivityCreatePaper.class);
		startActivityForResult(i, Conf.REQEST_CODE_CREATE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Conf.REQEST_CODE_CREATE && resultCode == Conf.RESULT_CODE_DONE){
			refreshList();
		}else
			super.onActivityResult(requestCode, resultCode, data);
	}


	public boolean onItemLongClick(AdapterView<?> adapter, View v,final int index,
			long arg3) {
		final Paper paper = mAdapter.getItem(index);
		String tip = String.format(getString(R.string.tip_delete_paper), 
				paper.getName());
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.tip_app);
		builder.setMessage(tip)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					if(paper != null){
						if(Dao.getDaoPaper().delete(paper.getId())){
							refreshList();
						}else{
							String string = String.format(getString(R.string.tip_delete_paper_failed_retry), 
									paper.getName());
							Conf.displayLongTimeToast(getApplicationContext(), string);
						}
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
		return false;
	}
}
