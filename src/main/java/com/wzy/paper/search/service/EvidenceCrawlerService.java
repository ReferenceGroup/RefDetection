package com.wzy.paper.search.service;

import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.exception.SearchException;

import java.io.IOException;
import java.util.List;

/**
 * 参考文献证据库搜索服务类
 *
 * @author wzy
 * @version 1.0
 * @date 2016.十一月.21 03:49 下午
 */
public interface EvidenceCrawlerService {

    /**
     * 搜索参考文献证据服务
     *
     * @param originRef 参考文献原文
     * @param n 搜索证据文献最大个数
     * @return
     * @throws IOException
     */
    List<Reference> searchRef(String originRef, int n) throws IOException, SearchException;
}


//~ Formatted by Jindent --- http://www.jindent.com
