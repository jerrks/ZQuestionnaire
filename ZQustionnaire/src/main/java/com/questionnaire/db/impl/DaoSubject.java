package com.questionnaire.db.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.questionnaire.db.HSubject;
import com.questionnaire.db.Subject;
import com.questionnaire.db.interfaces.ISubject;

public class DaoSubject extends Impl<Subject> implements ISubject<Subject> {

	DaoSubject(SQLiteDatabase db) {
		super(db);
		table = "tb_subject_proxy";
		columns = new String[]{ID,SUBJECT_ID,
				PAPER_ID,PAPER_NUMBER,
				IS_RELATED,RELATED_SUBJECT_IDS};
	}
	
	@Override
	public long insert(Subject data) {
		long id = has(data);
		boolean insert = true;
		try {
			if(id > -1){
				String sql = SUBJECT_ID + "=" + data.getSubjectId();
				int i = update(data,sql);
				if(i>0){
					insert = false;
				}else{
					delete(id);
				}
			}
			if(insert){
				id = Dao.getSubjectImpl().insert(data);
				if(id > -1){
					data.setSubjectId(id);
					id = db.insert(table,
							DEFUALT_CLUMNS, 
							getContentValues(data));
					if(id > -1){
						data.setId(data.getSubjectId());
						data.setId(id);
					}
				}else{
					Dao.getSubjectImpl().delete(data.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	@Override
	public Subject get(long id) {
		Cursor c = null;
		try {
			String sql = SUBJECT_ID + "=" + id;
			c = db.query(table, columns, sql, 
					null, null, null, SUBJECT_ID, null);
			if(c != null && c.moveToFirst()){
				Subject subject =  getData(c);
				HSubject data = Dao.getSubjectImpl().get(subject.getId());
				parse(subject, data);
				return subject;
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
	
	@Override
	public int update(Subject data, String sql) {
		int count = -1;
		if(data == null)
			return count;
		try {
			String sqls = ID + "=" + data.getId();
			count = Dao.getSubjectImpl().update(data, sqls);
			if(count > -1){
				count = db.update(table, 
					getContentValues(data), 
					sql,
					null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public boolean delete(long id) {
		try {
			String sql = SUBJECT_ID + "=" + id;
			int count = db.delete(table, sql, null);
			return count>-1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected Subject getData(Cursor c) {
		int subjectId = c.getColumnIndex(ID);
		int id = c.getColumnIndex(SUBJECT_ID);
		int paperId = c.getColumnIndex(PAPER_ID);
		int paperNumber = c.getColumnIndex(PAPER_NUMBER);
		int isRelated = c.getColumnIndex(IS_RELATED);
		int relatedIds = c.getColumnIndex(RELATED_SUBJECT_IDS);
		Subject data = new Subject();
		data.setId(c.getLong(id)); // id for the ture subject
		data.setSubjectId(c.getLong(subjectId)); // proxy id for the subject
		data.setPaperId(c.getLong(paperId));
		data.setPaperNumber(c.getInt(paperNumber));
		data.setRelated(c.getInt(isRelated) == SUBJECT_RELATED);
		data.setRelatedSubjectIds(c.getString(relatedIds));
		return data;
	}

	@Override
	protected long has(Subject data) {
		long id = -1;
		if(data.getId()<-1 || data.getSubjectId() < 0)
			return id;
		Cursor c = null;
		try {
			String sql = SUBJECT_ID + "=" + data.getId() + " and "
					+ PAPER_ID + " = " + data.getPaperId() + " and "
					+ ID + " = " + data.getSubjectId();
			c = db.query(table, columns, sql, null, null, null, PAPER_NUMBER);
			if(c != null && c.moveToFirst()){
				id = c.getLong(c.getColumnIndex(SUBJECT_ID));
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
	protected ContentValues getContentValues(Subject data) {
		ContentValues values = new ContentValues();
		values.put(SUBJECT_ID, data.getSubjectId());
		values.put(PAPER_ID, data.getPaperId());
		values.put(PAPER_NUMBER, data.getPaperNumber());
		values.put(IS_RELATED, data.isRelated());
		values.put(RELATED_SUBJECT_IDS, data.getRelatedSubjectIds());
		return values;
	}
	
	private void parse(Subject subject,HSubject data){
		if(subject == null || data == null)
			return;
		subject.setId(data.getId());
		subject.setOptions(data.getOptions());
		subject.setTopic(data.getTopic());
		subject.setType(data.getType());
	}
	
	public List<Subject> getAll(long paperId) {
		Cursor c = null;
		try {
			String sql = PAPER_ID + "=" + paperId;
			c = db.query(table, columns, sql, 
					null, null, null, PAPER_NUMBER, null);
			if(c != null && c.moveToFirst()){
				List<Subject> list = new ArrayList<Subject>(c.getCount());
				do{
					Subject data = getData(c);
					HSubject bean = Dao.getSubjectImpl().get(data.getId());
					parse(data, bean);
					if(data != null)
						list.add(data);
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
	
	private static final String IS_RELATED = "is_related";
	private static final String RELATED_SUBJECT_IDS = "related_subject_ids";
	private static final String PAPER_NUMBER = "paper_number";
	public static final int SUBJECT_RELATED = 1;
	public static final int SUBJECT_UNRELATED = 0;
	
	protected static final String createTableSql = " CREATE TABLE "
			+ "tb_subject_proxy" + " ( " 
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " 
				
			+ SUBJECT_ID + " INTEGER, " 
			+ PAPER_ID + " INTEGER, " 
			+ IS_RELATED + " INTEGER, " 
			+ RELATED_SUBJECT_IDS + " STRING, " 
			
			+ PAPER_NUMBER + " INTEGER);";
}
