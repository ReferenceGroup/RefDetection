package com.wzy.paper.extract;

import com.wzy.paper.BaseJunit4Test;
import com.wzy.paper.extract.core.FeatureProcessor;
import com.wzy.paper.extract.service.ExtractService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FeatureListTest  extends BaseJunit4Test {
    @Autowired
    private FeatureProcessor featureProcessor;


    @Test
    public void modifyFeatureList(){
        try {
            //更改训练集，去除第二个特征
//            List<List> featureList= featureProcessor.parseTxt2FeatureList("C:\\Users\\Administrator\\Desktop\\CRF++\\train.data");
//            featureList.remove(1);
//            featureProcessor.writeFeatureList2File(new File("C:\\Users\\Administrator\\Desktop\\CRF++\\train3.data"),featureList,false);

            List<List> featureList= featureProcessor.parseTxt2FeatureList("C:\\Users\\Administrator\\Desktop\\CRF++\\test.data");
            featureList.remove(1);
            featureProcessor.writeFeatureList2File(new File("C:\\Users\\Administrator\\Desktop\\CRF++\\test2.data"),featureList,false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
