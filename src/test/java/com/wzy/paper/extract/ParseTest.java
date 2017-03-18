package com.wzy.paper.extract;

import com.wzy.paper.entity.RefElements;
import com.wzy.paper.extract.core.FeatureProcessor;
import com.wzy.paper.util.ListUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParseTest {
    @Test
    public void test() {
        String filePath = "C:\\Users\\Administrator\\Desktop\\CRF++\\test.txt";

        FeatureProcessor parseTrainedTxt = new FeatureProcessor();
        try {
            List<List> featureList = parseTrainedTxt.parseTxt2FeatureList(filePath);
            List<RefElements> refElementsList = parseTrainedTxt.packFeatureList2Ref(featureList.get(0), featureList.get(featureList.size() - 1));
            ListUtil.println(refElementsList);
            FileUtils.write(new File("C://pack2.txt"), ListUtil.list2String(refElementsList, "\r\n"), "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
