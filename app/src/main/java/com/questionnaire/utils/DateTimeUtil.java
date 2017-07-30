package com.questionnaire.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;

public class DateTimeUtil {

	/**
	 * format as system current time and format text
	 * @param context
	 * @return
     */
	public static String getCurrentTime(Context context) {
		return DateUtils.formatDateTime(context, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
	}

	/**
	 * formate time as "a hh:mm"
	 * @param context
	 * @param time
	 * @return
	 */
	public static String formateTime24HOUR(Context context, long time) {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(time));
    }

	/**
	 * formate time as "AM HH:mm"  or  "PM HH:mm"
	 * @param context
	 * @param time
	 * @return
	 */
	public static String formateTime12HOUR(Context context, long time) {
	    SimpleDateFormat sdf = new SimpleDateFormat("a hh:mm");
        return sdf.format(new Date(time));
    }

	/**
	 * ormate time as "yyyyMMddHHmmss"
	 * @param time
	 * @return
	 */
	public static String getFormatTime(long time) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return df.format(time);
	}
}
