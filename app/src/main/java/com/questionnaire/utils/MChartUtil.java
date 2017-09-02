package com.questionnaire.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulk on 17-9-2.
 */

public class MChartUtil {

    private static Typeface mTfRegular;
    private static Typeface mTfLight;

    public static List<Integer> getChartColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();

        //MATERIAL_COLORS
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        //浅色斑斓颜
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        //深色斓颜
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        //深色五彩
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        //浅色淡雅
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        //深色淡雅
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }

    public static Typeface getTfLight(Context context) {
        if (mTfLight == null) {
            mTfLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        }

        return mTfLight;
    }

    public static Typeface getTfRegular(Context context) {
        if (mTfRegular == null) {
            mTfRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
        }
        return mTfRegular;
    }
}
