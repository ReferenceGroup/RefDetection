package com.wzy.paper.start;

import com.wzy.paper.annotate.Exception.AnnotationException;
import com.wzy.paper.annotate.service.AnnotateService;
import com.wzy.paper.compare.service.RefCompareService;
import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.exception.SearchException;
import com.wzy.paper.search.service.EvidenceCrawlerService;
import com.wzy.paper.search.service.LocalEvidenceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * spring启动类
 *
 * @author wzy
 * @version 1.0
 * @date 2016.十一月.21 03:53 下午
 */
public class ServerStatup {

    private static Log log = LogFactory.getLog(ServerStatup.class);


    public static void main(String[] args)  {
        @SuppressWarnings("resource")
         AbstractApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext_main.xml");

        log.info("Server Start Success /服务成功启动");
        context.registerShutdownHook();
        EvidenceCrawlerService refSearchService= (EvidenceCrawlerService) context.getAutowireCapableBeanFactory().getBean("EvidenceCrawlerService");
        LocalEvidenceService localEvidenceService= (LocalEvidenceService) context.getAutowireCapableBeanFactory().getBean("LocalEvidenceService");
        AnnotateService annotateService= (AnnotateService) context.getAutowireCapableBeanFactory().getBean("AnnotateService");
        RefCompareService refCompareService= (RefCompareService) context.getAutowireCapableBeanFactory().getBean("RefCompareService");


        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            //1 输入参考文献原文
            String originRefString=scanner.nextLine();
            System.out.println("输入："+originRefString);

            //2 网上爬取证据参考文献列表
            List<Reference> evidenceList= null;
            try {
                evidenceList = refSearchService.searchRef(originRefString,3);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SearchException e) {
                e.printStackTrace();
            }

            //3 将证据参考文献列表写入本地库ES
            try {
                localEvidenceService.writeEvidenceToES(originRefString,evidenceList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //4 从本地库中搜索最可能的证据参考文献
            Reference evidence = null;
            try {
                evidence=localEvidenceService.searchMostRelevantRef(originRefString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("选定证据："+evidence.toString());

            //5 标注参考文献原文
            Reference originRef= null;
            try {
                originRef = annotateService.annotateByEvidence(originRefString,evidence);
            } catch (AnnotationException e) {
                e.printStackTrace();
            }

            //6 比对原文与证据相似度
            double xsl=refCompareService.compare(originRef,evidenceList.get(0));
            System.out.println("xsl"+xsl);
            System.out.println();
        }
        scanner.close();

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
