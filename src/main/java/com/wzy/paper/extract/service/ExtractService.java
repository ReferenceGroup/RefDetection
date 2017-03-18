package com.wzy.paper.extract.service;

import com.wzy.paper.annotate.Exception.AnnotationException;
import org.apache.tika.exception.TikaException;

import java.io.IOException;
import java.util.List;

/**
 * @Author wzy
 * @Date 2016/11/15 11:22
 */
public interface ExtractService {


    /**
     * 根据文本路径提取文章内容
     * @param filePath 文本路径
     * @return
     */
    //String extractContext(String filePath) throws IOException, TikaException;

    /**
     * 从文本中提取参考文献部分
     *
     * @param filePath
     * @return
     */
    List<String> extractRef(String filePath) throws IOException, TikaException;

    /**
     * 提取参考文献特征，并写入文献
     * @param refs
     * @param filePath
     * @param isLabeled 是否添加最后每个字符的预标注结果做训练语料，仍需人工核对
     * @param isAppended 是添加到原文件尾部还是否（重写）
     * @throws IOException
     * @throws AnnotationException
     */
    void extractAndWrite(List<String> refs, String filePath, boolean isLabeled, boolean isAppended) throws IOException, AnnotationException;
}
