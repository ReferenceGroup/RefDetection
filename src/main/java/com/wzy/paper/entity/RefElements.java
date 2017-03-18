package com.wzy.paper.entity;

import com.beust.jcommander.internal.Lists;
import com.wzy.paper.util.ListUtil;

import java.util.List;

/**
 * 参考文献主要实体类
 */
public class RefElements {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 作者
     */
    private List<String> author;

    /**
     * 文章来源
     */
    private String source;

    /**
     * 文章发表时间
     */
    private String time;

    @Override
    public String toString() {

        return ("作者：" + ListUtil.list2String(author, " ") + "   文章名：" + title + "   来源：" + source + "   时间：" + time);
    }

    public RefElements() {
        author = Lists.newArrayList();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
