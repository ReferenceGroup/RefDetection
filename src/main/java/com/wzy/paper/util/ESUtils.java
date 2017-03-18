package com.wzy.paper.util;

import com.wzy.paper.search.entity.ESReference;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author wzy
 * @Date 2016/11/15 11:22
 */
public class ESUtils {
    static Pattern badChars = Pattern.compile("\\s*[\\s~!\\^&\\(\\)\\-\\+:\\|\\\\\"\\\\$]+\\s*");
    private static Client client = ClientFactory.getClient();

    /**
     * 关闭对应client
     *
     * @param client
     */
    public static void close(Client client) {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
            }
            client = null;
        }
    }


    public static void optimize(String indexName) throws Exception {
        client.admin().indices().prepareOptimize(indexName).execute();
    }

    public static void flush() throws Exception {
        client.admin().indices().flush(new FlushRequest()).actionGet();
    }

    public static boolean indicesExists(String indexName) {
        return indicesExists(client, indexName);
    }

    public static boolean indicesExists(Client client, String indexName) {
        IndicesExistsRequest ier = new IndicesExistsRequest();
        ier.indices(new String[]{indexName.toLowerCase()});

        return client.admin().indices().exists(ier).actionGet().isExists();
    }

    public static boolean typesExists(Client sClient, String indexName, String indexType) {
        if (indicesExists(sClient, indexName)) {
            TypesExistsRequest ter = new TypesExistsRequest(new String[]{indexName.toLowerCase()}, indexType);
            return sClient.admin().indices().typesExists(ter).actionGet().isExists();
        }
        return false;
    }

    public static void createIndex(String indexName, String indexType) throws IOException {
        Map<String, Object> map = Maps.newHashMap();
        map.put("number_of_replicas", "0");

        Settings settings = ImmutableSettings.settingsBuilder().put(map).build();
        IndicesExistsRequest ier = new IndicesExistsRequest();
        ier.indices(new String[]{indexName.toLowerCase()});

        boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
        if (!exists) {
            client.admin().indices().prepareCreate(indexName.toLowerCase()).setSettings(settings).execute().actionGet();
        }
    }

    public static boolean createArticleIndex(String indexName) throws IOException {
        IndicesExistsRequest ier = new IndicesExistsRequest();
        ier.indices(new String[]{indexName.toLowerCase()});
        Map<String, Object> map = Maps.newHashMap();
        map.put("number_of_replicas", "1");

        Settings settings = ImmutableSettings.settingsBuilder().put(map).build();

        boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
        if (!exists) {
            CreateIndexResponse response = client.admin().indices().prepareCreate(indexName.toLowerCase())
                    .setSettings(settings).execute().actionGet();
            if (!typesExists(client, indexName, "Article"))
                createArticleMapping(indexName);
            return response.isAcknowledged();
        } else {
            return true;
        }
    }

    public static boolean createSentenceIndex(String indexName) throws IOException {
        return createSentenceIndex(indexName, client);
    }

    public static boolean createSentenceIndex(String indexName, Client sClient) throws IOException {
        IndicesExistsRequest ier = new IndicesExistsRequest();
        ier.indices(new String[]{indexName.toLowerCase()});

        boolean exists = sClient.admin().indices().exists(ier).actionGet().isExists();
        if (!exists) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("number_of_replicas", "0");
            map.put("index.refresh_interval", "-1");//小库快速删减，不需刷新
//			map.put("index.translog.flush_threshold_ops", "100000");
            map.put("index.store.type", "memory");

            Settings settings = ImmutableSettings.settingsBuilder().put(map).build();
            CreateIndexResponse response = sClient.admin().indices().prepareCreate(indexName.toLowerCase())
                    .setSettings(settings).execute().actionGet();
            if (!typesExists(sClient, indexName, "SamllSentence")) {
                // createSentenceMapping(sClient,indexName);
            }
            return response.isAcknowledged();
        } else {
            return true;
        }
    }

    public static boolean createArticleMapping(String indexName) throws IOException {
        PutMappingResponse response = client.admin().indices().preparePutMapping(indexName).setType("article")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("article").startObject("properties")
                        .startObject("id").field("type", "string").field("index", "not_analyzed").field("store", "no")
                        .startObject("title").field("type", "string").field("index", "analyzed").field("store", "yes")
                        .startObject("keyword").field("type", "string").field("index", "analyzed").field("store", "yes")
                        .startObject("remark").field("type", "string").field("index", "analyzed").field("store", "yes")
                        .startObject("author").field("type", "string").field("index", "not_analyzed")
                        .field("store", "no").startObject("content").field("type", "string").field("index", "analyzed")
                        .field("store", "yes").startObject("times").field("type", "integer").field("index", "no")
                        .field("store", "no")

                        .endObject().endObject().endObject().endObject().endObject().endObject().endObject().endObject().endObject()
                        .endObject())
                .execute().actionGet();
        return response.isAcknowledged();
    }

    public static boolean createSentenceMapping(Client scClient, String indexName) throws IOException {
        PutMappingResponse response = scClient.admin().indices().preparePutMapping(indexName).setType("SamllSentence")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("SmallSentence")
                        .startObject("properties").startObject("id").field("type", "string")
                        .field("index", "not_analyzed").field("store", "no")
                        // .startObject("paperid").field("type",
                        // "string").field("index",
                        // "not_analyzed").field("store", "yes")
                        .startObject("title").field("type", "string").field("index", "analyzed").field("store", "yes")
                        .startObject("originSentence").field("type", "string").field("index", "analyzed")
                        .field("store", "yes").startObject("foreSentence").field("type", "string").field("index", "no")
                        .field("store", "yes").startObject("behindSentence").field("type", "string")
                        .field("index", "no").field("store", "yes").startObject("length").field("type", "integer")
                        .field("index", "no").field("store", "no").startObject("shingle").field("type", "string")
                        .field("index", "no").field("store", "yes")
                        // .startObject("existXs").field("type",
                        // "boolean").field("index", "no").field("store", "no")
                        // .startObject("maxXsl").field("type",
                        // "float").field("index", "no").field("store", "no")
                        .endObject().endObject().endObject().endObject().endObject().endObject().endObject().endObject()
                        .endObject()
                        // .endObject()
                        // .endObject()
                        // .endObject()
                        .endObject())
                .execute().actionGet();
        return response.isAcknowledged();
    }

    /**
     * 根据索引名称删除索引
     *
     * @param indexName 索引名称
     */
    public static void deleteIndex(String indexName) {

        IndicesExistsRequest ier = new IndicesExistsRequest();
        ier.indices(new String[]{indexName.toLowerCase()});

        boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
        if (exists) {
            client.admin().indices().prepareDelete(indexName.toLowerCase()).execute().actionGet();
        }

    }

    public synchronized static void indexReference(String indexName, String typeNmae, List<?> entitys) throws IOException {
        index(indexName, typeNmae, entitys, client);
    }

    public static void index(String indexName, String typeNmae, List<?> entitys, Client scClient) throws IOException {
        if (entitys == null || entitys.size() == 0)
            return;
        BulkRequestBuilder bulkRequest = scClient.prepareBulk();
        for (Object object : entitys) {
            String json = JsonUtil.generateJson(object);
            IndexRequest request = scClient.prepareIndex(indexName, typeNmae).setSource(json).request();
            bulkRequest.add(request);
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            System.out.println("批量创建索引错误！");
        }

    }

    /**
     * 过滤自我文章查询句子
     *
     * @param indexName
     * @param indexTypes
     * @param fields
     * @param queryString
     * @param from
     * @param size
     * @param valueType
     * @return
     * @throws Exception
     */
    public static List<Object> search(String indexName, String indexTypes, String fields, String queryString,
                                      int from, int size, Class<?> valueType) throws Exception {
        List<FilterBuilder> filterBuilders = new ArrayList<FilterBuilder>();
        filterBuilders.add(FilterBuilders.termFilter("paperId", indexName));

        return search(indexName, indexTypes, fields, queryString, filterBuilders, from, size, valueType);
    }

    /**
     * 搜索参考文献，返回一个最相近证据参考文献
     *
     * @param originId  参考文献原文ID
     * @param reference 参考文献原文
     * @return
     * @throws Exception
     */
    public static List<Object> searchReference(String originId, String reference) throws Exception {
//        List<FilterBuilder> filterBuilders = Lists.newArrayList();
//        filterBuilders.add(FilterBuilders.inFilter().termFilter("originId", originId));

        return search("esreferences", "esreference", "originId", "evidenceRef", originId, reference, 0, 1, ESReference.class);
    }

    public static List<Object> search(String indexName, String indexTypes, String field1, String field2, String originId, String reference,
                                      int from, int size, Class<?> valueType) throws Exception {
        indexName = indexName.toLowerCase();
        client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName);
        searchRequestBuilder.setTypes(indexTypes);

        if (StringUtils.isBlank(originId) || StringUtils.isBlank(reference)) return null;
        reference = QueryParser.escape(reference);

        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(originId).defaultField(field1)).should(QueryBuilders.queryStringQuery(reference).defaultField(field2));
        searchRequestBuilder.setQuery(booleanQuery).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from)
                .setSize(size).setExplain(false);

        SearchHits searchHits = searchRequestBuilder.execute().actionGet().getHits();
        return swapResult(searchHits, valueType);
    }

    public static List<Object> search(String indexName, String indexTypes, String fields, String queryString,
                                      List<FilterBuilder> filterBuilders, int from, int size, Class<?> valueType) throws Exception {
        indexName = indexName.toLowerCase();
        client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName);
        searchRequestBuilder.setTypes(indexTypes);

        if (StringUtils.isBlank(queryString)) return null;
        queryString = QueryParser.escape(queryString);

        if (filterBuilders != null && filterBuilders.size() > 0) {
            BoolFilterBuilder boolFilterBuilder = null;
            for (FilterBuilder filterBuilder : filterBuilders) {
                boolFilterBuilder = FilterBuilders.boolFilter().must(filterBuilder);//限定此条件
            }
            searchRequestBuilder.setQuery(QueryBuilders.filteredQuery(QueryBuilders.queryStringQuery(queryString).defaultField(fields), boolFilterBuilder)).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from)
                    .setSize(size).setExplain(false);
        } else {
            searchRequestBuilder.setQuery(QueryBuilders.queryStringQuery(queryString).defaultField(fields)).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from)
                    .setSize(size).setExplain(false);
        }

        SearchHits searchHits = searchRequestBuilder.execute().actionGet().getHits();
        return swapResult(searchHits, valueType);
    }

    public static HashMap<String, Long> search(String indexName, String indexTypes, String fields, String queryString,
                                               List<FieldSortBuilder> sortBuilders, int from, int size, Class<?> valueType, String aggField)
            throws Exception {
        HashMap<String, Long> hashMap = new HashMap<String, Long>();

        indexName = indexName.toLowerCase();
        client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName);
        searchRequestBuilder.setTypes(indexTypes);

        searchRequestBuilder.setQuery(QueryBuilders.queryStringQuery(queryString).field(fields))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from).setSize(size).setExplain(false)
                .addAggregation(AggregationBuilders.terms("aggName").field(aggField));
        SearchResponse sr = searchRequestBuilder.execute().actionGet();

        TopHits topHits = sr.getAggregations().get("aggName");
        for (SearchHit sh1 : topHits.getHits()) {
            System.out.println(sh1.sourceAsMap().get("akc194").toString());
        }

        Terms terms = sr.getAggregations().get("aggName");
        List<Terms.Bucket> collection = terms.getBuckets();
        for (Terms.Bucket bucket : collection) {
            String region = bucket.getKey();
            long count = bucket.getDocCount();
            hashMap.put(region, count);
        }
        return hashMap;
    }


    public static List<Object> swapResult(SearchHits hits, Class<?> valueType) throws Exception {
        List<Object> articles = new ArrayList<Object>();

        if (hits == null || hits.getTotalHits() <= 0) {
            return null;
        }

        for (int i = 0; i < hits.hits().length; i++) {
            SearchHit hit = hits.getAt(i);
            String json = hit.getSourceAsString();
            Object article = JsonUtil.generateObject(json, valueType);
            articles.add(article);
        }

        return articles;
    }

    public static Map<String, Object> swapResult(GetResponse response) {
        if (response == null || !response.isExists()) {
            return null;
        }

        Map<String, Object> rowData = response.getSourceAsMap();
        rowData.put("_index", response.getIndex());
        rowData.put("_type", response.getType());
        rowData.put("_id", response.getId());

        return rowData;
    }
}
