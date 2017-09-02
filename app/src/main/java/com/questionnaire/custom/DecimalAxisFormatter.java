package com.questionnaire.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.questionnaire.utils.Util;

import java.text.DecimalFormat;

/**
 * 坐标值小鼠格式化
 */
public class DecimalAxisFormatter implements IAxisValueFormatter {

    public DecimalAxisFormatter() {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return Util.formateFloatStr(value);
    }
}
