package com.atguigu.app;

import com.atguigu.bean.Student;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.io.IOException;

/**
 * @author chenhuiup
 * @create 2020-10-25 14:11
 */
/*
单次数据写入，但是像流式数据，比如SparkStreaming，就是微批次写入数据，一个批次获取一个连接。
 */
public class EsWriterByBulk {
    public static void main(String[] args) throws IOException {
        //1.创建客户端对象
        //1.1 创建工厂对象
        JestClientFactory jestClientFactory = new JestClientFactory();

        //1.2设置连接地址
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://hadoop102:9200").build();
        jestClientFactory.setHttpClientConfig(httpClientConfig);

        //1.3获取客户端对象
        JestClient jestClient = jestClientFactory.getObject();

        //2.操作数据
        //2.1 创建Index对象
        Student student1 = new Student("0621", "田七", "female", 23, "睡觉", "睡觉吃饭");
        Index index1 = new Index.Builder(student1)
                .id("1011")
                .build();
        Student student2 = new Student("0621", "王八", "female", 23, "睡觉", "睡觉吃饭");
        Index index2 = new Index.Builder(student2)
                .id("1012")
                .build();
        Student student3 = new Student("0621", "喝九", "female", 23, "睡觉", "睡觉吃饭");
        Index index3 = new Index.Builder(student3)
                .id("1013")
                .build();

        //2.2 创建Bulk对象
        // 由于是批量写入数据，所以index和type都是相同的，可以使用默认的，避免重复写。
        Bulk bulk = new Bulk.Builder()
                .addAction(index1)
                .addAction(index2)
                .addAction(index3)
                .defaultIndex("stu")
                .defaultType("_doc")
                .build();

        //2.3 执行批量写入数据
        jestClient.execute(bulk);

        //3.关闭连接
        jestClient.shutdownClient();
    }
}
