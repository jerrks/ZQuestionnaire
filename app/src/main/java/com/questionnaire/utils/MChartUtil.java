package com.questionnaire.utils;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulk on 17-9-2.
 */

public class MChartUtil {
    public static List<Integer> getChartColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();

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
}
