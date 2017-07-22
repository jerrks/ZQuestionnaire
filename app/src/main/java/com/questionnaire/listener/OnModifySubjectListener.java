package com.questionnaire.listener;

import com.questionnaire.db.Subject;

public interface OnModifySubjectListener {
	public void onEditSubject(Subject data,int extra);
	public void onDeleteSubject(Subject data,int extra);
}
