package ac.cn.saya.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @Title: HbaseDemo
 * @ProjectName hbase
 * @Description: TODO
 * @Author Saya
 * @Date: 2018/11/29 22:17
 * @Description:
 */

public class HbaseDemo {

    public static Configuration conf;

    static {
        conf = HBaseConfiguration.create();
    }

    /**
     * 判断表是否存在
     * @param tableName
     * @return
     */
    public static boolean isExists(String tableName) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        return admin.tableExists(TableName.valueOf(tableName));//存在 trur 不存在 false
    }

    /**
     * 创建表
     * @param tableName
     * @param columnFamily
     * @throws IOException
     */
    public static void createTable(String tableName,String ...columnFamily) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        if(isExists(tableName))
        {
            System.out.println("表已经存在");
        }
        else
        {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for(String item : columnFamily)
            {
                hTableDescriptor.addFamily(new HColumnDescriptor(item));
            }
            admin.createTable(hTableDescriptor);
            System.out.println("表创建成功");
        }
    }

    public static void main(String[] args) throws IOException
    {
        //System.out.println(isExists("student"));
        createTable("student","basicInfo","studyInfo");
    }

}
