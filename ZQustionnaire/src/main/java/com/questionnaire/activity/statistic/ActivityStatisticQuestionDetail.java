package com.questionnaire.activity.statistic;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.activity.ActivityBase;
import com.questionnaire.adapter.AbsListAdapter;
import com.questionnaire.adapter.AdapterQuestionAnswer;
import com.questionnaire.content.ChartItem;
import com.questionnaire.db.Answer;
import com.questionnaire.db.Subject;
import com.questionnaire.db.SubjectAnswerPairs;
import com.questionnaire.utils.QuestManager;
import com.questionnaire.utils.Util;
import com.questionnaire.view.BarChartView;
import com.questionnaire.view.PieChartView;
import com.questionnaire.view.StatSortItem;

import static com.questionnaire.QuestApp.getContext;

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
	BarChartView mBarChart;

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
		mAnswerListView = (ListView) findViewById(R.id.answers_list);
		mTitle = (TextView) findViewById(R.id.title_center);
		mResultLayout  = (LinearLayout) findViewById(R.id.result_layout);
		mPieChart = (PieChartView) findViewById(R.id.pie_chart);
		mBarChart = (BarChartView) findViewById(R.id.bar_chart);
		mBack = (Button) findViewById(R.id.title_left);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
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
		case Subject.TYPE_CHOICE_MUTILPE://多选题
			initChoicesOptionsForSubject();
			drawCharts();
			setResultInfo();
			break;
		case Subject.TYPE_SORT://排序题
            initChoicesOptionsForSubject();
			setSortInfo();
			break;
		case Subject.TYPE_ANSWER://问答题
			initAnswerList();
			break;

		default:
			break;
		}
	}

	void initChoicesOptionsForSubject(){
        Subject data = mSubject;
        String[] options = data.getOptions();
        ViewGroup container = (ViewGroup) findViewById(R.id.subject_detail_options_container);
        View child;
        int c;

        TextView tv;
        ImageView iv;
        Drawable icon = ContextCompat.getDrawable(this,
                data.getType()==Subject.TYPE_CHOICE_SINGLE ? R.drawable.ic_choice_unselect : R.drawable.ic_mutil_choice_normal);
        String[] labels = getResources().getStringArray(R.array.subject_label);
        String value;

        for (int i = 0; i < options.length; i++) {
            c = container.getChildCount();
            if(c>i){
                child = container.getChildAt(i);
            }else{
                child = LayoutInflater.from(container.getContext()).inflate(R.layout.subject_option_item_view,null,false);
                container.addView(child);
            }
            if (TextUtils.isEmpty(options[i])){
                child.setVisibility(View.GONE);
                continue;
            }

            iv = (ImageView) child.findViewById(R.id.subject_option_icon);
            iv.setImageDrawable(icon);

            tv = (TextView) child.findViewById(R.id.subject_option_value);
            value = String.format(
                    getContext().getString(R.string.subject_value_fromat),
                    labels[i],
                    options[i]);
            tv.setText(value);
            child.setVisibility(View.VISIBLE);
        }
    }

	public void setResultInfo() {
		if (mSubject != null) {
			mResultInfo.setVisibility(View.VISIBLE);
			mResultInfo.setText(mManager.parseAnswerChoicesInfo(mAnswers, mSubject.getOptLabels()));
		}
	}

	void drawCharts() {
		if (mSubject == null) {
			return;
		}
		int type = mSubject.getType();
		switch (type) {
			case Subject.TYPE_CHOICE_SINGLE://单选题
				List<ChartItem> list = getChoicePercentage(mAnswers, mSubject.getOptLabels());
				drawPieChart(list);//饼状图
				break;
			case Subject.TYPE_CHOICE_MUTILPE://多选题
				List<ChartItem> list2 = getChoicePercentage(mAnswers, mSubject.getOptLabels());
				drawBarChart(list2);
				break;
			default:
				break;
		}
	}

	/**
	 * 绘制饼状图
	 */
	void drawPieChart(List<ChartItem> list) {
		if (list != null && !list.isEmpty()) {
			mPieChart.setVisibility(View.VISIBLE);
			mPieChart.setPieDataSet(list, mSubject.getTypeString());
			mPieChart.setCenterText(getString(R.string.statistic_detail));
		} else {
			mPieChart.setVisibility(View.GONE);
		}
	}

	/**
	 * 绘制饼状图
	 */
	void drawBarChart(List<ChartItem> list) {
		if (list != null && !list.isEmpty()) {
			mBarChart.setVisibility(View.VISIBLE);
			String label = getString(R.string.statistic_detail) + "(" + mSubject.getTypeString() + ")";
			mBarChart.setDataSet(list, label);
		} else {
			mBarChart.setVisibility(View.GONE);
		}
	}

	public List<ChartItem> getChoicePercentage(List<Answer> list, String[] labels) {
		List<Map.Entry<String, Integer>> resultNum = mManager.getChoiceResoultList(list, labels);
		int totel = mAnswers.size();//答题者总人数
		List<ChartItem> chartItems = new ArrayList<ChartItem>();
		for (Map.Entry<String, Integer> entry : resultNum) {
			int selNum = entry.getValue();
			String label = entry.getKey();
			Log.i(TAG, "label=" + label + ", persons totel=" + totel + ", selNum=" + selNum);
			float per = (100 * selNum) / totel;
			chartItems.add(new ChartItem(label, totel, selNum, per));
		}
		return chartItems;
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
		} else if (per.length() > 4) {
			per = per.substring(0, 3);
		}
		return per  + "%";
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