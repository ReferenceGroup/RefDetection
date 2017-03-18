package com.wzy.paper.annotate.service.impl;

import com.wzy.paper.annotate.Exception.AnnotationException;
import com.wzy.paper.annotate.core.ElementAnnotation;
import com.wzy.paper.annotate.service.AnnotateService;
import com.wzy.paper.entity.Reference;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wzy
 * @Date 2016/11/26 21:03
 */
@Service("AnnotateService")
public class AnnotateServiceImpl implements AnnotateService{
    @Autowired
    private ElementAnnotation elementAnnotation;

    public Reference annotateByEvidence(String refString, Reference evidence) throws AnnotationException {
        if(StringUtils.isEmpty(refString)){
            throw new AnnotationException("参考文献原文为空！");
        }
        if(evidence==null){
            throw new AnnotationException("证据参考文献为空");
        }

        return elementAnnotation.annotateByEvidence(refString,evidence);
    }

    @Override
    public List<String> annotatePossibly(String refString) throws AnnotationException {
        if(StringUtils.isEmpty(refString)){
            throw new AnnotationException("参考文献原文为空！");
        }

        return elementAnnotation.annotatePossibly(refString);
    }
}
