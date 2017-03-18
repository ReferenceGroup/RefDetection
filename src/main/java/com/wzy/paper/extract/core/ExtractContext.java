package com.wzy.paper.extract.core;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wzy
 * @version 1.0
 * @date 2016.十一月.15 04:18 下午
 */
@Component
public class ExtractContext {

    /**  */
    private static final String pageStart = "--pageStart--";

    /**  */
    private static final String paraStart = "--paraStart--";

    /**
     * @param filepath
     * @return
     * @throws Exception
     */
    private static String getTxtfromTxt(String filepath) throws Exception {
        String result = null;

        result = new Tika().parseToString(new File(filepath));

        return result;
    }

    /**
     * @param path
     * @return
     * @throws IOException
     * @throws TikaException
     */
    public String extractTxt(String path) throws IOException, TikaException {
        String txt = null;

        txt = new Tika().parseToString(new File(path));

        // 消除乱码
        Pattern pattern = Pattern.compile("\\x00\\x0b-\\x0c\\x0e-\\x1f\\uD800-\\uDFFF]");
        Matcher matcher = pattern.matcher(txt);

        matcher.replaceAll("");
        txt = TxtAnalyse.ReplaceLowOrderASCIICharacters(txt);

        return txt;
    }

    /**
     * 引用pdfbox.jar中的方法处理pdf文档，获得返回内容
     *
     * @param filepath
     * @return
     * @throws Exception
     */
    private String getTxtfromPdf(String filepath) throws Exception {

//      PDDocument doc = null;
//      String result = "";
//
//      try {
//          FileInputStream fileInputStream = new FileInputStream(filepath);
////          PDFParser parser = new PDFParser(fileInputStream);
//
//          parser.parse();
//          doc = parser.getPDDocument();
//
//          if (doc.isEncrypted()) {
//              return null;
//          }
//
//          PDFTextStripper stripper = new PDFTextStripper();
//
//          stripper.setPageEnd(pageStart);
//          stripper.setParagraphStart(paraStart);
//          result = stripper.getText(doc);
//      } catch (Exception e) {
//          e.printStackTrace();
//
//          throw e;
//      } finally {
//          if (doc != null) {
//              try {
//                  doc.close();
//              } catch (Exception e) {
//                  e.printStackTrace();
//
//                  throw e;
//              } finally {
//                  doc = null;
//              }
//          }
//      }
//
//      return result;
        return null;
    }

    /**
     * @param filepath
     * @return
     * @throws Exception
     */
    private String getTxtfromWord(String filepath) throws Exception {
        String text = null;

        text = new Tika().parseToString(new File(filepath));

        return text;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
