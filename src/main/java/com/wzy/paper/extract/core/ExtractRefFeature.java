package com.wzy.paper.extract.core;

import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.wzy.paper.annotate.Exception.AnnotationException;
import com.wzy.paper.annotate.service.AnnotateService;
import com.wzy.paper.util.LabelUtil;
import com.wzy.paper.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 提取参考文献特征类
 *
 * @author wzy
 * @version 1.0, 2016.12.16 at 03:27:49 CST
 */
@Component
public class ExtractRefFeature {

    @Autowired
    private ExtractPartOfSpeech extractPartOfSpeech;

    @Autowired
    private ExtractPosotion extractPosotion;

    @Autowired
    private AnnotateService annotateService;


    /**
     * 参考文献长度分成段数
     */
    private final int k = 5;


    /**
     * 提取参考文献特征，返回特征列表List<List>
     *
     * @param refs
     * @param filePath
     * @param isLabeled 是否添加最后每个字符的预标注结果做训练语料，仍需人工核对
     * @throws IOException
     */
    public List<List> extractFeatureList(List<String> refs, String filePath, boolean isLabeled) throws IOException, AnnotationException {

        // 1 输出文件不存在，则创建
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        List<List> featureList = Lists.newArrayList();//特征集合

        // 2 参考文献集合为空，返回
        if (CollectionUtils.isEmpty(refs)) {
            return featureList;
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder ref_cn = new StringBuilder();

        // 3 遍历每个参考文献
        for (int i = 0; i < refs.size(); i++) {
            String ref = StringUtil.deleteChSpace(StringUtil.subForeSen(refs.get(i)));
            List<String> characters=Lists.newArrayList();
            List positions=Lists.newArrayList();
            List<String> partOfSpeechs=Lists.newArrayList();

            // 如果是中文
            if (StringUtil.isChineseString(ref)) {
                ref_cn.append(ref + "\n");

                // 4 获得参考文献特征
                characters = StringUtil.getCharacter(ref);
                positions = extractPosotion.getCnPosition(ref, k);
                partOfSpeechs = extractPartOfSpeech.getPartOfSpeech(ref);
                if (isLabeled) {
                    List<String> labels = annotateService.annotatePossibly(ref);
                    featureList.add(labels);
                }

                featureList.add(characters);
                featureList.add(positions);
                featureList.add(partOfSpeechs);
            }else{//英文
                //4 英文分词
                List<Term> termList= HanLP.segment(ref);
                for(Term term:termList){
                    characters.add(term.word);
                    partOfSpeechs.add(term.nature.name());
                }
                positions=extractPosotion.getEnPosition(characters,k);
            }

            if (isLabeled) {
                List<String> labels = annotateService.annotatePossibly(ref);
                featureList.add(labels);
            }

            featureList.add(characters);
            featureList.add(positions);
            featureList.add(partOfSpeechs);
        }

        return featureList;

    }


}


//~ Formatted by Jindent --- http://www.jindent.com
