package com.wzy.paper.compare.core;

import com.wzy.paper.util.StringUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author wzy
 * @Date 2016/11/27 21:28
 */
@Component("ElementComparison")
public class ElementComparison {

    public boolean compareTitle(String refTitle,String evidenceTitle){
        //原文文章名可能含有【J】符号，证据文献中不含。理论上原文中标题 包含 证据文献标题
        //
        //1 提取纯字符
        refTitle= StringUtil.getfromSen(refTitle);
        evidenceTitle= StringUtil.getfromSen(evidenceTitle);

        //2 直接判断是否包含
        return refTitle.contains(evidenceTitle);
    }

    public boolean compareTime(String refTime,String evidenceTime){
        //我们只关注时间中的年份，所以只匹配各自中第一个19** 20** 并判断
        //
        //1 匹配各自中年份
        Pattern pattern=Pattern.compile("19\\d{2}|20\\d{2}");
        Matcher m1=pattern.matcher(refTime);
        if(m1.find()){
            refTime=m1.group(0).trim();
        }

        Matcher m2=pattern.matcher(evidenceTime);
        if(m2.find()){
            evidenceTime=m2.group(0).trim();
        }

        return refTime.equals(evidenceTime);
    }

    public boolean compareSource(String refSource,String evidenceSource){
        //出版单位难以判断，暂定：理论上原文中标题 包含 证据文献标题 TODO
        //
        //1 提取纯字符
        refSource= StringUtil.getfromSen(refSource);
        evidenceSource= StringUtil.getfromSen(evidenceSource);

        //2 直接判断是否包含
        return refSource.contains(evidenceSource);
    }

}
