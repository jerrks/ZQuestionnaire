package com.questionnaire.db.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.questionnaire.db.HSubject;
import com.questionnaire.db.interfaces.IHSubject;

class SubjectImpl extends Impl<HSubject> implements IHSubject<HSubject> {

	SubjectImpl(SQLiteDatabase db) {
		super(db);
		table = "tb_subject";
		columns = new String[]{ID,TOPIC,TYPE,
			OPTION_A,OPTION_B,OPTION_C,
			OPTION_D,OPTION_E,OPTION_F,OPTION_G};
	}

	@Override
	protected HSubject getData(Cursor c) {
		HSubject data = new HSubject();
		int id = c.getColumnIndex(ID);
		int topic = c.getColumnIndex(TOPIC);
		int type = c.getColumnIndex(TYPE);
		data.setId(c.getLong(id));
		data.setTopic(c.getString(topic));
		data.setType(c.getInt(type));
		String[] options = new String[OPTIONS.length];
		for(int i=0; i<OPTIONS.length; i++){
			options[i] = c.getString(c.getColumnIndex(OPTIONS[i]));
		}
		data.setOptions(options);
		return data;
	}

	@Override
	protected long has(HSubject data) {
		if(data.getId() < 0)
			return -1;
		long id = -1;
		String sql = ID + " = " + data.getId();
		Cursor c = null;
		try {
			c = db.query(table, columns, sql, null, null, null, ID);
			if(c != null && c.moveToFirst()){
				return data.getId();
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
	protected ContentValues getContentValues(HSubject data) {
		ContentValues values = new ContentValues();
		values.put(TOPIC, data.getTopic());
		values.put(TYPE, data.getType());
		int i = 0;
		String options[] = data.getOptions();
		for(;options!=null && i<options.length;i++){
			String str = data.getOptions()[i];
			if(str == null)
				break;
			values.put(OPTIONS[i], str);
		}
		if(i<OPTIONS.length){
			for(;i<OPTIONS.length;i++)
			values.put(OPTIONS[i], "");
		}
		return values;
	}

	private static final String TOPIC="topic";
	private static final String TYPE = "type";
	private static final String OPTION_A = "option_a";
	private static final String OPTION_B = "option_b";
	private static final String OPTION_C = "option_c";
	private static final String OPTION_D = "option_d";
	private static final String OPTION_E = "option_e";
	private static final String OPTION_F = "option_f";
	private static final String OPTION_G = "option_g";
	private static final String[] OPTIONS = {
		OPTION_A,OPTION_B,OPTION_C,OPTION_D,OPTION_E,OPTION_F,OPTION_G
	};
	
	protected static final String createTableSql = " CREATE TABLE "
			+ "tb_subject" + " ( " 
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " 
			
			+ TOPIC + " STRING, " 
			+ TYPE + " INTEGER, " 
			+ OPTION_A + " STRING, " 
			+ OPTION_B + " STRING, " 
			+ OPTION_C + " STRING, " 
			+ OPTION_D + " STRING, " 
			+ OPTION_E + " STRING, " 
			+ OPTION_F + " STRING, " 
			+ OPTION_G + " STRING);";
}
