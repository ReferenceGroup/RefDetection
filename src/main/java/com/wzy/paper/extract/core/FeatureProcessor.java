package com.wzy.paper.extract.core;

import com.beust.jcommander.ParameterException;
import com.google.common.collect.Lists;
import com.wzy.paper.entity.RefElements;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 特征处理器，主要包括：从CRF++结果文件中提取特征、将特征集合写入文本等
 */
@Component
public class FeatureProcessor {
    /**
     * @param file
     * @param featureList
     * @param isAppended  true是添加到原文件尾部,false是重写
     * @throws IOException
     */
    public void writeFeatureList2File(File file, List<List> featureList, boolean isAppended) throws IOException {
        writeFeatureList2File(file, featureList, "GBK", isAppended);
    }

    /**
     * @param file
     * @param featureList
     * @param charset
     * @param isAppended  true是添加到原文件尾部,false是重写
     * @throws IOException
     */
    public void writeFeatureList2File(File file, List<List> featureList, String charset, boolean isAppended) throws IOException {
        // 1 输出文件不存在，则创建
        if (!file.exists()) {
            file.createNewFile();
        }


        //2 入参校验
        if (CollectionUtils.isEmpty(featureList)) {
            throw new ParameterException("将特征集合写入文本--特征集不可为空！");
        }

        List<String> characters = featureList.get(0);
        StringBuilder sb = new StringBuilder();

        //3 将特征值变成语句
        for (int j = 0; j < characters.size(); j++) {
            StringBuilder sbLine = new StringBuilder();
            for (int k = 0; k < featureList.size(); k++) {
                sbLine.append("\t" + featureList.get(k).get(j));
            }
            String line = sbLine.toString().substring(1) + "\n";
            sb.append(line);
        }

        //4 写入文本
        FileUtils.write(file, sb.toString(), charset, isAppended);//将中文参考文献写入文件中
    }

    /**
     * 将文本内容解析成List<List<String>>特征列表形式,默认分隔符为	：\t
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public List<List> parseTxt2FeatureList(String filePath) throws IOException {
        String splitReg = "\t";
        return parseTxt2FeatureList(filePath, splitReg);
    }


    /**
     * 将文本内容解析成List<List<String>>特征列表形式
     *
     * @param filePath
     * @param splitReg 各特征间间隔符号
     * @return
     * @throws IOException
     */
    public List<List> parseTxt2FeatureList(String filePath, String splitReg) throws IOException {

        //1 读取文档得到所有列
        List<String> lineList = FileUtils.readLines(new File(filePath), "GBK");

        //2 初始化特征存储列表
        List<List> featureList = Lists.newArrayList();
        int featureNum = lineList.get(0).split(splitReg).length;
        for (int i = 0; i < featureNum; i++) {
            List<String> feature = Lists.newArrayList();
            featureList.add(feature);
        }

        //3 遍历得到所有特征内容，并存储
        for (int i = 0; i < lineList.size(); i++) {
            String line = lineList.get(i);
            if (StringUtils.isEmpty(line)) continue;
            String[] features = line.split(splitReg);
            for (int j = 0; j < featureNum; j++) {
                featureList.get(j).add(features[j]);
            }
        }

        return featureList;
    }

    /**
     * 将特征列表组装为参考文献
     *
     * @param characterList
     * @param labelList
     * @return
     */
    public List<RefElements> packFeatureList2Ref(List<String> characterList, List<String> labelList) {
        //存储每条参考文献
        List<RefElements> refElementsList = Lists.newArrayList();

        //1 数据校验
        if (CollectionUtils.isEmpty(characterList) || CollectionUtils.isEmpty(labelList)) return refElementsList;
        if (characterList.size() != labelList.size()) return refElementsList;

        //当前遍历位置索引
        int index = 0;
        //一条参考文献
        StringBuilder ref = new StringBuilder();
        RefElements refElements = new RefElements();
        String lastType = "";

        //2 遍历所有列数，根据标注结果将组装成每条参考文献
        for (int i = 0; i < characterList.size(); ) {
            String label = labelList.get(i);
            String type = label.substring(label.length() - 1);

            //3 如果是开头，则深入挖掘其结尾，确定一个标注元素
            if (label.contains("B")) {
                //如果是开始计数新的人名，则将上面的参考文献存入列表、并清空
                if (!"N".equals(lastType) && "N".equals(type)) {
                    if (!StringUtils.isEmpty(ref.toString())) {
                        ref = new StringBuilder();
                        refElementsList.add(refElements);
                        refElements = new RefElements();
                    }
                }

                index++;
                while (index < characterList.size() && labelList.get(index).contains("I")) {
                    index++;
                }

                StringBuilder character = new StringBuilder();
                for (int j = i; j <= index; j++) {
                    character.append(characterList.get(j));
                }

                //4 构建“N-霍永亮”
                ref.append(type + "-" + character.toString() + " ");
                switch (type) {
                    case "N":
                        refElements.getAuthor().add(character.toString());
                        break;
                    case "P":
                        refElements.setTitle(character.toString());
                        break;
                    case "S":
                        refElements.setSource(character.toString());
                        break;
                    case "D":
                        refElements.setTime(character.toString());
                        break;
                    default:
                        System.out.println("构建参考文献实体异常：" + type + "-" + character.toString() + "  ");
                        break;
                }
                index++;
                i = index;

            } else {//不是开头
                i++;
                index++;
            }
            //如果刚刚标签为O，是无意义标签，不改变当前标签，扔记录离得最近的有意义标签：N或P或S或D
            lastType = type.equals("O") ? lastType : type;
        }

        return refElementsList;
    }


}
