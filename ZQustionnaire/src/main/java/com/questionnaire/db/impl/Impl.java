package com.questionnaire.db.impl;

import java.util.ArrayList;
import java.util.List;

import com.questionnaire.db.Base;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

abstract class Impl<R extends Base>{
	
	protected final static String DEFUALT_CLUMNS = "defualt_clumns";
	protected final static String ID = "_id";
	protected final static String SUBJECT_ID = "subject_id";
	protected final static String PAPER_ID = "paper_id";
	protected final static String TESTER_ID = "tester_id";
	
	protected SQLiteDatabase db;
	protected String table;
	protected String[] columns;
	
	Impl(SQLiteDatabase db){
		this.db = db;
	}
	
	public R get(long id) {
		Cursor c = null;
		try {
			String sql = ID + "=" + id;
			c = db.query(table, columns, sql, 
					null, null, null, ID, null);
			if(c != null && c.moveToFirst()){
				return getData(c);
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

	public List<R> getAll() {
		Cursor c = null;
		try {
			c = db.query(table, columns, null, 
					null, null, null, ID, null);
			if(c != null && c.moveToFirst()){
				List<R> list = new ArrayList<R>(c.getCount());
				do{
					R data = getData(c);
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

	public long insert(R data) {
		long id = has(data);
		boolean insert = true;
		try {
			if(id > -1){
				String sql = ID + "=" + data.getId();
				int i = update(data,sql);
				if(i>-1){
					insert = false;
				}
			}
			if(insert){
				id = db.insert(table,
						DEFUALT_CLUMNS, 
						getContentValues(data));
				if(id > -1)
					data.setId(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public boolean insertAll(List<R> list){
		boolean ready = true;
		long id = -1;
		for(R data : list){
			id = insert(data);
			if(id < 0){
				ready = false;
				break;
			}
		}
		if(!ready){
			for(R d : list){
				if(d.getId() > -1){
					if(delete(d.getId())){
						d.setId(-1);
					}
				}
			}
		}
		return ready;
	}
	
	
	public int update(R data,String sql) {
		int count = -1;
		if(data == null)
			return count;
		try {
			count = db.update(table, 
					getContentValues(data), 
					sql,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public boolean delete(long id) {
		try {
			String sql = ID + "=" + id;
			int count = db.delete(table, sql, null);
			return count>-1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteAll() {
		try {
			int count = db.delete(table, null, null);
			return count>-1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected abstract R getData(Cursor c);
	protected abstract long has(R data);
	protected abstract ContentValues getContentValues(R data);
}
