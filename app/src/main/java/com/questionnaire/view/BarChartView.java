package com.questionnaire.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.questionnaire.R;
import com.questionnaire.content.ChartItem;
import com.questionnaire.custom.DecimalAxisFormatter;
import com.questionnaire.custom.LabelAxisFormatter;
import com.questionnaire.utils.MChartUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/8/31.
 */

public class BarChartView extends BarChart implements OnChartValueSelectedListener {

    private static final String TAG = "PieChart";

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    protected Drawable mStarIcon;


    public BarChartView(Context context) {
        super(context);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mTfRegular = MChartUtil.getTfRegular(getContext());
        mTfLight = MChartUtil.getTfLight(getContext());
        mStarIcon = getResources().getDrawable(R.drawable.star);
        initView();
    }

    private void initView() {
        // apply styling
        getDescription().setEnabled(false);
        setDrawGridBackground(false);

        //X轴属性
        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);

        //y轴属性
        IAxisValueFormatter custom = new DecimalAxisFormatter();

        YAxis leftAxis = getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMaximum(100);
        leftAxis.setValueFormatter(custom);

        YAxis rightAxis = getAxisRight();
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMaximum(100);
        rightAxis.setValueFormatter(custom);

        Legend l = getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        // set data
        //setData(data);
        //setFitBars(true);

        // do not forget to refresh the chart
        invalidate();
        animateY(700);
    }

    public MyBarDataSet generateDataSet(List<ChartItem> list, String chartLabel, Drawable icon) {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        List<String> xLabels = new ArrayList<String>();//X轴上的文字标签
        if (list != null) {
            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            for (int i = 0; i < list.size() ; i++) {
                ChartItem item = list.get(i);
                entries.add(new BarEntry(i, item.getPercentage()));
                xLabels.add(item.getLabel());
            }
        }
        return new MyBarDataSet(entries, chartLabel, xLabels);
    }

    public void setDataSet(List<ChartItem> list, String chartLabel) {
        MyBarDataSet dataSet = generateDataSet(list, chartLabel, mStarIcon);
        setDataSet(dataSet);
    }

    public void setDataSet(MyBarDataSet dataSet) {

        BarDataSet d = dataSet;

        // apply styling
        d.setValueTypeface(mTfLight);
        d.setValueTextColor(Color.BLACK);
        //d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setColors(MChartUtil.getChartColors());
        d.setBarShadowColor(Color.rgb(203, 203, 203));

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        sets.add(d);

        BarData cd = new BarData(sets);
        cd.setBarWidth(0.9f);
        // set data
        setData(cd);
        setFitBars(true);

        //X轴显示文字
        IAxisValueFormatter xAxisFormatter = new LabelAxisFormatter(dataSet.mXLabels);
        getXAxis().setValueFormatter(xAxisFormatter);

        // do not forget to refresh the chart
        invalidate();
        animateY(700);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i(TAG,
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "nothing selected");
    }

    public class MyBarDataSet extends BarDataSet {
        public List<String> mXLabels = new ArrayList<String>();//X轴上的文字标签
        public MyBarDataSet(List<BarEntry> yVals, String chartLabel, List<String> xLabels) {
            super(yVals, chartLabel);
            setXLabels(xLabels);
        }
        public void setXLabels(List<String> xLabels) {
            mXLabels.clear();
            mXLabels.addAll(xLabels);
        }
    }
}
