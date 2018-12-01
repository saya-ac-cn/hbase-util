package ac.cn.saya.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    /**
     * 删除表
     * @param tableName
     * @throws IOException
     */
    public static void deleteTable(String tableName) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        if(isExists(tableName))
        {
            if(!admin.isTableDisabled(TableName.valueOf(tableName)))
            {
                admin.disableTable(TableName.valueOf(tableName));
            }
            admin.deleteTable(TableName.valueOf(tableName));
            System.out.println("删除成功");
        }else
        {
            System.err.println("表不存在");
        }
    }

    /**
     * 添加数据
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param value
     * @throws IOException
     */
    public static void addRow(String tableName,String rowKey,String columnFamily,String column,String value) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(columnFamily),Bytes.toBytes(column),Bytes.toBytes(value));
        table.put(put);
    }

    /**
     * 删除一行数据
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @throws IOException
     */
    public static void deleteRow(String tableName,String rowKey,String columnFamily) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);
    }

    /**
     * 删除多行数据
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public static void deleteMutiRow(String tableName,String... rowKey) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        List<Delete> list = new ArrayList<Delete>();
        for(String row : rowKey)
        {
            Delete delete = new Delete(Bytes.toBytes(row));
            list.add(delete);
        }
        table.delete(list);
    }

    /**
     * 扫描表数据
     * @param tableName
     * @throws IOException
     */
    public static void getAllRow(String tableName) throws IOException
    {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        for(Result result : results)
        {
            Cell[] cells = result.rawCells();
            for(Cell cell : cells)
            {
                System.out.println("行键："+ Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.println("列族："+ Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("列："+ Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("值："+ Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
    }


    public static void main(String[] args) throws IOException
    {
        //System.out.println(isExists("student"));
        //createTable("student","basicInfo","studyInfo");
        //deleteTable("student");
        //addRow("student","141102000","basicInfo","name","admin");
        //deleteRow("student","141102012",null);
        //deleteMutiRow("student","141102001","141102002");
        getAllRow("student");
    }

}
