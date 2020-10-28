package com.atguigu.app;

import com.atguigu.bean.Student;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;

import java.io.IOException;

/**
 * @author chenhuiup
 * @create 2020-10-25 13:22
 */
/*
单次数据写入，但是像流式数据，比如SparkStreaming，就是微批次写入数据，一个批次获取一个连接。
 */
public class EsWrite {
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
        //导包：import io.searchbox.core.Index;
        // Builder的参数类型为Object，说明可以传入一个javaBean对象，通过反射的方式写入数据，让代码简洁一些。
//        Index index = new Index.Builder("\"class_id\":\"0317\",\n" +
//                "  \"name\":\"张小芳\",\n" +
//                "  \"gender\":\"female\",\n" +
//                "  \"age\":19,\n" +
//                "  \"favo1\":\"臭美查岗,胸卡手机交一下,口罩戴起来\",\n" +
//                "  \"favo2\":\"臭美查岗,胸卡手机交一下,口罩戴起来\"")
//                .index("stu")
//                .type("_doc")
//                .id("1010")
//                .build();

        Student student = new Student("0621", "王五", "female", 23, "睡觉", "睡觉吃饭");

        Index index = new Index.Builder(student)
                .index("stu")
                .type("_doc")
                .id("1010")
                .build();

        // 2.2插入数据
        jestClient.execute(index);

        //3.关闭连接
        //尽管在6.x中shutdownClient过时，但是由于close方法有些问题，所以依旧使用过时的方法，可能在7.x中解决了。
        jestClient.shutdownClient();


    }
}
