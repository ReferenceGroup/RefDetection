package com.wzy.paper.util;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @version        1.0
 * @author         wzy
 * @date           2016.十一月.23 02:43 下午
 */
@Component
public class StringUtil {

    /**  */
    private static Hashtable<Character, Character> markHM = new Hashtable<Character, Character>();

    /**  */
    private static final Log LOG = LogFactory.getLog(StringUtil.class);

    /**
     * 载入用于切分句子的标点符号
     */
    static {
        char[] f = {
            '.', '。', ';', '；', '？', '?', '！', '!', '．', ',', '，'
        };    // 短句

        for (int i = 0; i < f.length; i++) {
            markHM.put(f[i], f[i]);
        }
    }

    /**
     * 提取字符串中字符
     *
     * @param ref
     * @return
     */
    public static List<String> getCharacter(String ref) {
        List<String> characters = Lists.newArrayList();

        if (org.apache.commons.lang.StringUtils.isEmpty(ref)) {
            return characters;
        }

        for (int i = 0; i < ref.length(); i++) {
            characters.add(ref.charAt(i) + "");
        }

        return characters;
    }

    /**
     * 过滤标点后，比较两个字符串中字符是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isCharEqualsOfTwoString(String a, String b) {
        a = getfromSen(a);
        b = getfromSen(b);

        return a.equals(b);
    }



    /**
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String  content  = "[21] Y. Yang, M. Xie, T. N. Goh, Statistical Analysis of a Weibull Extension Model, Communications in Statistics Theory and Methods, Vol.32, No.5: 913-928（2003）";

        System.out.println(isChineseString(content));
    }


    /**
     * 删除字符串开头非中英文字符
     * @param string
     * @return
     */
    public static String subForeSen(String string) {
        for (int i = 0; i <string.length(); i++) {
            char c=string.charAt(i);
            if (isChinese(c)||isLetter(c)) {
               string=string.substring(i);
                return string;
            }
        }

        return "";
    }

    /**
     * 从一段话中提取字符
     *
     * @param sen
     * @return
     */
    public static String getfromSen(String sen) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sen.length(); i++) {
            if (isChinese(sen.charAt(i))
                    || isLetter(sen.charAt(i))
                    || StringUtils.isNumeric(String.valueOf(sen.charAt(i)))) {
                sb.append(sen.charAt(i));
            }
        }

        return sb.toString();
    }

    /**
     * 删除中文中空格
     *
     * @param string
     * @return
     */
    public static String deleteChSpace(String string) {
        if ((string == null) || (string == "")) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        try {
            stringBuilder.append(string.charAt(0));
        } catch (Exception e) {
            System.out.println(string);
            e.printStackTrace();
            LOG.error(string + " " + e);
        }

        for (int i = 1; i < string.length() - 1; i++) {
            if ((string.charAt(i) == ' ') &&!isLetter(string.charAt(i - 1)) &&!isLetter(string.charAt(i + 1))) {
                continue;
            } else {
                stringBuilder.append(string.charAt(i));
            }
        }

        stringBuilder.append(string.charAt(string.length() - 1));

        return stringBuilder.toString();
    }

    /**
     * 是否为英文
     *
     * @param c
     * @return
     */
    public static boolean isLetter(char c) {
        if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
            return true;
        }

        return false;
    }

    /**
     * 是否为中文
     *
     * @param c
     * @return TODO
     */
    public static boolean isChinese(char c) {
        boolean result = false;

        if ((c >= 19968) && (c <= 171941)) {    // 汉字范围 \u4e00-\u9fa5 (中文)
            if ((c != '，') && (c != '。') && (c != '！') && (c != '；')&& (c != '（')&& (c != '）')) {
                result = true;
            }
        }

        return result;
    }

    public static boolean isChineseString(String input){
        if(org.apache.commons.lang.StringUtils.isEmpty(input)){
            return  false;
        }

        //字符串中中文个数
        int num=0;

        for(int i=0;i<input.length();i++){
            char c=input.charAt(i);
            if(isChinese(c)){
                num++;
            }
        }

        return ((double)num/input.length())>0.5;
    }

    // public static boolean isChinese(char c) {
    // Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    // if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub ==
    // Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
    // || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub
    // == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
    // || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub ==
    // Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    // || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
    // return true;
    // }
    // return false;
    // }
}


//~ Formatted by Jindent --- http://www.jindent.com
