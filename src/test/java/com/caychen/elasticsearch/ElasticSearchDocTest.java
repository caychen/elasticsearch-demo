package com.caychen.elasticsearch;

import com.caychen.elasticsearch.entity.Person;
import com.caychen.elasticsearch.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class ElasticSearchDocTest {

    private static RestHighLevelClient esClient;

    @BeforeEach
    public void initEsClient() {
        esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

        Assertions.assertNotNull(esClient);
        System.out.println("连接成功...");
    }

    /**
     * 单个新增
     *
     * @throws IOException
     */
    @Test
    public void testCreateDoc() throws IOException {
        User user = User.builder()
                .id(1L)
                .address("上海市浦东新区龙阳路111号")
                .age(20)
                .gendex(1)
                .name("zhangsan")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        IndexRequest indexRequest =
                new IndexRequest("user").id(user.getId().toString()).source(userJson, XContentType.JSON);

        IndexResponse response = esClient.index(indexRequest, RequestOptions.DEFAULT);

        DocWriteResponse.Result result = response.getResult();
        System.out.println(result);

    }

    /**
     * 根据id查询
     *
     * @throws IOException
     */
    @Test
    public void testGetDocById() throws IOException {

        GetRequest getRequest = new GetRequest("user", "3");

        GetResponse response = esClient.get(getRequest, RequestOptions.DEFAULT);

        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        sourceAsMap.forEach((k, v) -> {
            System.out.println(k + " ---------> " + v);
        });
    }

    /**
     * 更新文档
     *
     * @throws IOException
     */
    @Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("user", "1");
        request.doc(XContentType.JSON, "name", "TOM");
        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    /**
     * 删除文档
     *
     * @throws IOException
     */
    @Disabled
    @Test
    public void testDeleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("user", "5");
        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    /**
     * 批量新增
     *
     * @throws IOException
     */
    @Order(2)
    @Test
    public void testBatchCreateDoc() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        IndexRequest indexRequest1 = new IndexRequest("person").id("1").source(
                objectMapper.writeValueAsString(Person.builder().name("张三").gender("男的").tel("11111").age(30).build()),
                XContentType.JSON);

        IndexRequest indexRequest2 = new IndexRequest("person").id("2").source(
                objectMapper.writeValueAsString(Person.builder().name("李四").gender("男的").tel("22222").age(24).build()),
                XContentType.JSON);

        IndexRequest indexRequest3 = new IndexRequest("person").id("3").source(
                objectMapper.writeValueAsString(Person.builder().name("王五").gender("女的").tel("33333").age(29).build()),
                XContentType.JSON);


        IndexRequest indexRequest4 = new IndexRequest("person").id("4").source(
                objectMapper.writeValueAsString(Person.builder().name("莉莉").gender("女的").tel("5343232").age(21).build()),
                XContentType.JSON);

        IndexRequest indexRequest5 = new IndexRequest("person").id("5").source(
                objectMapper.writeValueAsString(Person.builder().name("肉四").gender("男的").tel("22323").age(25).build()),
                XContentType.JSON);

        IndexRequest indexRequest6 = new IndexRequest("person").id("6").source(
                objectMapper.writeValueAsString(Person.builder().name("三毛").gender("女的").tel("90032").age(24).build()),
                XContentType.JSON);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest1);
        bulkRequest.add(indexRequest2);
        bulkRequest.add(indexRequest3);
        bulkRequest.add(indexRequest4);
        bulkRequest.add(indexRequest5);
        bulkRequest.add(indexRequest6);

        BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        BulkItemResponse[] items = response.getItems();
    }

    /**
     * 批量修改
     *
     * @throws IOException
     */
    @Disabled
    @Test
    public void testBatchUpdateDoc() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        UpdateRequest updateRequest1 = new UpdateRequest("person", "1").doc(
                XContentType.JSON, "tel", "35454454");

        UpdateRequest updateRequest2 = new UpdateRequest("person", "2").doc(
                XContentType.JSON, "tel", "23323232");

        UpdateRequest updateRequest3 = new UpdateRequest("person", "3").doc(
                XContentType.JSON, "tel", "0132930");

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(updateRequest1);
        bulkRequest.add(updateRequest2);
        bulkRequest.add(updateRequest3);

        BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        BulkItemResponse[] items = response.getItems();
    }

    /**
     * 批量删除
     *
     * @throws IOException
     */
    @Order(1)
    @Test
    public void testBatchDeleteDoc() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        DeleteRequest deleteRequest1 = new DeleteRequest("person", "1");

        DeleteRequest deleteRequest2 = new DeleteRequest("person", "2");

        DeleteRequest deleteRequest3 = new DeleteRequest("person", "3");

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(deleteRequest1);
        bulkRequest.add(deleteRequest2);
        bulkRequest.add(deleteRequest3);

        BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        BulkItemResponse[] items = response.getItems();
    }

    @AfterEach
    public void closeEsClient() throws IOException {
        if (esClient != null) {
            System.out.println("关闭连接...");
            esClient.close();
        }
    }
}
