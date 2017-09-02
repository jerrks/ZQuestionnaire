package com.questionnaire.custom;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.questionnaire.utils.Util;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by philipp on 02/06/16.
 */
public class LabelAxisFormatter implements IAxisValueFormatter {

    public static final String TAG =  "LabelAxisFormatter";
    public List<String> xLabels;

    public LabelAxisFormatter(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (xLabels != null && !xLabels.isEmpty()) {
            try {
                int index = (int)value;
                if (index < 0) {
                    index = 0;
                } else if (index >= xLabels.size()) {
                    index = xLabels.size() - 1;
                }
                String label = xLabels.get(index);
                Log.i(TAG, "getFormattedValue label:  + label" + ", value= " + value);
                return label;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Util.formateFloatStr(value);
    }
}
