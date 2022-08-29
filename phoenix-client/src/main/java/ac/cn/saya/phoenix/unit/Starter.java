package ac.cn.saya.phoenix.unit;


import ac.cn.saya.phoenix.util.PhoenixDataSourceFactory;
import ac.cn.saya.phoenix.util.PhoenixUtil;
import org.apache.commons.lang.time.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * https://gitee.com/astra_zhao/hbase-phoenix-docker/
 * docker run -it -d -p 8765:8765 -p 60000:60000 -p 60010:60010 -p 60020:60020 -p 2181:2181 -p 60030:60030  -p 2888:2888 -p 3888:3888 iteblog/hbase-phoenix-docker
 * echo /etc/hosts
 * echo "127.0.0.1       localhost" > /etc/hosts
 * echo "10.203.1.219      9df7e1446d20" >> /etc/hosts
 * 创建一个 schema
 * create schema business;
 * 查看所有的table
 * !table
 * 退出终端
 * !quit
 * 创建一个表
 * CREATE TABLE IF NOT EXISTS business.student(id VARCHAR primary key,name VARCHAR,addr VARCHAR);
 * 插入一条数据
 * upsert into business.student values('1001','zhanghong','chengdu');
 * 查询数据
 * select * from business.student;
 */
public class Starter{

    public static PhoenixDataSourceFactory dataSourceFactory;

    public static void main(String[] args) throws Exception {
        // 项目资源的初始化
        //String url = "jdbc:phoenix:hadoop102,hadoop103,hadoop104:2181";
        String url = "jdbc:phoenix:10.203.1.219:2181";
        Properties props = new Properties();
        //props.put("phoenix.schema.isNamespaceMappingEnabled","true");
        dataSourceFactory = new PhoenixDataSourceFactory(url, props, 3, 5);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Map<String, Object>> lines = PhoenixUtil.executeQuery("select * from business.student", null);
        stopWatch.stop();
        for (Map<String, Object> line:lines) {
            for (Map.Entry<String,Object> field:line.entrySet()) {
                System.out.println(field.getKey()+"="+field.getValue());
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>");
        }
        System.out.println("耗时："+stopWatch.getTime());
    }


}

