package com.wzy.paper.search.service.impl;

import com.wzy.paper.entity.Reference;
import com.wzy.paper.search.rule.Rule;
import com.wzy.paper.search.exception.SearchException;
import com.wzy.paper.search.service.EvidenceCrawlerService;
import com.wzy.paper.search.util.RuleUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考文献证据库爬取服务类
 *
 * @author wzy
 * @version 1.0
 * @date 2016.十一月.21 03:49 下午
 */
@Service("EvidenceCrawlerService")
public class EvidenceCrawlerServiceImpl implements EvidenceCrawlerService {

    /**
     * 搜索参考文献证据服务
     *
     * @param originRef
     * @param n
     * @return
     * @throws IOException
     */
    public List<Reference> searchRef(String originRef, int n) throws IOException, SearchException {
        Rule rule = new Rule("http://xueshu.baidu.com/s",
                             new String[] { "word" },
                             new String[] { originRef },
                             Rule.GET);

        // 通过rule得到documnet
        RuleUtil util     = new RuleUtil(rule);
        Document document = util.getDocumet();

        if (document == null) {
            return null;
        }

        // 获取参考文献links
        ArrayList<String> links = getABSLinks(document, n);

        if ((links == null) || (links.size() == 0)) {
            return null;
        }

        // 获取参考文献集
        ArrayList<Reference> articles = new ArrayList<Reference>();

        for (int i = 0; i < links.size(); i++) {
            Reference article = new Reference();

            article = getArticle(links.get(i));

            if (article != null) {
                articles.add(article);
            }
        }

        return articles;
    }

    /**
     * 根据link获取单个参考文献的信息
     *
     * @param link
     * @return Article
     * @throws IOException
     */
    private Reference getArticle(String link) throws IOException {
        if (link.equals("") || (link == null)) {
            return null;
        }

        Reference article = new Reference();

        article.setLink(link);

        //直接在百度学术搜索获得的信息，可能不全面，需通过文章链接获取更详细的信息
        Document doc = Jsoup.connect(link) // 设置代理伪装成浏览器
                .header("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                .timeout(10000)
                .get();

        if (doc == null) {
            return null;
        }

        // 获取title
        Elements h3    = doc.select("h3");
        String   title = h3.get(0).getElementsByTag("a").text();

        article.setTitle(title);

        // 获取其他元素
        Elements content = doc.select("div.c_content.content_hidden");

        // 获取author
        Elements author_wr     = content.get(0).getElementsByClass("author_wr");
        Elements p_authors     = author_wr.select("p.author_text");
        Elements a_authors     = p_authors.select("a");
        Elements links_authors = p_authors.select("a");
        String   author        = "";
        int      i             = 0;

        for (Element element : links_authors) {
            if (i == 0) {
                author += element.text();
            } else {
                author += "," + element.text();
            }

            i++;
        }

        article.setAuthor(author);

        // 获取summary
        Elements abstract_wr   = content.select("div.abstract_wr");
        Elements p_abstract    = abstract_wr.select("p.abstract");
        String   abstract_text = p_abstract.get(0).text();

        article.setSummary(abstract_text);

        // 获取source
        Elements publish_wr = content.select("div.publish_wr");
        Elements p_publish  = publish_wr.select("p.publish_text");
        Elements a_publish  = p_publish.select("a");
        String   publisher  = a_publish.text();

        article.setSource(publisher);

        // 获取time
        Elements span_publish = p_publish.select("span");
        String   publish_time = "";

        i = 0;

        for (Element element : span_publish) {
            if (i == 0) {
                publish_time += element.text();
            } else {
                publish_time += "," + element.text();
            }

            i++;
        }

        article.setTime(publish_time);

        return article;
    }

    /**
     * 获取参考文献绝对Links
     *
     * @param document
     * @param n        可以自定义数量(最多10个：百度学术一页所有数据）
     * @return
     */
    private ArrayList<String> getABSLinks(Document document, int n) {
        if ((document == null) || (n <= 0)) {
            return null;
        }

        ArrayList<String> linkLists = new ArrayList<String>();
        Elements          eles      = document.select("div.sc_content");

        // 获取文章绝对link
        Elements ele_links = eles.select("h3.t.c_font");

        if (ele_links == null) {
            return null;
        }

        for (int i = 0; i < ele_links.size(); i++) {
            Element link       = ele_links.get(i);
            String  title_link = link.getElementsByTag("a").attr("abs:href");

            linkLists.add(title_link);

            if (i >= n - 1) {
                break;
            }
        }

        return linkLists;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
