package com.wzy.paper.extract.core;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExtractPosotion {
    /**
     * 获取每个字符在文中位置，共k段
     *
     * @param ref
     * @param k 总共分成k段
     * @return
     */
    public List<Integer> getCnPosition(String ref, int k) {
        List<Integer> positions = Lists.newArrayList();

        if (StringUtils.isEmpty(ref)) {
            return positions;
        }

        int length = ref.length();

        for (int i = 0; i < ref.length(); i++) {
            positions.add(((int) ((double) i / length * k)));
        }

        return positions;
    }



    /**
     * 获取每个字符在文中位置，共k段
     *
     * @param wordList
     * @param k 总共分成k段
     * @return
     */
    public List<Integer> getEnPosition(List<String> wordList, int k) {
        List<Integer> positions = Lists.newArrayList();

        if (CollectionUtils.isEmpty(wordList)) {
            return positions;
        }

        int length = wordList.size();

        for (int i = 0; i < length; i++) {
            positions.add(((int) ((double) i / length * k)));
        }

        return positions;
    }
}
