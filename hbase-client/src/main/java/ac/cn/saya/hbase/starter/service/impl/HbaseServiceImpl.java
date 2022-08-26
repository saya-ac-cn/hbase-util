package ac.cn.saya.hbase.starter.service.impl;

import ac.cn.saya.hbase.starter.config.HbaseConfig;
import ac.cn.saya.hbase.starter.service.HbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.hadoop.hbase.client.*;
import org.springframework.util.StringUtils;
import org.apache.hadoop.hbase.TableName;
import java.io.IOException;

/**
 * @Title: HbaseServiceImpl
 * @ProjectName hbase-starter
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2019-08-13 23:39
 * @Description:
 */

@Service(value = "hbaseServiceImpl")
public class HbaseServiceImpl implements HbaseService {

    @Autowired
    private HbaseConfig con;

    /**
     * 判断表是否存在
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    @Override
    public boolean isExists(String tableName) {
        Connection connection=null;
        if (StringUtils.isEmpty(tableName)){
            return false;
        }
        try {
            connection = ConnectionFactory.createConnection(con.configuration());
            Admin admin = connection.getAdmin();
            // 存在 trur 不存在 false
            return admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("判断表是否存在发生异常："+e);
            return false;
        } finally {
            // 释放资源 必须
            try {
                if (null != connection){
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
