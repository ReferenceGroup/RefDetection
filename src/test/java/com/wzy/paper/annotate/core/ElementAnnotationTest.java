package com.wzy.paper.annotate.core;

import com.wzy.paper.entity.Reference;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author wzy
 * @Date 2016/11/22 21:56
 */
public class ElementAnnotationTest {
    @Test
    public void testAnnotatePossibly(){
        String refString="刘国钧，陈绍业，王凤翥. 图书馆目录[M]. 北京：高等教育出版社，1957.15-18;";

        ElementAnnotation elementAnnotation=new ElementAnnotation();
        try {
            List<String> reference1= elementAnnotation.annotatePossibly(refString);
            System.out.println(reference1.toString());
            System.out.println(reference1.size());
            System.out.println(refString.length());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAnnotateByEvidence(){
        String refString="刘国钧，陈绍业，王凤翥. 图书馆目录[M]. 北京：高等教育出版社，1957.15-18.";
        Reference reference=new Reference();
        reference.setAuthor("陈绍业");
        reference.setTitle("图书馆目录");
        reference.setSource("高等教育出版社");
        reference.setTime("2000");

        ElementAnnotation elementAnnotation=new ElementAnnotation();
        try {
            Reference reference1= elementAnnotation.annotateByEvidence(refString,reference);
            System.out.println(reference.getAuthor());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testRegex(){
        String time="1957";
        Pattern pattern=Pattern.compile("19\\d{2}|20\\d{2}");
        Matcher matcher=pattern.matcher(time);
        if(matcher.matches()){
            System.out.println("matches");
        }

        if(matcher.find()){
            System.out.println("find");
        }
    }

}
