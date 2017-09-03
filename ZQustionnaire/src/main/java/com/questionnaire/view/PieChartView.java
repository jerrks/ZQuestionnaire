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
import com.questionnaire.content.ChartItem;
import com.questionnaire.utils.MChartUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/8/31.
 */

public class PieChartView extends PieChart implements OnChartValueSelectedListener {

    private static final String TAG = "PieChart";

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    protected Drawable mStarIcon;


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
        mTfRegular = MChartUtil.getTfRegular(getContext());
        mTfLight = MChartUtil.getTfLight(getContext());
        mStarIcon = getResources().getDrawable(R.drawable.star);
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

        setLegendValues();//可以不用设置

        // entry label styling
        setEntryLabelColor(Color.BLACK);//标签黑色
        setEntryLabelTypeface(mTfRegular);
        setEntryLabelTextSize(12f);
    }

    void setLegendValues() {
        Legend l = getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    public static PieDataSet generateDataSet(List<ChartItem> list, String chartLabel, Drawable icon) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        if (list != null) {
            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            for (int i = 0; i < list.size() ; i++) {
                ChartItem item = list.get(i);
                float per = item.getPercentage();
                entries.add(new PieEntry(per, item.label, icon));
            }
        }

        return new PieDataSet(entries, chartLabel);
    }

    public void setPieDataSet(List<ChartItem> list, String chartLabel) {
        PieDataSet dataSet = generateDataSet(list, chartLabel, mStarIcon);
        setPieDataSet(dataSet);
    }

    public void setPieDataSet(PieDataSet dataSet) {

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        dataSet.setColors(MChartUtil.getChartColors());
        dataSet.setSelectionShift(0f);

        //文字显示在里面还是外面，默认里面
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);//百分比为白色
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
}
