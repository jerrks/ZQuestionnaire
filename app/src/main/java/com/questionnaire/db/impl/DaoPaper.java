package com.questionnaire.db.impl;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.questionnaire.db.Paper;
import com.questionnaire.db.interfaces.IPaper;

public class DaoPaper extends Impl<Paper> implements IPaper<Paper> {

	DaoPaper(SQLiteDatabase db) {
		super(db);
		table = "tb_paper";
		columns = new String[]{ID,SUBJECT_IDS,NAME,DESCRIPTION,
				AUTHOR,MARKES,DATE};
	}
	
	public List<Paper> getPapers(long testerId) {
		return Dao.getDaoAnswer().getPapers(testerId);
	}

	@Override
	protected Paper getData(Cursor c) {
		int id = c.getColumnIndex(ID);
		int name = c.getColumnIndex(NAME);
		int desc = c.getColumnIndex(DESCRIPTION);
		int anthor = c.getColumnIndex(AUTHOR);
		int marks = c.getColumnIndex(MARKES);
		int date = c.getColumnIndex(DATE);
		Paper paper = new Paper();
		paper.setId(c.getLong(id));
		paper.setName(c.getString(name));
		paper.setDescription(c.getString(desc));
		paper.setAuthor(c.getString(anthor));
		paper.setMarkes(c.getString(marks));
		paper.setDate(c.getLong(date));
		return paper;
	}

	@Override
	protected long has(Paper data) {
		long id = -1;
		if(data.getId()<0)
			return id;
		id = data.getId();
		if(get(id)==null)
			id = -1;
		return id;
	}

	@Override
	protected ContentValues getContentValues(Paper data) {
		ContentValues values = new ContentValues();
		values.put(NAME, data.getName());
		values.put(SUBJECT_IDS, data.getSubjectIds());
		values.put(DESCRIPTION, data.getDescription());
		values.put(AUTHOR, data.getAuthor());
		values.put(MARKES, data.getMarkes());
		values.put(DATE, data.getDate());
		return values;
	}

	private static final String SUBJECT_IDS = "subject_ids";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String AUTHOR = "author";
	private static final String MARKES = "markes";
	private static final String DATE = "date";
	
	protected static final String createTableSql = " CREATE TABLE "
			+ "tb_paper" + " ( " 
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " 
			
			+ SUBJECT_IDS + " STRING, " 
			+ NAME + " STRING, " 
			+ DESCRIPTION + " STRING, " 
			+ AUTHOR + " STRING, " 
			+ MARKES + " STRING, " 
			
			+ DATE + " INTEGER);";
}
