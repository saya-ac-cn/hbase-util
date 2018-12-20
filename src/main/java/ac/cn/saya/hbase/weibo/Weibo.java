package ac.cn.saya.hbase.weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Title: Weibo
 * @ProjectName hbase
 * @Description: TODO
 * @Author Saya
 * @Date: 2018/12/19 22:41
 * @Description:
 */

public class Weibo {

    // 互殴配置conf
    private Configuration conf = HBaseConfiguration.create();
    // 微博内容表名
    private static final byte[] TABLE_CONTENT = Bytes.toBytes("ns_weibo:content");
    // 用户关系表名
    private static final byte[] TABLE_RELATION = Bytes.toBytes("ns_weibo:relation");
    // 微博收件箱表名
    private static final byte[] TABLE_INBOX = Bytes.toBytes("ns_weibo:inbox");

    /**
     * @描述 初始化命名空间
     * @参数
     * @返回值
     * @创建人  saya.ac.cn-刘能凯
     * @创建时间  2018/12/19
     * @修改人和其它信息
     */
    public void initNameSpace()
    {
        HBaseAdmin admin = null;
        try{
            Connection connection = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin)connection.getAdmin();
            // 命名空间类似于关系型数据库中的schema，可以想象成文件夹
            NamespaceDescriptor weibo = NamespaceDescriptor
                    .create("ns_weibo")
                    .addConfiguration("author","Saya")
                    .addConfiguration("creat_time",System.currentTimeMillis() + "")
                    .build();
        }catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        }catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(null != admin)
            {
                try {
                    admin.close();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @描述 创建微博内容表
     * @参数
     * @返回值
     * @创建人  saya.ac.cn-刘能凯
     * @创建时间  2018/12/20
     * @修改人和其它信息
     * Table Name:ns_weibo:content
     * RowKey:用户 ID_时间戳
     * ColumnFamily:info
     * ColumnLabel:标题,内容,图片 URL
     * Version:1 个版本
     */
    public void createTableContent(){
        HBaseAdmin admin = null;
        Connection connection = null;
        try{
            connection = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin)connection.getAdmin();
            // 创建表描述器
            HTableDescriptor contentTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_CONTENT));
            // 创建列描述器
            HColumnDescriptor infoColumnDescriptor = new HColumnDescriptor(Bytes.toBytes("info"));
            // 设置块缓存
            infoColumnDescriptor.setBlockCacheEnabled(true);
            // 设置块缓存大小
            infoColumnDescriptor.setBlocksize(2097152);
            // 设置压缩方式
            // infoColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
            // 设置版本确界
            infoColumnDescriptor.setMaxVersions(1);
            infoColumnDescriptor.setMinVersions(1);
            contentTableDescriptor.addFamily(infoColumnDescriptor);
            admin.createTable(contentTableDescriptor);
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(admin != null)
            {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @描述 用户关系表
     * @参数
     * @返回值
     * @创建人  saya.ac.cn-刘能凯
     * @创建时间  2018/12/20
     * @修改人和其它信息
     * Table Name:ns_weibo:relation
     * RowKey:用户 ID
     * ColumnFamily:attends,fans
     * ColumnLabel:关注用户 ID，粉丝用户 ID
     * ColumnValue:用户 ID
     * Version：1 个版本
     */
    public void creaTableRelation(){
        HBaseAdmin admin = null;
        Connection connection = null;
        try{
            connection = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin)connection.getAdmin();
            // 创建表描述器
            HTableDescriptor relationTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_RELATION));

            // 创建列描述器 关注人的列族
            HColumnDescriptor attendColumnDescriptor = new HColumnDescriptor(Bytes.toBytes("attends"));
            // 设置块缓存
            attendColumnDescriptor.setBlockCacheEnabled(true);
            // 设置块缓存大小
            attendColumnDescriptor.setBlocksize(2097152);
            // 设置压缩方式
            // attendColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
            // 设置版本确界
            attendColumnDescriptor.setMaxVersions(1);
            attendColumnDescriptor.setMinVersions(1);

            // 创建列描述器 粉丝列族
            HColumnDescriptor fansColumnDescriptor = new HColumnDescriptor(Bytes.toBytes("fans"));
            // 设置块缓存
            fansColumnDescriptor.setBlockCacheEnabled(true);
            // 设置块缓存大小
            fansColumnDescriptor.setBlocksize(2097152);
            // 设置压缩方式
            // fansColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
            // 设置版本确界
            fansColumnDescriptor.setMaxVersions(1);
            fansColumnDescriptor.setMinVersions(1);

            relationTableDescriptor.addFamily(attendColumnDescriptor);
            relationTableDescriptor.addFamily(fansColumnDescriptor);
            admin.createTable(relationTableDescriptor);
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(admin != null)
            {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @描述 创建微博收件箱表
     * @参数
     * @返回值
     * @创建人  saya.ac.cn-刘能凯
     * @创建时间  2018/12/20
     * @修改人和其它信息
     * Table Name: ns_weibo:inbox
     * RowKey:用户 ID
     * ColumnFamily:info
     * ColumnLabel:用户 ID_发布微博的人的用户 ID
     * ColumnValue:关注的人的微博的 RowKey
     * Version:1000
     */
    public void createTableInbox(){
        HBaseAdmin admin = null;
        Connection connection = null;
        try{
            connection = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin)connection.getAdmin();
            // 创建表描述器
            HTableDescriptor inboxTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_INBOX));
            // 创建列描述器
            HColumnDescriptor infoColumnDescriptor = new HColumnDescriptor(Bytes.toBytes("info"));
            // 设置块缓存
            infoColumnDescriptor.setBlockCacheEnabled(true);
            // 设置块缓存大小
            infoColumnDescriptor.setBlocksize(2097152);
            // 设置压缩方式
            // infoColumnDescriptor.setCompressionType(Compression.Algorithm.SNAPPY);
            // 设置版本确界
            infoColumnDescriptor.setMaxVersions(1);
            infoColumnDescriptor.setMinVersions(1);
            inboxTableDescriptor.addFamily(infoColumnDescriptor);
            admin.createTable(inboxTableDescriptor);
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(admin != null)
            {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
