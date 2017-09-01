package com.questionnaire.view;

import com.github.mikephil.charting.utils.Utils;
import com.questionnaire.R;

import java.util.List;
import java.util.ArrayList;
import android.graphics.Color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.questionnaire.utils.Util;

public class MRatingBar extends LinearLayout {

	private TextView mLabel, mPercentage;
	private RatingBar mRatingBar;
	PieChartView mPieChart;
	private String label;
	private float percentage;
	private float rating;
	private int numStars;

	Context mContext;

	public MRatingBar(Context context) {
		super(context);
		mContext = context;
		init(context);
	}

	void init(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.rating_bar, MRatingBar.this);
		mLabel = (TextView) view.findViewById(R.id.label);
		mPercentage = (TextView) view.findViewById(R.id.percentage); 
		mRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
		mPieChart = (PieChartView) view.findViewById(R.id.rate_pie_chart);
		numStars = mRatingBar.getNumStars();
		mPieChart.setLabelStyle(Color.BLACK, 8f);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		mLabel.setText(label);
		mPieChart.setCenterText(label);
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
		mRatingBar.setRating(rating);
	}

	public int getNumStars() {
		return numStars;
	}

	public void setNumStars(int numStars) {
		this.numStars = numStars;
		mRatingBar.setNumStars(numStars);
	}

	public RatingBar getmRatingBar() {
		return mRatingBar;
	}

	public void setmRatingBar(RatingBar mRatingBar) {
		this.mRatingBar = mRatingBar;
	}

	/**
	 * @return the percentage
	 */
	public float getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String label, float percentage) {
		this.percentage = percentage;
		mPercentage.setText(Util.formateFloatStr(percentage) + "%");
		List<PieChartView.ChartItem> list = new ArrayList<PieChartView.ChartItem>();
		PieChartView.ChartItem selectedItem = new PieChartView.ChartItem();
		selectedItem.label = mContext.getString(R.string.selected);
		selectedItem.value = percentage;
		list.add(selectedItem);

		PieChartView.ChartItem unselectedItem = new PieChartView.ChartItem();
		unselectedItem.label = mContext.getString(R.string.unselected);
		unselectedItem.value = (float) 100 - percentage;
		list.add(unselectedItem);
		mPieChart.setPieDataSet(list, "");
		setLabel(label);
	}
}
