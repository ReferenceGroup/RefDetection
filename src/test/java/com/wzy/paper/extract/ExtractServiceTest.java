package com.wzy.paper.extract;

import com.google.common.collect.Lists;
import com.wzy.paper.BaseJunit4Test;
import com.wzy.paper.annotate.Exception.AnnotationException;
import com.wzy.paper.extract.core.ExtractRefFeature;
import com.wzy.paper.extract.core.FeatureProcessor;
import com.wzy.paper.extract.service.ExtractService;
import com.wzy.paper.search.service.EvidenceCrawlerService;
import com.wzy.paper.util.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author wzy
 * @Date 2016/11/25 16:44
 */
public class ExtractServiceTest extends BaseJunit4Test {
    @Autowired
    private ExtractService extractService;
    @Autowired
    private EvidenceCrawlerService refSearchService;
    @Autowired
    private ExtractRefFeature extractRefFeature;
    @Autowired
    private FeatureProcessor featureProcessor;


    /**
     * 提取所有的pdf参考文献为txt格式
     *
     * @throws IOException
     */
    @Test
    public void testExtractRef() throws IOException {
        List<String> fileList = Lists.newArrayList();
        List<String> refList = Lists.newArrayList();

        //1 扫描文件夹下所有文件
        String dirPath = "J:\\PersionalData\\大数据文档\\JJW_2016\\1";
        String finishDirPath = "J:\\PersionalData\\大数据文档\\JJW_2016\\txt1";
        String finishTxtDirPath = "C:\\Users\\Administrator\\Desktop\\paper\\txt";
        FileUtil.makeDir(finishDirPath);
        FileUtil.makeDir(finishTxtDirPath);

        FileUtil.scanningFile(dirPath, fileList);

        //2 提取所有文件中所有参考文献列表
        for (int i = 0; i < fileList.size(); i++) {
            String filePath = fileList.get(i);
            try {
                refList = extractService.extractRef(filePath);
            } catch (TikaException e) {
                System.err.println(filePath + ":提取失败！");
                e.printStackTrace();
                continue;
            }
            if (CollectionUtils.isEmpty(refList)) continue;

            StringBuffer sb = new StringBuffer();
            for (String ref : refList) {
                sb.append(ref + "\n");
            }

            String txtFilePath = finishTxtDirPath + "\\" + FileUtil.getFileName(new File(filePath));
            txtFilePath = txtFilePath.replace("pdf", "txt");

            //3 提取参考文献写入txt中
            FileUtils.write(new File(txtFilePath), sb.toString(), "gbk", true);

            //4 转移处理过文件
            try {
                FileUtil.transFile(filePath, dirPath, finishDirPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提取训练语料
     *
     * @throws IOException
     * @throws TikaException
     */
    @Test
    public void testExtractTrained() throws IOException, TikaException {
        List<String> fileList = Lists.newArrayList();
        List<String> refList = Lists.newArrayList();

        //1 扫描文件夹下所有文件
        String dirPath = "C:\\Users\\Administrator\\Desktop\\paper\\200";
        FileUtil.scanningFile(dirPath, fileList);

        //2 提取所有文件中所有参考文献列表
        for (int i = 0; i < fileList.size(); i++) {
            String filePath = fileList.get(i);
            String txt = new Tika().parseToString(new File(filePath));
            refList.addAll(Lists.newArrayList(txt.split("\\r|\\n")));
//            refList.addAll(extractService.extractRef(filePath));
        }
        FileUtil.writeList(new File("C:\\Users\\Administrator\\Desktop\\aaa.txt"),refList,false);
        //3 对提取的参考文献列表进行语料库标注并写入
//        try {
//            extractRefFeature.extractFeatureList(refList, "C:\\Users\\Administrator\\Desktop\\CRF++\\train3.data", true);
//        } catch (AnnotationException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 提取测试语料，不进行机器标注
     *
     * @throws IOException
     * @throws TikaException
     */
    @Test
    public void testExtractTested() throws IOException, TikaException {
        List<String> fileList = Lists.newArrayList();
        List<String> refList = Lists.newArrayList();

        //1 如果是文件夹，扫描文件夹下所有文件；如果是文件，提取该文件
//        String path = "C:\\Users\\Administrator\\Desktop\\paper\\test";
        String path = "C:\\Users\\Administrator\\Desktop\\paper\\200";
        File file = new File(path);
        if (file.isFile()) {
            fileList.add(path);
        } else {
            FileUtil.scanningFile(path, fileList);
        }


        //2 提取所有文件中所有参考文献列表
        for (int i = 0; i < fileList.size(); i++) {
            String filePath = fileList.get(i);
            String txt = new Tika().parseToString(new File(filePath));
            refList.addAll(Lists.newArrayList(txt.split("\\r|\\n")));
        }



        //3 对提取的参考文献列表进行语料库标注并写入
        String savePath = "C:\\Users\\Administrator\\Desktop\\CRF++\\test.data";
        try {
            List<List>featureList=extractRefFeature.extractFeatureList(refList, savePath, false);
//            featureProcessor.writeFeatureList2File();
        } catch (AnnotationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试单条参考文献提取特征
     *
     * @throws IOException
     * @throws TikaException
     */
    @Test
    public void testExtractFingure() throws IOException, TikaException {
        List<String> refStrings = Lists.newArrayList();
//        refStrings.add("戚建明，李叶舟，袁文俊，关于某些代数微分方程亚纯解的增长性估计，数学物理学报 33A(4): 759-765");
//        refStrings.add("顾蓓青、徐晓岭、王蓉华，二元几何分布串联系统在损伤失效率模型下步进应力加速寿命试验的参数估计，西南交通大学学报，2012，47：39-44");
        refStrings.add("Deyu, L., Xiuyun, G. The influence of c-normality of subgroups on the structure of finite groups. Journal of Pure and Applied Algebra, 150(1), 53-60");
//        refString = extractService.extractRef(filePath);

        try {
            List<List>featureList=extractRefFeature.extractFeatureList(refStrings, "C:\\Users\\Administrator\\Desktop\\CRF++\\test1.data", false);
            featureProcessor.writeFeatureList2File(new File("C:\\Users\\Administrator\\Desktop\\aa.txt"),featureList,false);
        } catch (AnnotationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtractContext() throws IOException, TikaException {
        String filePath = "C:\\Users\\Administrator\\Desktop\\paper\\1160010018.pdf";
        // String filePath = "/Users/lqf/Documents/基于最大熵的依存句法分析.pdf";
        StringBuffer context = new StringBuffer();
        List<String> refStrings = null;

        refStrings = extractService.extractRef(filePath);
        for (int i = 0; i < refStrings.size(); i++) {
            context.append(refStrings.get(i) + "\n\n");
        }

        FileUtils.write(new File("c://1.txt"), context, "gbk");

//        try {
//            //context = extractService.extractContext(filePath);
////            System.out.println(context);
//            refString = extractService.extractRef(filePath);
//            for (int i = 0; i < refString.size(); i++) {
//                String originRefString = refString.get(i);
//                System.out.println(originRefString);
//
//                //2 网上爬取证据参考文献列表
//                List<Reference> evidenceList = null;
//                try {
//                    evidenceList = refSearchService.searchRef(originRefString, 3);
//                    if (evidenceList == null) {
//                        System.out.println("evidenceList 证据为空 ");
//                        continue;
//                    }
//                    for (int j = 0; j < evidenceList.size(); j++) {
//
//                        System.err.println("evidence:" + evidenceList.get(j));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (SearchException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TikaException e) {
//            e.printStackTrace();
//        }
    }

}
