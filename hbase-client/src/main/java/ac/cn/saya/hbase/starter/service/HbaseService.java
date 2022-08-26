package ac.cn.saya.hbase.starter.service;

import java.io.IOException;

/**
 * @Title: HbaseService
 * @ProjectName hbase-starter
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2019-08-13 23:37
 * @Description:
 */

public interface HbaseService {

    /**
     * 判断表是否存在
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean isExists(String tableName);

}
