package com.questionnaire;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class Conf {
	public final static boolean DEBUG = false;
	public static String INTENT_SUBJECT = "subject";
	public static String INTENT_DATA = "data";
	public static String INTENT_TITLE = "title";
	public static String INTENT_EXTRA = "intent_extra";
	public static String INTENT_TYPE = "type";
	
	public static int REQEST_CODE_CREATE = 1;
	public static int RESULT_CODE_DONE = 10;
	
	public final static String ACTION_DOPAPER = "intent.action.doPaper";
	public final static String ACTION_STATISTIC = "intent.action.statistic";
	
	public static String formatTime(long milles){
		if(milles < 1000)
			milles = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = new Date(milles);
		return sdf.format(d);
	}
	
	public static void displayToast(Context context, String m) {
        Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
    }

    public static void displayToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_SHORT).show();
    }
    
    public static void displayLongToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG).show();
    }

    public static void displayLongTimeToast(Context context, String m) {
        Toast.makeText(context, m, Toast.LENGTH_LONG).show();
    }
    
    public static String formatString(int res, Object objs) {
		if(objs == null) return "";
		return String.format(QuestApp.getContext().getString(res), objs);
	}
}
