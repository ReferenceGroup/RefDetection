package com.wzy.paper.search.service.impl;

import com.google.common.collect.Lists;
import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.entity.ESReference;
import com.wzy.paper.search.exception.SearchException;
import com.wzy.paper.search.service.LocalEvidenceService;
import com.wzy.paper.search.util.ReferenceUtil;
import com.wzy.paper.util.ESUtils;
import com.wzy.paper.util.MD5Util;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author wzy
 * @Date 2016/11/27 22:11
 */
@Service("LocalEvidenceService")
public class LocalEvidenceServiceImpl implements LocalEvidenceService {
    public void writeEvidenceToES(String originRef, List<Reference> evidenceList) throws IOException {
        List<ESReference> esReferenceList = Lists.newArrayList();

        for (int i = 0; i < evidenceList.size(); i++) {
            esReferenceList.add(ReferenceUtil.Reference2ES(originRef, evidenceList.get(i)));
        }
        ESUtils.indexReference("esreferences", "esreference", esReferenceList);
    }

    public Reference searchMostRelevantRef(String originRef) throws Exception {
        String originId = MD5Util.MD5(originRef);

        List<Object> references = ESUtils.searchReference(originId,originRef);
        if(CollectionUtils.isEmpty(references)){
            throw new SearchException("本地参考文献证据库未检索到对应证据参考文献");
        }

        return (Reference) references.get(0);
    }
}
