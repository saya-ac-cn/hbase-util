package ac.cn.saya.hbase.starter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

/**
 * @Title: HbaseAPITest
 * @ProjectName hbase-starter
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2019-08-13 22:24
 * @Description:
 */

public class HbaseAPITest {

    Configuration config = null;
    private Connection connection = null;

    @Before
    public void init() throws Exception {
        config = HBaseConfiguration.create();// 配置
        config.set("hbase.zookeeper.quorum", "127.0.0.1");// zookeeper地址
        config.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
        connection = ConnectionFactory.createConnection(config);
    }
    /**
     * 创建表
     * @throws Exception
     */
    @Test
    public void testCreateTable() throws Exception{
        //创建表管理类
        HBaseAdmin admin = new HBaseAdmin(config);
        //创建表描述类
        TableName tableName = TableName.valueOf("user");
        HTableDescriptor descriptor = new HTableDescriptor(tableName);
        //创建列族描述类
        HColumnDescriptor info1 = new HColumnDescriptor("id");
        //列族加入表中
        descriptor.addFamily(info1);
        HColumnDescriptor info2 = new HColumnDescriptor("name");
        descriptor.addFamily(info2);
        //创建表
        admin.createTable(descriptor);
    }

    /**
     * 向表中插入数据
     * 单条插入(包括修改)
     * @throws Exception
     */
    @Test
    public void testPutOne() throws Exception{
        Table table = connection.getTable(TableName.valueOf("user"));
        //rowkey
        Put put = new Put(Bytes.toBytes("10001"));
        //列族，列，值
        // 设置行键数据
        put.add("id".getBytes(), "uid".getBytes(), "10001".getBytes());
        put.add("name".getBytes(), "school".getBytes(), "saya".getBytes());
        // 添加数据
        table.put(put);
        // 关闭连接
        table.close();
    }

    /**
     * 单条查询
     * @throws Exception
     */
    @Test
    public void testGetSingle() throws Exception{
        Table table = connection.getTable(TableName.valueOf("user"));
        //rowkey
        Get get = new Get(Bytes.toBytes("10001"));
        Result result = table.get(get);
        //列族，列名
        byte[] name = result.getValue(Bytes.toBytes("id"), Bytes.toBytes("uid"));
        byte[] age = result.getValue(Bytes.toBytes("name"), Bytes.toBytes("school"));
        System.out.println(Bytes.toString(name));
        System.out.println(Bytes.toString(age));
        // 关闭连接
        table.close();
    }

}
