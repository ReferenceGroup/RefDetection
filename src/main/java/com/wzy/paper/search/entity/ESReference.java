package com.wzy.paper.search.entity;

import com.wzy.paper.entity.Reference;

import java.io.Serializable;

/**
 * @Author wzy
 * @Date 2016/11/27 22:13
 */
public class ESReference extends Reference implements Serializable {

    /** 参考文献原文ID */
    String originId;

    /** 参考文献原文 */
    String originRef;

    /** 证据参考文献 */
    String evidenceRef;

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getOriginRef() {
        return originRef;
    }

    public void setOriginRef(String originRef) {
        this.originRef = originRef;
    }

    public String getEvidenceRef() {
        return evidenceRef;
    }

    public void setEvidenceRef(String evidenceRef) {
        this.evidenceRef = evidenceRef;
    }

}


//~ Formatted by Jindent --- http://www.jindent.com
