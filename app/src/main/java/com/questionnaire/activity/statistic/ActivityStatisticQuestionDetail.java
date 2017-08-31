package com.questionnaire.activity.statistic;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.activity.chart.PieChartActivity;
import com.questionnaire.adapter.AbsListAdapter;
import com.questionnaire.adapter.AdapterQuestionAnswer;
import com.questionnaire.db.Answer;
import com.questionnaire.db.Subject;
import com.questionnaire.db.SubjectAnswerPairs;
import com.questionnaire.utils.QuestManager;
import com.questionnaire.utils.Util;
import com.questionnaire.view.MRatingBar;
import com.questionnaire.view.PieChartView;
import com.questionnaire.view.StatSortItem;

public class ActivityStatisticQuestionDetail extends ActivityBase implements
		OnItemClickListener, OnClickListener {

	private static final String TAG = "StatisticQuestionDetail";
	private ListView mAnswerListView = null;
	private SubjectAnswerPairs mSubjectAnswerPairs = null;
	private Subject mSubject = null;
	private List<Answer> mAnswers = null;
	QuestManager mManager = null;
	TextView mResultInfo = null;
	TextView mTopic = null;
	TextView mTitle = null;
	
	LinearLayout mResultLayout;
	PieChartView mPieChart;

	private final int[] mMultiOpts = { R.id.choice_multiple_a,
			R.id.choice_multiple_b, R.id.choice_multiple_c,
			R.id.choice_multiple_d, R.id.choice_multiple_e,
			R.id.choice_multiple_f, R.id.choice_multiple_g };

	private final int[] mSingleOpts = { R.id.choice_sigle_a,
			R.id.choice_sigle_b, R.id.choice_sigle_c, R.id.choice_sigle_d };
	private Button mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_quest_detail);
		
		initRes();
		mManager = QuestManager.getmInstance();
		initData();
	}
	
	void initRes() {
		mTopic = (TextView) findViewById(R.id.topic);
		mResultInfo = (TextView) findViewById(R.id.result_info);
		mResultInfo.setVisibility(View.GONE);
		mAnswerListView = (ListView) findViewById(R.id.answers_list);
		mTitle = (TextView) findViewById(R.id.title_center);
		mResultLayout  = (LinearLayout) findViewById(R.id.result_layout);
		mPieChart = (PieChartView) findViewById(R.id.pie_chart);
		mBack = (Button) findViewById(R.id.title_left);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
		mResultLayout.setOnClickListener(this);
	}
	
	void initData() {
		mSubjectAnswerPairs = (SubjectAnswerPairs) getIntent()
				.getSerializableExtra(Conf.INTENT_DATA);
		if (mSubjectAnswerPairs != null) {
			mSubject = mSubjectAnswerPairs.getSubject();
			mAnswers = mSubjectAnswerPairs.getAnswers();
			if(mSubject == null || mAnswers == null) {
				Log.e(TAG, "Subject or answer list is null !! ");
				return;
			}
			String title = getString(R.string.statistic_detail) +"(" +mSubject.getTypeString()+")";
			mTitle.setText(title);
			int paperNumber = mSubject.getPaperNumber() + 1;
			mTopic.setText(paperNumber + ". " + mSubject.getTopic());
			dispatch();
		}
	}

	void dispatch() {
		switch (mSubject.getType()) {
		case Subject.TYPE_CHOICE_SINGLE://单选题
			initSingleChoice();
			break;
		case Subject.TYPE_CHOICE_MUTILPE://多选题
			initMultiChoice();
			break;
		case Subject.TYPE_SORT://排序题
			initSortResult();
			break;
		case Subject.TYPE_ANSWER://问答题
			initAnswerList();
			break;

		default:
			break;
		}
	}

	void initSingleChoice() {
		View view = findViewById(R.id.detail_singlechoice);
		view.setVisibility(View.VISIBLE);
		setOptionsAndResult(view, mSingleOpts);
		setChoiceRating(false, true);
	}

	void initMultiChoice() {
		View view = findViewById(R.id.detail_multichoice);
		view.setVisibility(View.VISIBLE);
		setOptionsAndResult(view, mMultiOpts);
		setChoiceRating(true, false);
	}

	void setOptionsAndResult(View view, int[] optRes) {
		view.findViewById(R.id.choice_title).setVisibility(View.GONE);
		String[] labels = getResources().getStringArray(R.array.subject_label);
		String[] options = mSubject.getOptions();
		for (int i = 0; i < optRes.length; i++) {
			CompoundButton btn = (CompoundButton) view.findViewById(optRes[i]);
			String opt = options[i];
			if(TextUtils.isEmpty(opt)) {
				btn.setVisibility(View.GONE);
				continue;
			}else{
				btn.setVisibility(View.VISIBLE);
				btn.setEnabled(false);
				String text = formatOption(labels[i], options[i]);
				btn.setText(text);
			}
		}
		
	}
	
	void setChoiceRating(boolean isMultiChoice, boolean isChart) {
		mResultInfo.setText(mManager.parseAnswerChoicesInfo(mAnswers, mSubject.getOptLabels()));
		List<Map.Entry<String, Integer>> resultNum = mManager.getChoiceResoultList(mAnswers, mSubject.getOptLabels());
		int totel = mAnswers.size();//答题者总人数
		List<PieChartView.ChartItem> list = new ArrayList<PieChartView.ChartItem>();
		for (Map.Entry<String, Integer> entry : resultNum) {
			MRatingBar optItem = new MRatingBar(this);
			int numStars = optItem.getNumStars();
			float selNum = entry.getValue();
			String label = entry.getKey();
			if(totel <= 0) totel = numStars;
			float rating = (float)(numStars * selNum / totel);
			Log.d(TAG, "label=" + label + ", totel=" + totel + ", selNum=" + selNum + ", numStars=" + numStars + ", rating=" + rating);
			float per = (100 * selNum) / totel;
			optItem.setLabel(label + ": ");
			optItem.setPercentage(Util.formateFloatStr(per) + "%");
			optItem.setRating(rating);
			mResultLayout.addView(optItem);
			list.add(new PieChartView.ChartItem(label, per));
		}
		if (isChart) {
			if (!list.isEmpty()) {
				mPieChart.setVisibility(View.VISIBLE);
				mPieChart.setPieDataSet(list, mSubject.getTypeString());
				mPieChart.setCenterText(getString(R.string.statistic_detail));
			}
		}
	}
	
	String formatOption(String label, String option) {
		String text = String.format(getString(R.string.subject_value_fromat),
				label, option);
		return text;
	}
	
	void initAnswerList() {
		findViewById(R.id.choice_layout).setVisibility(View.GONE);
		mAnswerListView.setVisibility(View.VISIBLE);
		AbsListAdapter<Answer> adapter = new AdapterQuestionAnswer(this, mAnswers);
		mAnswerListView.setAdapter(adapter);
		mAnswerListView.setOnItemClickListener(this);
	}
	
	void initSortResult() {
		View view = findViewById(R.id.detail_multichoice);
		view.setVisibility(View.VISIBLE);
		setOptionsAndResult(view, mMultiOpts);
		setSortInfo();
	}
	void setSortInfo() {
		List<Map.Entry<String, Map<Integer, Integer>>> resultNum = mManager.parseSortSubResults(mAnswers, mSubject.getOptLabels());
		int totel = mAnswers.size();
		StatSortItem sortItem = new StatSortItem(this);
		String[] sortInfos = getResources().getStringArray(R.array.stat_sort_label);
		sortItem.setLabels(sortInfos);
		sortItem.setTextColor(R.color.blanchedalmond);
		mResultLayout.addView(sortItem);

		for (Entry<String, Map<Integer, Integer>> optEntry : resultNum) {
			String label = optEntry.getKey();
			String[] opts = new String[8];
			opts[0] = label.trim() + ":";
			int i = 1;
			 Map<Integer, Integer> map = optEntry.getValue();
			 for (Entry<Integer, Integer> entry : map.entrySet()) {
				 int numKey = (int) entry.getKey();
				 if(Conf.DEBUG)Log.d(TAG, "initSortResult: numKey = " + numKey);
				 float numValue = entry.getValue();
				 float rating = (float)(numValue / totel);
				 String ratingInfo = formatePer(rating);
				 opts[i] = ratingInfo.trim();
				 i++;
			 }
			 StatSortItem optItem = new StatSortItem(this);
			 optItem.setLabels(opts);
			 mResultLayout.addView(optItem);
		}
	}

	String formatePer(float f) {
		String per = Util.formateIntegerStr(100 * f);
		if(per.length() < 4) {
			per = "    " + per;
		}
		return per  + "%"/*Util.formateIntegerStr(100 * f)*/;
	}
	
	String formateWidth(String s) {
		String str = s;
		if(str.length() < 5) {
			str = "        " + str;
		}
		return str;
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Log.d(TAG, "mList item : " + mAnswers.get(position));
	}

	public void onClick(View v) {
		if(v == mBack) {
			finish();
		}
	}
}