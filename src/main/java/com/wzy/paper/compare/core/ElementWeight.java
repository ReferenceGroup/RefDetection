package com.wzy.paper.compare.core;

import com.beust.jcommander.internal.Lists;

import java.util.List;

/**
 * 参考文献各元素权重设定类
 * 注：
 *      输出值会将输入值单元化（总数为1）
 * @Author wzy
 * @Date 2016/11/27 16:08
 */
public class ElementWeight {

    /** 作者权重列表 */
    private List<Double> authorWeight;

    /** 标题权重 */
    private double titleWeight ;

    /** 出版社权重 */
    private double sourceWeight ;

    /** 时间权重 */
    private double timeWeight ;

    /** 总共设定的权重 */
    private double totalWeight ;

    public ElementWeight() {
        this(4.0,2.0,1.0,1.0,3.0,4.0);
    }

    /**
     *
     * @param firstAuthorWeight
     * @param secondAuthorWeight
     * @param thirdAuthorWeight
     * @param titleWeight
     * @param sourceWeight
     * @param timeWeight
     */
    public ElementWeight(double firstAuthorWeight, double secondAuthorWeight, double thirdAuthorWeight,
                         double titleWeight, double sourceWeight, double timeWeight) {
        this.totalWeight  = firstAuthorWeight + secondAuthorWeight + thirdAuthorWeight + titleWeight + sourceWeight
                + timeWeight;

        this.authorWeight= Lists.newArrayList();
        authorWeight.add(firstAuthorWeight/totalWeight);
        authorWeight.add(secondAuthorWeight/totalWeight);
        authorWeight.add(thirdAuthorWeight/totalWeight);
        this.titleWeight  = titleWeight/totalWeight;
        this.sourceWeight = sourceWeight/totalWeight;
        this.timeWeight   = timeWeight/totalWeight;

    }

    public List<Double> getAuthorWeight() {
        return authorWeight;
    }

    /**
     *
     * @return
     */
    public double getTitleWeight() {
        return titleWeight ;
    }

    /**
     *
     * @return
     */
    public double getSourceWeight() {
        return sourceWeight ;
    }

    /**
     *
     * @return
     */
    public double getTimeWeight() {
        return timeWeight ;
    }

    /**
     *
     * @return
     */
    public double getTotalWeight() {
        return totalWeight ;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
