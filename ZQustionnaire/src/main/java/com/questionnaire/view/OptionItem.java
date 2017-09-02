package com.questionnaire.view;

import com.questionnaire.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OptionItem extends RelativeLayout {

	TextView mText = null;
	String text = null;

	public OptionItem(Context context) {
		super(context);
		init();
	}

	public OptionItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.option_item, this);
		mText = (TextView) view.findViewById(R.id.text);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		mText.setText(text);
	}
}
