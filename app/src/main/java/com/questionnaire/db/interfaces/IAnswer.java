package com.questionnaire.db.interfaces;

import java.util.List;

import com.questionnaire.db.Paper;
import com.questionnaire.db.SubjectAnswerPairs;


public interface IAnswer<R> extends IFace<R>{
	/**
	 * @param paperId the paper id
	 * @param subjectId the subject id of the paper
	 * */
	List<R> getSubjectAnswers(long paperId,long subjectId);
	
	/**
	 * @param paperId the id of paper
	 * @return all the answers of all the subjects of the given paper
	 * */
	List<SubjectAnswerPairs> getSubjectsAnswers(long paperId);
	
	/**
	 * get papers of the tester have done
	 * @param testerId
	 * */
	List<Paper> getPapers(long testerId);
}
