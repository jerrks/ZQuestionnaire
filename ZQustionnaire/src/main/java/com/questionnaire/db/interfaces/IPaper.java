package com.questionnaire.db.interfaces;

import java.util.List;

public interface IPaper<R> extends IFace<R> {
	/**
	 * @param testerId the id of tester 
	 * 
	 * @return return the papers of the tester have done
	 * */
	List<R> getPapers(long testerId); 
}
