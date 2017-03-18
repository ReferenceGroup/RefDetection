package com.wzy.paper.search;

import com.beust.jcommander.internal.Lists;
import com.wzy.paper.BaseJunit4Test;
import com.wzy.paper.compare.service.RefCompareService;
import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.entity.ESReference;
import com.wzy.paper.search.service.LocalEvidenceService;
import com.wzy.paper.search.util.ReferenceUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @Author wzy
 * @Date 2016/11/28 9:30
 */
public class LocalEvidenceServiceTest extends BaseJunit4Test {
    @Autowired
    private LocalEvidenceService localEvidenceService;

    @Test
    public void testWrite(){
        String originRef="曹阳。图书馆目录。高等教育。1992";

        Reference evidence1=new Reference();
        evidence1.setAuthor("陈绍业,王振宇");
        evidence1.setTitle("图书馆目");
        evidence1.setSource("高等教育出版社");
        evidence1.setTime("2000");

        Reference evidence2=new Reference();
        evidence2.setAuthor("陈绍");
        evidence2.setTitle("图书馆目录");
        evidence2.setSource("高等教育出版局");
        evidence2.setTime("2000");

        List<Reference> evidences= Lists.newArrayList();
        evidences.add(evidence1);
        evidences.add(evidence2);

        try {
            localEvidenceService.writeEvidenceToES(originRef,evidences);
        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println();
    }

    @Test
    public void testSearch(){
        String originRef="曹阳。图书馆目录。高等教育。1992";

        try {
            Reference evidence= localEvidenceService.searchMostRelevantRef(originRef);
            System.out.println(evidence.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
