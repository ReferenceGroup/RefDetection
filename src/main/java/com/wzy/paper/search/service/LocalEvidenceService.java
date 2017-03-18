package com.wzy.paper.search.service;

import com.wzy.paper.entity.Reference;

import java.io.IOException;
import java.util.List;

/**
 * 针对本地落库的证据参考文献，操作的服务类
 * @Author wzy
 * @Date 2016/11/27 14:43
 */
public interface LocalEvidenceService {

    /**
     * 将爬取到的证据参考文献列表写入ES库中
     *
     * @param originRef 参考文献原文爬取到的证据参考文献列表
     * @param evidenceList
     *
     * @throws IOException
     */
    void writeEvidenceToES(String originRef, List<Reference> evidenceList) throws IOException;

    /**
     * 根据给定的参考文献原文得到最相关的证据参考文献
     *
     * @param originRef
     * @return
     */
    Reference searchMostRelevantRef(String originRef) throws Exception;
}


//~ Formatted by Jindent --- http://www.jindent.com
