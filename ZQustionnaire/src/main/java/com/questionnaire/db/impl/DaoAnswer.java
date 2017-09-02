package com.questionnaire.db.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.questionnaire.db.Answer;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Subject;
import com.questionnaire.db.SubjectAnswerPairs;
import com.questionnaire.db.interfaces.IAnswer;

public class DaoAnswer extends Impl<Answer> implements IAnswer<Answer> {

	DaoAnswer(SQLiteDatabase db) {
		super(db);
		table = "tb_answer";
		columns = new String[]{ID,SUBJECT_ID,ANSWER,TESTER_ID,PAPER_ID};
	}
	
	public List<Answer> getSubjectAnswers(long paperId, long subjectId) {
		if(paperId < 0 || subjectId < 0)
			return null;
		
		Cursor c = null;
		try {
			String sql = PAPER_ID + " = " + paperId + " and " +
					SUBJECT_ID + " = " + subjectId;
			c = db.query(table, columns, sql, 
					null, null, null,ID, null);
			if(c != null && c.moveToFirst()){
				List<Answer> list = new ArrayList<Answer>(c.getCount());
				do{
					Answer data = getData(c);
					if(data != null){
						list.add(data);
					}
				}while(c.moveToNext());
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c!= null && !c.isClosed()){
				c.close();
			}
		}
		return null;
	}
	
	public List<Paper> getPapers(long testerId) {
		if(testerId < 0)
			return null;
		Cursor c = null;
		try {
			String sql = TESTER_ID + " = " + testerId;
			c = db.query(table, columns, sql, 
					null, null, null,ID, null);
			if(c != null && c.moveToFirst()){
				List<Answer> list = new ArrayList<Answer>(c.getCount());
				do{
					Answer data = getData(c);
					if(data != null){
						list.add(data);
					}
				}while(c.moveToNext());
				List<Paper> pList = new ArrayList<Paper>(list.size());
				for(Answer an : list){
					pList.add(Dao.getDaoPaper().get(an.getPaperId()));
				}
				list.clear();
				return pList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c!= null && !c.isClosed()){
				c.close();
			}
		}
		return null;
	}
	
	public List<SubjectAnswerPairs> getSubjectsAnswers(long paperId) {
		if(paperId < 0)
			return null;
		List<Subject> subList = Dao.getDaoSubject().getAll(paperId);
		if(subList == null)
			return null;
		
		List<SubjectAnswerPairs> map = new ArrayList<SubjectAnswerPairs>(subList.size());
		for(Subject subj : subList){
			if(subj == null)
				continue;
			List<Answer> answers = getSubjectAnswers(paperId,subj.getId());
			if(answers != null){
				SubjectAnswerPairs pairs = new SubjectAnswerPairs();
				pairs.setSubject(subj);
				pairs.setAnswers(answers);
				map.add(pairs);
			}
		}
		return map;
	}
	
	@Override
	protected Answer getData(Cursor c) {
		Answer data = new Answer();
		int id = c.getColumnIndex(ID);
		int ans = c.getColumnIndex(ANSWER);
		int subjectId = c.getColumnIndex(SUBJECT_ID);
		int paperId = c.getColumnIndex(PAPER_ID);
		int testerId = c.getColumnIndex(TESTER_ID);
		
		data.setId(c.getLong(id));
		data.setAnswer(c.getString(ans));
		data.setSubjectId(c.getLong(subjectId));
		data.setPaperId(c.getLong(paperId));
		data.setTesterId(c.getLong(testerId));
		return data;
	}

	@Override
	protected long has(Answer data) {
		long id = -1;
		if(data.getId() < 0)
			return id;
		Cursor c = null;
		try {
			String sql = ID + "=" + data.getId() + " and "
					+ SUBJECT_ID + " = " + data.getSubjectId();
			c = db.query(table, columns, sql, null, null, null, SUBJECT_ID);
			if(c != null && c.moveToFirst()){
				return c.getLong(c.getColumnIndex(ID));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(c!=null && !c.isClosed())
				c.close();
		}
		return id;
	}

	@Override
	protected ContentValues getContentValues(Answer data) {
		ContentValues values = new ContentValues();
		values.put(ANSWER, data.getAnswer());
		values.put(SUBJECT_ID, data.getSubjectId());
		values.put(PAPER_ID, data.getPaperId());
		values.put(TESTER_ID, data.getTesterId());
		return values;
	}
	
	private static final String ANSWER = "answer";
	
	protected static final String createTableSql = " CREATE TABLE "
			+ "tb_answer" + " ( " 
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " 
			+ ANSWER + " STRING, "
			+ SUBJECT_ID + " INTEGER, "
			+ PAPER_ID + " INTEGER, " 
			+ TESTER_ID + " INTEGER);";
}
