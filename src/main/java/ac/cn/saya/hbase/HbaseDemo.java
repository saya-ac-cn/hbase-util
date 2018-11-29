package ac.cn.saya.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

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

    }

    public static void main(String[] args)
    {
        
    }

}
