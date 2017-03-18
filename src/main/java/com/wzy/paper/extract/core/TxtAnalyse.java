package com.wzy.paper.extract.core;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wzy
 * @version 1.0
 * @date 2016.十一月.15 04:20 下午
 */
public class TxtAnalyse {

    /**  */
    private static final String pageStart = "--pageStart--";

    /**  */
    private static final String paraStart = "--paraStart--";

    /**  */
    private static final String lineEnd = "\n";

    /**  */
    private static final String endChars = "！？。.!”";

    /**  */
    private static final int titleLenTh = 10;

    /**  */
    private static final int textLenTh = 20;

    /**  */
    private static TxtAnalyse m_instance = null;

    /**
     * XML十六进制无效字符的处理
     *
     * @param str
     * @return
     */
    public static String RemainFW(String str) {
        StringBuilder sb = new StringBuilder();

        for (char ss : str.toCharArray()) {
            if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss <= 32)) || (ss == 127)) {
                sb.append("");    // &#x{0:X};
            } else if ((ss == '。') || (ss == '，')) {
                sb.append("");    // &#x{0:X};
            } else {
                sb.append(ss);
            }
        }

        return sb.toString();
    }

    /**
     * XML十六进制无效字符的处理
     *
     * @param tmp
     * @return
     */
    public static String ReplaceLowOrderASCIICharacters(String tmp) {
        StringBuilder info = new StringBuilder();

        for (int i = 0; i < tmp.length(); i++) {
            char cc = tmp.charAt(i);
            int  ss = (int) cc;

            if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss <= 32)) || (ss == 127)) {
                info.append(" ");    // &#x{0:X};
            } else {
                info.append(cc);
            }
        }

        return info.toString();    // .Trim();
    }

    /**
     * @param content
     * @return
     */
    public static String dealPdfContent(String content) {
        StringBuffer strBuf = new StringBuffer();

        if (!useParaStart(content)) {

            // return null;
            content = content.replaceAll(pageStart, "");
            content = content.replaceAll(paraStart, "\r\n");
            content = getInfoFromTxt(content);

            return content;
        } else {

//          content=TxtAnalyse.getInfoFromTxt(content);
            String[] lines = reviseParaStart(content);
            String[] res   = filterDocument(lines).split(paraStart);

            for (String r : res) {
                strBuf.append(r);
                strBuf.append("\r\n");
            }

            return strBuf.toString();
        }
    }

    /**
     * @param lines
     * @return
     */
    private static String filterDocument(String[] lines) {
        StringBuffer strBuf = new StringBuffer();
        int          start  = 0;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].indexOf(pageStart) >= 0) {
                strBuf.append(filterPage(lines, start, i));
                start = i + 1;
            }
        }

        if (start < lines.length) {
            strBuf.append(filterPage(lines, start, lines.length));
        }

        return strBuf.toString();
    }

    private static boolean useParaStart(String content)
    {
        String[] lines = content.split(lineEnd);
        int sum = 0;
        int count = 0;
        for(int i=0; i<lines.length; i++)
        {
            lines[i] = lines[i].trim();
        }
        for(int i=0; i<lines.length; i++)
        {
            if(!lines[i].isEmpty())
            {
                sum++;
                if(lines[i].startsWith(paraStart))
                {
                    count++;
                }
            }
        }
        return count < sum * 0.5;
    }

    /**
     * @param lines
     * @param from
     * @param to
     * @return
     */
    private static String filterPage(String[] lines, int from, int to) {
        StringBuffer strBuf = new StringBuffer();
        int          start  = from;

        for (int i = from; i < to - 1; i++) {
            if (lines[i].startsWith(paraStart) && lines[i + 1].startsWith(paraStart)) {
                start = i + 1;
            } else {
                break;
            }
        }

        for (int i = start; i < to; i++) {
            strBuf.append(lines[i]);
        }

        return strBuf.toString().startsWith(paraStart)
               ? strBuf.toString().substring(paraStart.length())
               : strBuf.toString();
    }

    // revise the position of paraStart

    /**
     * @param content
     * @return
     */
    private static String[] reviseParaStart(String content) {
        String[] lines = content.split(lineEnd);

        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }

        for (int i = 0; i < lines.length; i++) {
            String currentLine = lines[i];

            if (currentLine.startsWith(paraStart) && (i >= 2)) {
                String preLine    = lines[i - 1];
                String prepreLine = lines[i - 2];

                if (!preLine.startsWith(paraStart)
                        &&!isTitle(currentLine)
                        && isParagraphFirst(preLine)
                        && isParagraphLast(prepreLine)) {
                    lines[i]     = currentLine.substring(paraStart.length());
                    lines[i - 1] = paraStart + preLine;
                }
            }
        }

        return lines;
    }

    /**
     * 消除文章的虚假换行空格
     *
     * @param data
     * @return
     */
    public static String getInfoFromTxt(String data) {

//      Pattern pattern=Pattern.compile("[.。）\\);；：:](( )*)\\r\\n");
        Pattern pattern = Pattern.compile("[.。：:](\\s*)\\r\\n");
        Matcher matcher = pattern.matcher(data);

        data = matcher.replaceAll("#####");

        Pattern pattern2 = Pattern.compile("\\r\\n");
        Matcher matcher2 = pattern2.matcher(data);

        data = matcher2.replaceAll("");
        data = data.replaceAll("#####", "。\r\n");

        Pattern pattern4 = Pattern.compile("(?<=[^\\x00-\\xff])\\s(?=[^\\x00-\\xff])");
        Matcher matcher4 = pattern4.matcher(data);

        data = matcher4.replaceAll("");

        return data;
    }

    /**
     * @param line
     * @return
     */
    private static boolean isParagraphFirst(String line) {
        if (line.startsWith(paraStart)) {
            line = line.substring(paraStart.length());
        }

        return line.length() > textLenTh;
    }

    /**
     * @param line
     * @return
     */
    private static boolean isParagraphLast(String line) {
        if (line.isEmpty()) {
            return true;
        }

        char end = line.charAt(line.length() - 1);

        if ((line.length() < textLenTh) || (endChars.indexOf(end) >= 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param line
     * @return
     */
    private static boolean isTitle(String line) {
        if (line.startsWith(paraStart)) {
            line = line.substring(paraStart.length());
        }

        return line.length() < titleLenTh;
    }

    /**
     * @return
     */
    public static TxtAnalyse getTxtAnalyse() {
        if (m_instance == null) {
            m_instance = new TxtAnalyse();
        }

        return m_instance;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
