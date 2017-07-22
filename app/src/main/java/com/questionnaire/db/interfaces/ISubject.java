package com.questionnaire.db.interfaces;

import java.util.List;

public interface ISubject<R> extends IFace<R> {

	List<R> getAll(long paperId);
}
