package com.wzy.paper.compare.service.impl;

import com.wzy.paper.compare.core.ElementComparison;
import com.wzy.paper.compare.core.ElementWeight;
import com.wzy.paper.compare.service.RefCompareService;
import com.wzy.paper.entity.Reference;
import com.wzy.paper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wzy
 * @Date 2016/11/27 16:22
 */
@Service("RefCompareService")
public class RefCompareServiceImpl implements RefCompareService {
    @Autowired
    private ElementComparison elementComparison;



    public double compare(Reference reference, Reference evidence) {
        return compare(reference, evidence, new ElementWeight());
    }


    public double compare(Reference reference, Reference evidence, ElementWeight elementWeight) {
        double   xsl        = 0;
        String[] refAuthors = reference.getAuthor().split(",");
        String[] eviAuthors = evidence.getAuthor().split(",");
        int      refLength  = refAuthors.length;
        int      eviLength  = eviAuthors.length;

        // 比较前三个作者，
        // 原文、证据作者都存在且相等 或者原文作者不存在时
        //认定为正确
        for (int i = 0; i < 3; i++) {
            if ((refLength > i) && (eviLength > i)) {
                if (StringUtil.isCharEqualsOfTwoString(refAuthors[0], eviAuthors[0])) {
                    xsl += elementWeight.getAuthorWeight().get(i);
                }
            } else if (refLength <= i) {
                xsl += elementWeight.getAuthorWeight().get(i);
            }
        }

        // 文章名
        if (elementComparison.compareTitle(reference.getTitle(),evidence.getTitle())) {
            xsl += elementWeight.getTitleWeight();
        }

        // 出版单位
        if (elementComparison.compareSource(reference.getSource(),evidence.getSource())) {
            xsl += elementWeight.getSourceWeight();
        }

        // 时间
        if (elementComparison.compareTime(reference.getTime(),evidence.getTime())) {
            xsl += elementWeight.getTimeWeight();
        }

        return xsl;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
