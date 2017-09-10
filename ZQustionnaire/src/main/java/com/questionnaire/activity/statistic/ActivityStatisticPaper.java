package com.questionnaire.activity.statistic;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AbsListAdapter;
import com.questionnaire.adapter.PaperStatisticAdapter;
import com.questionnaire.db.Paper;
import com.questionnaire.db.SubjectAnswerPairs;
import com.questionnaire.content.QuestManager;
import com.questionnaire.view.PaperHeader;

public class ActivityStatisticPaper extends ActivityBase 
	implements OnItemClickListener, OnClickListener {
	
	private Paper mPaper  = null;
	TextView mTitle = null;
	TextView mRemarkTv = null;
	Button mBack = null;
	View mHeader;
	PaperHeader mPaperHeader = null;
	private ListView mListView = null;
	private AbsListAdapter<SubjectAnswerPairs> mAdapter = null;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_paper);

		initRes();
		mPaper = (Paper) getIntent().getSerializableExtra(Conf.INTENT_DATA);
		if(mPaper == null){
			Conf.displayToast(this, R.string.tip_data_is_invailde);
			finish();
		}else{
			initView();
		}
	}
	
	void initRes() {
		mTitle = (TextView) findViewById(R.id.title_center);
		mPaperHeader = new PaperHeader(this); 
		mListView = (ListView) findViewById(R.id.paper_list);
		mRemarkTv = (TextView) findViewById(R.id.remark_tv);
		mListView.setOnItemClickListener(this);
		mListView.addHeaderView(mPaperHeader);

		mBack = (Button) findViewById(R.id.title_left);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
	}

	void initView() {
		String paperName = mPaper.getName();
		mTitle.setText(paperName);
		mPaperHeader.setPaperHeader(mPaper);
		mPaperHeader.setNameVisible(View.GONE);
		initList();
	}
	
	void initList(){
		mAdapter = new PaperStatisticAdapter(this);
		mListView.setAdapter(mAdapter);
		refreshList1();
	}
	
	public void refreshList1() {
		List<SubjectAnswerPairs> list = QuestManager.getmInstance().getSubjectAnswerPairs(mPaper.getId());
		if(list != null && !list.isEmpty()) {
			mRemarkTv.setVisibility(View.GONE);
			mAdapter.updateDataSet(list);
		} else {
			mRemarkTv.setVisibility(View.VISIBLE);
			mRemarkTv.setText(R.string.paper_answers_null);
		}
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		position = position-1;
		if(position >= 0) {
			SubjectAnswerPairs pairs = mAdapter.getItem(position);
			if(pairs != null) {
				Intent intent = new Intent(this, ActivityStatisticQuestionDetail.class);
				intent.putExtra(Conf.INTENT_DATA, pairs);
				startActivity(intent);
			}
		} 
	}
	
	public void onClick(View v) {
		if(v.getId() == R.id.title_left) {
			finish();
		}
	}
}
