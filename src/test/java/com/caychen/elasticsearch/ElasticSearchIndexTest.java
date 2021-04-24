package com.caychen.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class ElasticSearchIndexTest {

    private static RestHighLevelClient esClient;

    /**
     * 初始化es客户端
     */
    @BeforeEach
    public void initEsClient() {
        esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

        Assertions.assertNotNull(esClient);
        System.out.println("连接成功...");
    }

    /**
     * 判断索引是否存在
     * @throws IOException
     */
    @Test
    public void testIndexExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest("shopping");
        boolean exists = esClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("该索引是否存在：" + exists);
    }

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("user");
//        request.settings()
//        request.mapping()
        CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);

        //响应状态
        System.out.println(response.isAcknowledged());

    }

    /**
     * 获取/查看索引
     *
     * @throws IOException
     */
    @Test
    public void testGetIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("person");
        GetIndexResponse response = esClient.indices().get(request, RequestOptions.DEFAULT);

        String[] indices = response.getIndices();
        if (indices.length != 0) {
            Map<String, Settings> settings = response.getSettings();
            settings.forEach((k, v) -> {
                System.out.println(k);
                System.out.println(v);
            });

            Map<String, MappingMetadata> mappings = response.getMappings();
            mappings.forEach((k, v) -> {
                Map<String, Object> sourceAsMap = v.getSourceAsMap();
                sourceAsMap.forEach((k1, v1) -> {
                    System.out.println(k1 + "  ------>  " + v1);
                });
            });
        }
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("person");
        AcknowledgedResponse response = esClient.indices().delete(request, RequestOptions.DEFAULT);

        System.out.println(response.isAcknowledged());
    }


    /**
     * 关闭es客户端
     *
     * @throws IOException
     */
    @AfterEach
    public void closeEsClient() throws IOException {
        if (esClient != null) {
            System.out.println("关闭连接...");
            esClient.close();
        }
    }
}
