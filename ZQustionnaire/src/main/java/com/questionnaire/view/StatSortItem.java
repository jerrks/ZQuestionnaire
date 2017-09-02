package com.questionnaire.view;

import com.questionnaire.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatSortItem extends LinearLayout {

	private TextView[] mLabel = new TextView[8];
	private String[] labels;
	int tvIds[] = { R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5,
			R.id.tv_6, R.id.tv_7, R.id.tv_8, };

	public StatSortItem(Context context) {
		super(context);
		init(context);
	}

	void init(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.stat_sort_item, StatSortItem.this);
		for (int i = 0; i < tvIds.length; i++) {
			mLabel[i] = (TextView) view.findViewById(tvIds[i]);
		}
	}

	/**
	 * @return the labels
	 */
	public String[] getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(String[] labels) {
		this.labels = labels;
		for (int i = 0; i < labels.length; i++) {
			mLabel[i].setText(labels[i]);
		}
	}
	
	/**
	 * @param color the labels to set
	 */
	public void setTextColor(int color) {
		for (int i = 0; i < labels.length; i++) {
			mLabel[i].setTextColor(color);
		}
	}

	public void setTextSize(int textSize) {
		for (int i = 0; i < labels.length; i++) {
			mLabel[i].setTextSize(textSize);
		}
	}
}
