package com.questionnaire.db;

import java.io.Serializable;
import java.util.List;

public class SubjectAnswerPairs implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4767339694873327825L;

	private Subject subject;
	private List<Answer> answers;
	/**
	 * @return the answers
	 */
	public List<Answer> getAnswers() {
		return answers;
	}
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	/**
	 * @return the subject
	 */
	public Subject getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}
