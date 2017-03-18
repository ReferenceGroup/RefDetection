package com.wzy.paper;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUtilsTest {
    @Test
    public void testRead() throws IOException {
        String filePath="C:\\Users\\Administrator\\Desktop\\CRF++\\test.txt";
        String string1= FileUtils.readFileToString(new File(filePath),"GBK");
        System.out.println(string1);
        List<String> string2= FileUtils.readLines(new File(filePath),"GBK");
        String[] string3=string2.get(0).split("\t");
        System.out.println(string2);
    }
}
