package com.wzy.paper.search.util;

import com.wzy.paper.search.rule.Rule;
import com.wzy.paper.search.exception.SearchException;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RuleUtil {

    public static  final  int TIME_OUT=10000;//10秒
    public static Rule rule;


    public RuleUtil(Rule rule) {
        // TODO Auto-generated constructor stub
        this.rule = rule;
    }

    /**
     * 通过rule得到document,并对rule进行验证
     *
     * @return
     */
    public Document getDocumet() throws SearchException {
        // 进行对rule的必要校验

        if (!validateRule(rule)) {
            return null;
        }
        Document doc = null;
        try {
            /**
             * 解析rule
             */
            String url = rule.getUrl();
            String[] params = rule.getParams();
            String[] values = rule.getValues();
            int requestType = rule.getRequestMoethod();

            Connection conn = Jsoup.connect(url);
            // 设置查询参数

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    conn.data(params[i], values[i]);
                }
            }

            // 设置请求类型
            switch (requestType) {
                case Rule.GET:
                    doc = conn.timeout(TIME_OUT)  // 设置代理伪装成浏览器
                            .header("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                            .get();
                    break;
                case Rule.POST:
                    doc = conn.timeout(TIME_OUT)  // 设置代理伪装成浏览器
                            .header("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                            //.post(); //不能获取
                            .get();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return doc;
    }

    /**
     * 对传入的参数进行必要的校验
     */
    private static boolean validateRule(Rule rule) throws SearchException {
        String url = rule.getUrl();
        if (StringUtils.isEmpty(url)) {
            throw new SearchException("url不能为空！");
        }
        if (!url.startsWith("http://")) {
            throw new SearchException("url的格式不正确！");
        }

        if (rule.getParams() != null && rule.getValues() != null) {
            if (rule.getParams().length != rule.getValues().length) {
                throw new SearchException("参数的键值对个数不匹配！");
            }
        }
        return true;

    }

}
