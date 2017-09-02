package com.questionnaire.db.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.questionnaire.db.Answer;
import com.questionnaire.db.HSubject;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Subject;
import com.questionnaire.db.Tester;
import com.questionnaire.db.interfaces.IAnswer;
import com.questionnaire.db.interfaces.IHSubject;
import com.questionnaire.db.interfaces.IPaper;
import com.questionnaire.db.interfaces.ISubject;
import com.questionnaire.db.interfaces.ITester;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class Dao {

	protected static String PACKAGE_NAME;
	protected static String DB_PATH;
	
	private final static String DB_NAME = "db_questionnire.db";
	private static SQLiteDatabase db;
	
	private static IAnswer<Answer> mAnswerDao;
	private static IHSubject<HSubject> mSubjectImpl;
	private static ISubject<Subject> mSubjectDao;
	private static ITester<Tester> mTesterDao;
	
	private static IPaper<Paper> mPaperDao;
	
	public static void init(Context context){
		if(db == null){
			PACKAGE_NAME = context.getApplicationContext()
					.getPackageName();
			DB_PATH = "/data"
					+ Environment.getDataDirectory().getAbsolutePath() + "/"
					+ PACKAGE_NAME + "/databases";
			openDatabase(context);
			//db = new DatabaseHelper(context).getWritableDatabase();
			mAnswerDao = new DaoAnswer(db);
			mSubjectImpl = new SubjectImpl(db);
			mSubjectDao = new DaoSubject(db);
			mPaperDao = new DaoPaper(db);
			mTesterDao = new DaoTester(db);
		}
	}
	
	
	
	public static void onDestory(){
		if(db != null && db.isOpen()){
			db.close();
		}
		db = null;
	}
	
	protected static void openDatabase(Context context) {
		
		String path = DB_PATH;
		String name = DB_NAME;
		try {
			File fileDir = new File(path);
			File file = new File(path + "/" + name);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			
			if (!file.exists()) {
				InputStream is = context.getResources()
						.getAssets().open(DB_NAME);
				FileOutputStream os = new FileOutputStream(path + "/" + name);
				int BUFFER_SIZE = 200 * 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					os.write(buffer, 0, count);
				}
				os.close();
				is.close();
			}
			db = context.openOrCreateDatabase(DB_NAME,
					Context.MODE_PRIVATE, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static IHSubject<HSubject> getSubjectImpl(){
		return mSubjectImpl;
	}
	
	public static ISubject<Subject> getDaoSubject(){
		return mSubjectDao;
	}
	
	public static IAnswer<Answer> getDaoAnswer(){
		return mAnswerDao;
	}
	
	public static IPaper<Paper> getDaoPaper(){
		return mPaperDao;
	}
	public static ITester<Tester> getDaoTester(){
		return mTesterDao;
	}
	
//	private static class DatabaseHelper extends SQLiteOpenHelper {
//
//		public DatabaseHelper(Context context) {
//			super(context, DB_NAME, null, DB_VERSION);
//		}
//
//		@Override
//		public void onCreate(SQLiteDatabase db) {
////			db.execSQL(AnswerImpl.createTableSql);
//			db.execSQL(SubjectImpl.createTableSql);
//			
//			db.execSQL(DaoAnswer.createTableSql);
//			db.execSQL(DaoSubject.createTableSql);
//			db.execSQL(DaoTester.createTableSql);
//			db.execSQL(DaoPaper.createTableSql);
//		}
//
//		@Override
//		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		}
//	}
}
