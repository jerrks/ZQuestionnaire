package com.questionnaire.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;

public class DateTimeUtil {

	public static final String TEXT_DAY =  "天";//天
	public static final String TEXT_HOUR =  "小时";
	public static final String TEXT_MINUTE = "分钟";//60s
	public static final String TEXT_SECOND = "秒";

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

	/**
	 * 格式化间隔时间eg. XX天XX小时XX分钟XX秒
	 * @param intervalSeconds  时间间隔，单位：秒
	 * @return
	 */
	public static String formatIntervalSeconds(long intervalSeconds) {
		if (intervalSeconds <= 0) {
			return "";
		}
		long minutes = intervalSeconds / 60;
		long seconds = intervalSeconds % 60;
		long hours = minutes / 60;//求商为小时
		minutes = minutes % 60;//求余为剩下分钟数
		long days = hours / 24;//求商为天数
		hours = hours % 24; //求余为剩余小时数
		StringBuffer buffer = new StringBuffer();
		if (days > 0) {
			buffer.append(days + TEXT_DAY);//XX天
		}
		if (hours > 0) {
			buffer.append(hours + TEXT_HOUR);//XX小时
		}
		if (minutes > 0) {
			buffer.append(minutes + TEXT_MINUTE);//XX分钟
		}
		if (seconds > 0) {
			buffer.append(seconds + TEXT_SECOND);//XX秒
		}
		return buffer.toString();//XX天XX小时XX分钟XX秒
	}
}
