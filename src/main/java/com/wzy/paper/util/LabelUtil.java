package com.wzy.paper.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class LabelUtil {
    /**
     * BIES标注模式，BIE分别为开始、中间、结尾，S为单独一体
     * @param word
     * @param nature
     * @return
     */
    public static List<String> BIES(String word,String nature){
        List<String> list= Lists.newArrayList();

        if(StringUtils.isEmpty(word))return list;

        if(word.length()==1){
            list.add("S_"+nature);
            return list;
        }else{
            list.add("B_"+nature);
            for(int i=0;i<word.length()-2;i++){
                list.add("I_"+nature);
            }
        }
        list.add("E_"+nature);
        return list;
    }
}
