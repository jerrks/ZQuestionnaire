package com.questionnaire.db.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.questionnaire.db.Tester;
import com.questionnaire.db.interfaces.ITester;

public class DaoTester extends Impl<Tester> implements ITester<Tester> {

	DaoTester(SQLiteDatabase db) {
		super(db);
		table = "tb_tester";
	}

	@Override
	protected Tester getData(Cursor c) {
		int id = c.getColumnIndex(ID);
		int gender = c.getColumnIndex(GENDER);
		int age = c.getColumnIndex(AGE);
		int nation = c.getColumnIndex(NATION);
		int place = c.getColumnIndex(PLACE);
		int profession = c.getColumnIndex(PORFESSION);
		int contanct = c.getColumnIndex(CONTANCT);
		int date = c.getColumnIndex(DATE);
		
		Tester data = new Tester();
		data.setId(c.getLong(id));
		data.setGender(c.getInt(gender));
		data.setAge(c.getInt(age));
		data.setNation(c.getString(nation));
		data.setPlace(c.getString(place));
		data.setProfession(c.getString(profession));
		data.setContant(c.getString(contanct));
		data.setDate(c.getLong(date));
		return data;
	}

	@Override
	protected long has(Tester data) {
		if(data.getId() < -1)
			return -1;
		if(get(data.getId()) != null)
			return data.getId();
		return -1;
	}

	@Override
	protected ContentValues getContentValues(Tester data) {
		ContentValues values = new ContentValues();
		values.put(GENDER, data.getGender());
		values.put(AGE, data.getAge());
		values.put(NATION, data.getNation());
		values.put(PLACE, data.getPlace());
		values.put(PORFESSION, data.getProfession());
		values.put(CONTANCT, data.getContant());
		values.put(DATE, data.getDate());
		return values;
	}

	private static final String GENDER = "gender";
	private static final String AGE = "age";
	private static final String NATION = "nation";
	private static final String PLACE = "place";
	private static final String PORFESSION = "porfession";
	private static final String CONTANCT = "contanct";
	private static final String DATE = "date";
	
	protected static final String createTableSql = " CREATE TABLE "
			+ "tb_tester" + " ( " 
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " 
			
			+ GENDER + " INTEGER, " 
			+ AGE + " INTEGER, " 
			+ NATION + " STRING, " 
			
			+ PLACE + " STRING, " 
			+ PORFESSION + " STRING, "
			+ CONTANCT + " STRING, " 
			+ DATE + " INTEGER);";
	
}
