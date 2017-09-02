package com.questionnaire.view;

import com.questionnaire.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class MRatingBar extends LinearLayout {

	private TextView mLabel, mPercentage;
	private RatingBar mRatingBar;
	private String label;
	private String percentage;
	private float rating;
	private int numStars;
	public MRatingBar(Context context) {
		super(context);
		init(context);
	}

	void init(Context context){
		View view = LayoutInflater.from(context).inflate(R.layout.rating_bar, MRatingBar.this);
		mLabel = (TextView) view.findViewById(R.id.label);
		mPercentage = (TextView) view.findViewById(R.id.percentage); 
		mRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
		numStars = mRatingBar.getNumStars();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		mLabel.setText(label);
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
	public String getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		this.percentage = percentage;
		mPercentage.setText(percentage);
	}
}
