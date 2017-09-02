package com.questionnaire.view;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Paper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaperHeader extends LinearLayout {
	
	Context mContext = null;
	private TextView name;
	private TextView description;
	private TextView auther;
	private TextView date;
	private TextView marks;

	public PaperHeader(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public PaperHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.paper_header, this);
		name = (TextView) view.findViewById(R.id.header_title);
		name.setTextSize(45);
		auther = (TextView) view.findViewById(R.id.header_auther);
		date = (TextView) view.findViewById(R.id.header_create_time);
		description = (TextView) view.findViewById(R.id.header_description);
		marks = (TextView) view.findViewById(R.id.header_marks);
	}

	public void setNameVisible(int visibility) {
		this.name.setVisibility(visibility);
	}
	
	public void setName(String name) {
		this.name.setText(name.trim());
	}
	
	public void setDescription(String description) {
		String des = formatStr(R.string.paper_description, description.trim());
		this.description.setText(des);
	}
	
	public void setAuther(String auther) {
		this.auther.setText(formatStr(R.string.paper_author, auther.trim()));
	}
	
	public void setDate(long date) {
		this.date.setText(formatStr(R.string.paper_create_time, Conf.formatTime(date)));
	}
	
	public void setMarks(String marks) {
		this.marks.setText(formatStr(R.string.paper_marks, marks.trim()));
	}

	public void setPaperHeader(Paper paper) {
		if(paper == null) {
			return;
		}
		setName(paper.getName());
		setDescription(paper.getDescription());
		setMarks(paper.getMarkes());
		setAuther(paper.getAuthor());
		setDate(paper.getDate());
	}
	
	String formatStr(int res, String s) {
		return String.format(mContext.getString(res), s);
	}
}
