package com.wzy.paper.compare.service;

import com.wzy.paper.compare.core.ElementWeight;
import com.wzy.paper.entity.Reference;

/**
 * @Author wzy
 * @Date 2016/11/27 15:40
 */
public interface RefCompareService {

    /**
     * 两个参考文献比对，默认各比例权重：4.0,2.0,1.0,1.0,3.0,4.0
     * @param reference 原文参考文献
     * @param evidence 证据库参考文献
     * @return 返回相似率，double
     */
    double compare(Reference reference, Reference evidence);

    /**
     * 根据输入的各元素权重，计算原参考文献与证据参考文献相似度
     * @param reference 原文参考文献
     * @param evidence 证据库参考文献
     * @param elementWeight 各元素所占权重
     * @return
     */
    double compare(Reference reference, Reference evidence, ElementWeight elementWeight);
}


//~ Formatted by Jindent --- http://www.jindent.com
