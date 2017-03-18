package com.wzy.paper.compare;

import com.wzy.paper.BaseJunit4Test;
import com.wzy.paper.annotate.core.ElementAnnotation;
import com.wzy.paper.compare.service.RefCompareService;
import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.entity.ESReference;
import com.wzy.paper.search.util.ReferenceUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author wzy
 * @Date 2016/11/27 17:19
 */
public class RefCompareServiceTest extends BaseJunit4Test {
    @Autowired
    private RefCompareService refCompareService;

    @Test
    public void testCompare(){
        Reference reference=new Reference();
        reference.setAuthor("陈绍业");
        reference.setTitle("图书馆目录[J]");
        reference.setSource("高等教育出版社");
        reference.setTime("2000");

        Reference evidence=new Reference();
        evidence.setAuthor("陈绍");
        evidence.setTitle("图书馆目录");
        evidence.setSource("高等教育出版社");
        evidence.setTime("2000");

        ESReference esReference= ReferenceUtil.Reference2ES("aa",reference);

        double xsl=refCompareService.compare(esReference,evidence);
        System.out.println(reference.toString()+" and "+evidence.toString()+" xsl:");
        System.out.println(xsl);
    }
}
