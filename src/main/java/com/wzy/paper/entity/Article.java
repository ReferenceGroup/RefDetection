package com.wzy.paper.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 文章类
 *
 *
 * @version        1.0
 * @author         wzy
 * @date           2016.十一月.15 03:56 下午
 */
public class Article implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -8553002790904173081L;

    /** Field description */
    private String id;

    /** Field description */
    private String title;

    /** Field description */
    private String author;

    /** Field description */
    private String content;

    /** Field description */
    private List<String> references;

    /**
     * Method description
     *
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Method description
     *
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Method description
     *
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Method description
     *
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public List<String> getReferences() {
        return references;
    }

    /**
     * Method description
     *
     *
     * @param references
     */
    public void setReferences(List<String> references) {
        this.references = references;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method description
     *
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
