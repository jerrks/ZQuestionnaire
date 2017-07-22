package com.questionnaire.activity.statistic;

import java.util.ArrayList;
import java.util.List;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.activity.done.ActivityDoing;
import com.questionnaire.activity.statistic.ActivityStatisticPaper;
import com.questionnaire.adapter.AbsListAdapter;
import com.questionnaire.adapter.AdapterPaper;
import com.questionnaire.db.Paper;
import com.questionnaire.db.impl.Dao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ActivityStatisticList extends ActivityBase 
	implements OnItemClickListener {

	private static final String TAG = "ActivityStatisticList";
	ListView mListView = null;
	List<Paper> mList;
	AbsListAdapter<Paper> mAdapter = null;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paper_list);
		initRes();
		initView();
	}
	
	void initRes() {
		findViewById(R.id.title_left).setVisibility(View.GONE);
		findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) findViewById(R.id.title_center);
		title.setText(R.string.statistic_paper);
		mListView = (ListView) findViewById(R.id.paper_list_view);
		mListView.setOnItemClickListener(this);
	}

	void initView() {
		initList();
	}

	void initList(){
		mList = new ArrayList<Paper>();
		mAdapter = new AdapterPaper(this);
		mListView.setAdapter(mAdapter);
	}
	
	public void refreshList() {
		if(mAdapter == null) {
			initList();
		}
		mList = Dao.getDaoPaper().getAll();
		if(mList == null) {
			Log.d(TAG, "refreshList: mList is null ");
		}
		mAdapter.updateDataSet(mList);
	}
	
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Paper paper = mAdapter.getItem(position); 
		startStatistic(paper);
	}
	
	void startStatistic(Paper paper) {
		if(paper != null) {
			Intent intent = new Intent(this, ActivityStatisticPaper.class);
			intent.putExtra(Conf.INTENT_DATA, paper);
			startActivity(intent);
		}
	}
	
	void startAction(long paper_id){
		String action = getIntent().getAction();
		Intent intent = new Intent();
		if(Conf.ACTION_DOPAPER.equals(action)){
			intent.setClass(this, ActivityDoing.class);
		} else if(Conf.ACTION_STATISTIC.equals(action)) {
			intent.setClass(this, ActivityStatisticPaper.class);
		}
		intent.putExtra(Conf.INTENT_EXTRA, paper_id);
		startActivity(intent);
	}
}
