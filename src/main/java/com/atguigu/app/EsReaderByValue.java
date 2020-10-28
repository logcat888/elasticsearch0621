package com.atguigu.app;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author chenhuiup
 * @create 2020-10-25 14:43
 */
public class EsReaderByValue {
    public static void main(String[] args) throws IOException {
        //1.创建客户端对象
        //1.1 创建工厂对象
        JestClientFactory jestClientFactory = new JestClientFactory();

        //1.2设置连接地址
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://hadoop102:9200").build();
        jestClientFactory.setHttpClientConfig(httpClientConfig);

        //1.3获取客户端对象
        JestClient jestClient = jestClientFactory.getObject();

        //2.读取数据
        //2.1 创建Search对象,实现filter
        // 查询条件是一层方法一层对象，如果用scala写就全是方法
        /*
        #全值匹配查询filter(Where)
        GET stu/_search
        {
          "query": {
            "bool": {
              "filter": {
                "term": {
                  "favo1": "臭美"
                }
              }
            }
          }
        }
         */
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("favo1", "臭美");
//        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("favo1", args[0]);
        boolQueryBuilder.filter(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);

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

        //4.关闭连接
        jestClient.shutdownClient();
    }
}
