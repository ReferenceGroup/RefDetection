package com.wzy.paper.entity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 参考文献类
 *
 * @version        1.0
 * @author         wzy
 * @date           2016.十一月.21 03:42 下午
 */
public class Reference {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章链接
     */
    private String link;

    /**
     * 作者
     */
    private String author;

    /**
     * 文章来源
     */
    private String source;

    /**
     * 文章发表时间
     */
    private String time;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 获取用于比对的元素
     * @return
     */
    public List<String> getCompareElements() {
        List<String> elements = Lists.newArrayList();

        elements.add(author);
        elements.add(title);
        elements.add(source);
        elements.add(time);
        return elements;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * @return
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     *
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {

        // TODO Auto-generated method stub
        String string = author + "." + title + "." + source + "." + time;

        return string;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
