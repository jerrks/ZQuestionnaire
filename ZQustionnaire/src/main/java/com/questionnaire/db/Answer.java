package com.questionnaire.db;

public class Answer extends Base {
	private static final long serialVersionUID = 5046081496407403033L;
	private long paperId;
	private long testerId;
	private long subjectId;
	private String answer;
	
	private boolean done;
	
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
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
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
	 * @return the testerId
	 */
	public long getTesterId() {
		return testerId;
	}
	/**
	 * @param testerId the testerId to set
	 */
	public void setTesterId(long testerId) {
		this.testerId = testerId;
	}
	/**
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}
	/**
	 * @param done the done to set
	 */
	public void setDone(boolean done) {
		this.done = done;
	}
}
