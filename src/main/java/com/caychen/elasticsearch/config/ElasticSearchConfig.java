package com.caychen.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @Author: Caychen
 * @Date: 2021/4/24 15:17
 * @Description:
 */
//@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Override
    public RestHighLevelClient elasticsearchClient() {
        return null;
    }
}
