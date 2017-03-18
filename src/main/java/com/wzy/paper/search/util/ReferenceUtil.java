package com.wzy.paper.search.util;

import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.entity.ESReference;
import com.wzy.paper.util.MD5Util;

/**
 * @Author wzy
 * @Date 2016/11/27 22:55
 */
public class ReferenceUtil {
    public static ESReference Reference2ES(String originRef, Reference reference){
        ESReference esReference=new ESReference();

        esReference.setAuthor(reference.getAuthor());
        esReference.setTitle(reference.getTitle());
        esReference.setSource(reference.getSource());
        esReference.setTime(reference.getTime());
        esReference.setOriginId(MD5Util.MD5(originRef));
        esReference.setOriginRef(originRef);
        esReference.setEvidenceRef(reference.toString());

        return  esReference;

    }
}
