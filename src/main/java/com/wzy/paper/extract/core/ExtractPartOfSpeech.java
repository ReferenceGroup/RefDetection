package com.wzy.paper.extract.core;

import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.wzy.paper.util.LabelUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExtractPartOfSpeech {
    public List<String> getPartOfSpeech(String ref) {
        List<String> partOfSpeechList= Lists.newArrayList();

        List<Term> termList= HanLP.segment(ref);
        for(Term term:termList){
            partOfSpeechList.addAll(LabelUtil.BIES(term.word,term.nature.name()));
        }

        return partOfSpeechList;
    }
}
