package com.questionnaire.db.interfaces;

import java.util.List;

public interface IFace<R> {
	R get(long id);
	List<R> getAll();

	long insert(R data);
	
	int update(R data,String sql);
	
	boolean insertAll(List<R> list);

	boolean delete(long id);
	boolean deleteAll();
}
