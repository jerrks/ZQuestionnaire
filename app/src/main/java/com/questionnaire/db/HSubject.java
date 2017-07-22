package com.questionnaire.db;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.questionnaire.QuestApp;
import com.questionnaire.R;
import com.questionnaire.utils.Util;


/**
 * hide subject not used for inner use
 * */
public class HSubject extends Base {
	private static final long serialVersionUID = 8494134025190492620L;
	private String topic;
	private String[] options;
	private int type;
	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * @return the options
	 */
	public String[] getOptions() {
		return options;
	}
	/**
	 * @param options the options to set
	 */
	public void setOptions(String[] options) {
		this.options = options;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return the options label
	 */
	public String[] getOptLabels() {
		String[] subLabels = QuestApp.getApp().getResources()
				.getStringArray(R.array.subject_label);
		if(options == null) return subLabels;
		List<String> labels = new ArrayList<String>();
		for (int i = 0; i < options.length; i++) {
			if(!TextUtils.isEmpty(options[i])) {
				labels.add(i, subLabels[i]);
			}
		}
		return Util.toStringArray(labels);
	}
}
