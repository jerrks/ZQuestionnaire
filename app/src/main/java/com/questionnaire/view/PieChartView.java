package com.questionnaire.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.questionnaire.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/8/31.
 */

public class PieChartView extends PieChart implements OnChartValueSelectedListener {

    private static final String TAG = "PieChart";

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    static Drawable mStarIcon;


    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mTfRegular = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");
        //mStarIcon = getResources().getDrawable(R.drawable.star);
        initView();
    }

    private void initView() {
        setUsePercentValues(true);
        getDescription().setEnabled(false);
        setExtraOffsets(5, 10, 5, 5);

        setDragDecelerationFrictionCoef(0.95f);

        setCenterTextTypeface(mTfLight);
        //setCenterText(generateCenterSpannableText());

        setDrawHoleEnabled(true);
        setHoleColor(Color.WHITE);

        setTransparentCircleColor(Color.WHITE);
        setTransparentCircleAlpha(110);

        setHoleRadius(58f);
        setTransparentCircleRadius(61f);

        setDrawCenterText(true);

        setRotationAngle(0);
        // enable rotation of the chart by touch
        setRotationEnabled(true);
        setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        setOnChartValueSelectedListener(this);

        //setData(4, 100);

        animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        setEntryLabelColor(Color.BLACK);//Color.WHITE
        setEntryLabelTypeface(mTfRegular);
        setEntryLabelTextSize(12f);
    }

    /**
     *
     * @param color one of Color.*
     * @param textSize
     */
    public void setLabelStyle(Color color, float textSize) {
        setEntryLabelColor(Color.BLACK);
        //setEntryLabelTypeface(mTfRegular);
        setEntryLabelTextSize(textSize);
    }

    public static PieDataSet getDataSet(List<ChartItem> list, String chartLabel, Drawable icon) {
        if (list == null) {
            return null;
        }
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < list.size() ; i++) {
            ChartItem item = list.get(i);
            entries.add(new PieEntry(item.value, item.label, icon));
        }
        return new PieDataSet(entries, chartLabel);
    }

    public void setPieDataSet(List<ChartItem> list, String chartLabel) {
        PieDataSet dataSet = getDataSet(list, chartLabel, mStarIcon);
        setPieDataSet(dataSet);
    }

    public void setPieDataSet(PieDataSet dataSet) {

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        setData(data);

        // undo all highlights
        highlightValues(null);

        invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
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

    public static class ChartItem {
        public String label;//标签
        public float value;//百分比
        public ChartItem() {
        }
        public ChartItem(String label, float value) {
            this.label = label;
            this.value = value;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return "ChartItem{" +
                    "label=" + label +
                    ", value=" + value +
                    '}';
        }
    }
}
