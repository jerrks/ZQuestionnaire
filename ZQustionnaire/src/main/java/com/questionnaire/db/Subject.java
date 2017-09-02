package com.questionnaire.db;

import com.questionnaire.QuestApp;
import com.questionnaire.R;

public class Subject extends HSubject {
	
	public static final int TYPE_CHOICE_SINGLE = 0;
	public static final int TYPE_CHOICE_MUTILPE = 1;
	public static final int TYPE_SORT = 2;
	public static final int TYPE_ANSWER = 3;
	public static final int TYPE_CLOSE = 4;
	private static final long serialVersionUID = 8494133025190492620L;
	private long paperId;
	private int paperNumber;
	private boolean isRelated;
	private String relatedSubjectIds;
	private long subjectId;
	/**
	 * @return the paperId
	 */
	public long getPaperId() {
		return paperId;
	}
	/**
	 * @param paperId the paperId to set
	 */
	public void setPaperId(long paperId) {
		this.paperId = paperId;
	}
	/**
	 * @return the paperNumber
	 */
	public int getPaperNumber() {
		return paperNumber;
	}
	/**
	 * @param paperNumber the paperNumber to set
	 */
	public void setPaperNumber(int paperNumber) {
		this.paperNumber = paperNumber;
	}
	/**
	 * @return the isRelated
	 */
	public boolean isRelated() {
		return isRelated;
	}
	/**
	 * @param isRelated the isRelated to set
	 */
	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}
	/**
	 * @return the relatedSubjectIds
	 */
	public String getRelatedSubjectIds() {
		return relatedSubjectIds;
	}
	/**
	 * @param relatedSubjectIds the relatedSubjectIds to set
	 */
	public void setRelatedSubjectIds(String relatedSubjectIds) {
		this.relatedSubjectIds = relatedSubjectIds;
	}
	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * get subject type string according to type code
	 * @param typeCode
	 * @return
	 */
	public String getTypeString() {
		String typeStr = "";
		String[] types = QuestApp.getContext().getResources().getStringArray(R.array.dilog_subject_type);
		switch (getType()) {
		case Subject.TYPE_ANSWER:
			typeStr = types[3];
			break;
		case Subject.TYPE_CHOICE_MUTILPE:
			typeStr = types[1];
			break;
		case Subject.TYPE_CHOICE_SINGLE:
			typeStr = types[0];
			break;
		case Subject.TYPE_CLOSE:

			break;
		case Subject.TYPE_SORT:
			typeStr = types[2];
			break;

		default:
			break;
		}
		return typeStr;
	}
}
