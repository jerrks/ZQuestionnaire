package com.questionnaire.activity.done;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AdapterPaper;
import com.questionnaire.db.Paper;

public class ActivityDoing extends ActivityBase 
	implements OnItemClickListener{

	static final int TITLE_CANCEL = 0;
	static final int TITLE_COMPLETE = 1;
	
	private ListView mPaperList = null;
	private AdapterPaper mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doing);
		initList();
	}
	private void initList(){
		View v = findViewById(R.id.head);
		TextView tv = (TextView) v.findViewById(R.id.title_center);
		tv.setText(R.string.survey_paper);
		
		findViewById(R.id.title_right).setVisibility(View.GONE);
		findViewById(R.id.title_left).setVisibility(View.GONE);
		
		mPaperList = (ListView) findViewById(R.id.paper_list_view);
		mAdapter = new AdapterPaper(this);
		mPaperList.setAdapter(mAdapter);
		mPaperList.setOnItemClickListener(this);
		refreshList();
	}
	
	public void refreshList(){
		if(mAdapter == null){
			initList();
		}
		mAdapter.refreshDataSet();
	}
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Paper paper = mAdapter.getItem(position);
		if(paper != null){
			Intent i = new Intent(this,ActivityDoingPaper.class);
			i.putExtra(Conf.INTENT_DATA, paper);
			startActivity(i);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		refreshList();
	}
}
