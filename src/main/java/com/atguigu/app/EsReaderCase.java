package com.atguigu.app;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author chenhuiup
 * @create 2020-10-25 15:00
 */
/*
1.查询条件是一层方法一层对象,所以使用API实现需要先写好DSL语句，对着写，避免写错了
 */
public class EsReaderCase {
    public static void main(String[] args) throws IOException {
        //1.创建客户端对象
        //1.1 创建工厂对象
        JestClientFactory jestClientFactory = new JestClientFactory();

        //1.2设置连接地址
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://hadoop102:9200").build();
        jestClientFactory.setHttpClientConfig(httpClientConfig);

        //1.3获取客户端对象
        JestClient jestClient = jestClientFactory.getObject();
        /*
        #查询0523班爱好"臭美查岗"的,同时查询各班有多少人,并应用分页
        GET stu/_search
        {
          "query": {
            "bool": {
              "filter": {
                "term": {
                  "class_id": "0523"
                }
              },
              "must": [
                {
                  "match": {
                    "favo1": "臭美查岗"
                  }
                }
              ]
            }
          },
          "aggs": {
            "countByClass_id": {
              "terms": {
                "field": "class_id",
                "size": 10
              }
            }
          },
          "from": 1,
          "size": 20
        }
         */
        //2.读取数据
        //2.1 创建Search对象,实现filter
        // 查询条件是一层方法一层对象，如果用scala写就全是方法
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //全值匹配
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("class_id", "0523");
        boolQueryBuilder.filter(termQueryBuilder);
        //分词匹配
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("favo1", "臭美查岗");
        boolQueryBuilder.must(matchQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);

        //聚合组
        TermsAggregationBuilder countByClassId = AggregationBuilders.terms("countByClass_id");
        countByClassId.field("class_id");
        countByClassId.size(10);
        searchSourceBuilder.aggregation(countByClassId);

        //分页
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);



        // 2.2 创建Search对象
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex("stu")
                .addType("_doc")
                .build();

        //3.执行读取
        //3.1获得返回集
        SearchResult searchResult = jestClient.execute(search);
        // Json映射到java中就是Map，为了封装Source
        //3.2 解析searchResult
        System.out.println("命中条数为："+ searchResult.getTotal());
        List<SearchResult.Hit<Map, Void>> hits = searchResult.getHits(Map.class);
        //遍历明细
        for (SearchResult.Hit<Map, Void> hit : hits) {
            Map source = hit.source;
            for (Object o : source.keySet()) {
                System.out.println("Key: " + o + ",Value: " +source.get(o));
            }
        }

        //取出聚合组数据
        System.out.println("****************************");
        MetricAggregation aggregations = searchResult.getAggregations();
        TermsAggregation countByClassId1 = aggregations.getTermsAggregation("countByClassId");
        for (TermsAggregation.Entry bucket : countByClassId1.getBuckets()) {
            System.out.println("Key:" + bucket.getKey() + ",Value" + bucket.getCount());
        }

        //4.关闭连接
        jestClient.shutdownClient();
    }
}
