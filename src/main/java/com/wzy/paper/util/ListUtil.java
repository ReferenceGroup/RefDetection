package com.wzy.paper.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class ListUtil {
    public static void println(List list) {
        if (CollectionUtils.isEmpty(list)) return;

        for (Object o : list) {
            System.out.println(o.toString());
        }
    }

    /**
     * @param list
     * @param splitSign 分隔符号
     * @return
     */
    public static String list2String(List list, String splitSign) {
        if (CollectionUtils.isEmpty(list)) return "";

        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o.toString() + splitSign);
        }
        return sb.toString();
    }
}
