package com.questionnaire.content;

/**
 * Created by hulk on 17-9-2.
 */

public class ChartItem {
    public String label;//标签
    public int totel = 1;
    public int selNum = 0;//被选数量
    private float percentage = 0;//被选择的百分比
    public ChartItem() {
    }

    public ChartItem(String label, int totel, int selNum, float percentage) {
        this.label = label;
        this.totel = totel;
        this.selNum = selNum;
        if (percentage == 0 && totel > 0) {
            this.percentage = (100 * selNum) / totel;
        } else {
            this.percentage = percentage;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTotel() {
        return totel;
    }

    public void setTotel(int totel) {
        this.totel = totel;
    }

    public int getSelNum() {
        return selNum;
    }

    public void setSelNum(int selNum) {
        this.selNum = selNum;
    }

    public float getPercentage() {
        if (percentage == 0 && totel > 0) {
            percentage = (100 * selNum) / totel;
        }
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
