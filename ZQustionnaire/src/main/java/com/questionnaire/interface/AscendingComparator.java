package com.questionnaire.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * 升序比较器
 * Created by hulk on 17-9-10.
 */

public class AscendingComparator implements Comparator<Map.Entry<String, Integer>> {
    //返回1表示要交换位置，0和-1不交换位置
    @Override
    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
        if (e1.getValue() == e2.getValue()) {
            return 0;//两个不交换位置
        }
        if (e1.getValue() < e2.getValue()) {
            return -1; // 第一个比第二个小， 返回-1，不交换位置， 省区
        }
        return 1;//第一个比第二个大，返回1交换位置  升序
    }
}
