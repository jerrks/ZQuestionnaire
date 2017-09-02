package com.questionnaire.utils;

import java.text.DecimalFormat;
import java.util.List;

import android.text.TextUtils;

public class Util {
	private static DecimalFormat sFormat;

	public static String[] toStringArray(List<String> list) {
		if (list == null)
			return null;
		String[] array = new String[list.size()];
		Object[] objs = list.toArray();
		for (int i = 0; i < objs.length; i++) {
			array[i] = (String) objs[i];
		}

		return array;
	}

	public static DecimalFormat getDecimalFormat() {
		if (sFormat == null) {
			sFormat = new DecimalFormat("###,###,###,##0.0");
		}
		return sFormat;
	}

	public static String formateFloatStr(float f) {
		try {
			return getDecimalFormat().format(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f + "";
	}
	
	public static String formateIntegerStr(float f) {
		return getDecimalFormat().format(f);
	}
	
	/**
	 * 四舍五入小数
	 * @param f
	 * @param format 
	 * 一位小数:"#.0"
	 * 两位小数:"#.00"
	 * ...........
	 * @return float String
	 */
	public static String formateFloatStr(float f, String format) {
		java.text.DecimalFormat df = new java.text.DecimalFormat(format);
		return df.format(f);
	}
	
	/**
	 * 
	 * @param f
	 * @return
	 */
	public static float formateFloat(float f) {
		String df = formateFloatStr(f);
		if(TextUtils.isEmpty(df)) {
			return 0;
		}
		return Float.valueOf(df);
	}
	
	/**
	 * 四舍五入小数
	 * @param f
	 * @param format 
	 * 一位小数:"#.0"
	 * 两位小数:"#.00"
	 * ...........
	 * @return float
	 */
	public static float formateFloat(float f, String format) {
		String df = formateFloatStr(f, format);
		if(TextUtils.isEmpty(df)) {
			return 0;
		}
		return Float.valueOf(df);
	}
	
	public static Double formateDouble(double d) {
		String result = String.format("%.1f", d);
		if(TextUtils.isEmpty(result)) {
			return d;
		}
		return Double.valueOf(result);
	}
	
	public static String[] toStringArray(char[] chars){
		if(chars == null) return null;
		String[] array = new String[chars.length];
		for (int i = 0; i < chars.length; i++) {
			array[i] = String.valueOf(chars[i]);
		}
		return array;
	}
	
}
