package com.caychen.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class ElasticSearchQueryTest {

    private static RestHighLevelClient esClient;

    @BeforeEach
    public void initEsClient() {
        esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

        Assertions.assertNotNull(esClient);
        System.out.println("连接成功...");
    }

    /**
     * 全量查询
     *
     * @throws IOException
     */
    @Test
    public void testQueryAll() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("person");
        request.source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchAllQuery()));

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        long value = hits.getTotalHits().value;
        if (value != 0) {
            SearchHit[] searchHits = hits.getHits();

            Arrays.stream(searchHits).forEach(hit -> {
                String sourceAsString = hit.getSourceAsString();
                System.out.println(sourceAsString);
            });
        } else {
            System.out.println("未找到相关数据...");
        }
    }

    /**
     * 条件查询
     *
     * @throws IOException
     */
    @Test
    public void testQueryCondition() throws IOException {

        SearchRequest request = new SearchRequest();
        request.indices("person");

        //1、matchQuery：模糊查询
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders.matchQuery("name", "三")));

        //2、termQuery： 匹配查询
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders.termQuery("age", 24)));

        //3、分页
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .from(1)
                        .size(1)
                        .query(
                                QueryBuilders.matchAllQuery()));

        //4、排序
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .sort("age", SortOrder.DESC)
                        .query(
                                QueryBuilders.matchAllQuery()));

        //5、字段过滤
        String[] includes = new String[]{"name"};//包含的字段
        String[] excludes = new String[]{"age"};//不包含的字段
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .fetchSource(includes, excludes)
                        .query(
                                QueryBuilders.matchAllQuery()));

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        long value = hits.getTotalHits().value;
        if (value != 0) {
            SearchHit[] searchHits = hits.getHits();

            Arrays.stream(searchHits).forEach(hit -> {
                String sourceAsString = hit.getSourceAsString();
                System.out.println(sourceAsString);
            });
        } else {
            System.out.println("未找到相关数据...");
        }
    }

    /**
     * 组合条件查询
     *
     * @throws IOException
     */
    @Test
    public void testQueryMultiCondition() throws IOException {

        SearchRequest request = new SearchRequest();
        request.indices("person");

        //1、must
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders
                                        .boolQuery()
                                        .must(
                                                QueryBuilders.matchQuery("name", "三"))
                                        .must(
                                                QueryBuilders.matchQuery("gender", "男的"))));

        //2、should
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders
                                        .boolQuery()
                                        .should(//可以有五
                                                QueryBuilders.matchQuery("name", "五"))
                                        .must(//必须是男的
                                                QueryBuilders.matchQuery("gender", "男的"))));

        //3、range
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders
                                        .boolQuery()
                                        .filter(
                                                QueryBuilders.rangeQuery("age")
                                                        .gte(25)
                                                        .lt(30))));

        //4、模糊查询
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders
                                        .fuzzyQuery("name", "三")
                                        .fuzziness(Fuzziness.ZERO)));//0：不能缺，1：差一个，2：差两个

        //5、高亮查询
        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .query(
                                QueryBuilders.termQuery("name", "三"))
                        .highlighter(
                                SearchSourceBuilder.highlight()
                                        .field("name")
                                        .preTags("<font color='red'>")
                                        .postTags("</font>")));

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        long value = hits.getTotalHits().value;
        if (value != 0) {
            SearchHit[] searchHits = hits.getHits();

            Arrays.stream(searchHits).forEach(hit -> {
                String sourceAsString = hit.getSourceAsString();
                System.out.println(sourceAsString);
            });
        } else {
            System.out.println("未找到相关数据...");
        }
    }

    /**
     * 聚合查询
     *
     * @throws IOException
     */
    @Test
    public void testQueryAggs() throws IOException {

        SearchRequest request = new SearchRequest();
        request.indices("person");

        //1、aggs max
        // max --> Max
        // min --> Min
        // terms --> Terms
        // avg --> Avg
//        request.source(
//                SearchSourceBuilder
//                        .searchSource()
//                        .aggregation(AggregationBuilders.max("max_age").field("age")));
//
//        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
//        Aggregations aggregations = response.getAggregations();
//        Max max = aggregations.get("max_age");
//        System.out.println(max.getValue());

//        request.source(
//                SearchSourceBuilder
//                        .searchSource()
//                        .aggregation(AggregationBuilders.avg("avg_age").field("age")));
//
//        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
//        Aggregations aggregations = response.getAggregations();
//        Avg avg = aggregations.get("avg_age");
//        System.out.println(avg.getValue());

        request.source(
                SearchSourceBuilder
                        .searchSource()
                        .aggregation(
                                AggregationBuilders.terms("age_group")
                                        .field("age")
                                        //对聚合结果再排序
                                        .order(BucketOrder.aggregation("_key", true))));

        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Terms terms = aggregations.get("age_group");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        buckets.stream().forEach(b -> {
            System.out.println(b.getKey() + " =====> " + b.getDocCount());
        });

    }


    @AfterEach
    public void closeEsClient() throws IOException {
        if (esClient != null) {
            System.out.println("关闭连接...");
            esClient.close();
        }
    }
}
