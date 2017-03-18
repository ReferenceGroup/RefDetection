import com.wzy.paper.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;


public class TemplateNumber {
    @Test
    public void test(String aa) throws IOException {
        String filePath = "C:\\Users\\Administrator\\Desktop\\t3.txt";
        List<String> lineList = FileUtils.readLines(new File(filePath), "GBK");

        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lineList.size(); i++) {
            String line = lineList.get(i);

            if (line.startsWith("U")) {
                String number = (index >= 10 ? index + "" : "0" + index);
                line = "U" + number + line.substring(3);
                index++;
            }
            sb.append(line + "\r");

        }
        FileUtils.write(new File(filePath), sb.toString(), "GBK");

    }

    /**
     * @throws IOException
     */
    @Test//相似度计算
    public void test2() throws IOException {
        String a = "吕学强. 基于条件随机场的中文命名实体识别的研究. 中国科学技术大学 , 2009";
        String b = "张佳宝. 基于条件随机场的中文命名实体识别研究[D]. 国防科学技术大学, 2010.";

        a = StringUtil.getfromSen(a);
        b = StringUtil.getfromSen(b);

        HashSet hashSet = new HashSet();
        for (int i = 0; i < a.length(); i++) {
            hashSet.add(a.charAt(i));
        }

        double xsNum = 0;
        for (int i = 0; i < b.length(); i++) {
            if (hashSet.contains(b.charAt(i))) {
                xsNum++;
            }
        }

        if (xsNum >= a.length()) {
            System.out.println("1");
        } else {
            System.out.println("" + xsNum / a.length());
        }

    }
}
