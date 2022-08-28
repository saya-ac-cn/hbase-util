package ac.cn.saya.phoenix.util;

import ac.cn.saya.phoenix.unit.Starter;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoenixUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(PhoenixUtil.class);

    /**
     * @param sql
     * @param params 参数
     * 功能介绍：更新操作（修改，删除，插入）
     */
    public static int executeUpdate(String sql, Object[] params) {
        Connection connection = Starter.dataSourceFactory.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (params.length != 0) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            int rows = preparedStatement.executeUpdate();
            connection.commit();
            return rows;
        } catch (Exception e) {
            LOGGER.error("execute update fail! cause by:",e);
        } finally {
            Starter.dataSourceFactory.releaseConnection(connection);
        }
        return 0;
    }

    /**
     * @param sql
     * @param list
     * 功能介绍：批量更新
     */
    public static void batchUpdate(String sql, List<Object[]> list) {
        Connection connection = Starter.dataSourceFactory.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //关闭自动提交事务
            connection.setAutoCommit(false);
            //防止内存溢出
            final int batchSize = 1000;
            //记录插入数量
            int count = 0;
            int size = list.size();
            Object[] obj = null;
            for (int i = 0; i < size; i++) {
                obj = list.get(i);
                for (int j = 0; j < obj.length; j++) {
                    preparedStatement.setObject(j + 1, obj[j]);
                }
                preparedStatement.addBatch();
                if (++count % batchSize == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                }
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error("batch update fail! cause by:",e);
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            //关闭资源
            Starter.dataSourceFactory.releaseConnection(connection);
        }
    }

    /**
     * @param sql
     * @param params
     * 功能介绍：查询操作
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object[] params) {
        ResultSet rs = null;
        List<Map<String, Object>> list = null;
        Connection connection = Starter.dataSourceFactory.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            rs = preparedStatement.executeQuery();
            stopWatch.stop();
            LOGGER.info("UserBigTableService sql-executeQuery-time: " + stopWatch.getTime() + "ms");

            list = new ArrayList<>();
            //移动光标，如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Map<String, Object> map = new HashMap<>(16);
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                list.add(map);
            }
            return list;

        } catch (Exception e) {
            LOGGER.error("execute query fail! cause by:",e);
        } finally {
            //关闭资源
            Starter.dataSourceFactory.releaseConnection(connection);
        }
        return null;
    }

    /**
     * @param sql
     * @param params
     * 功能介绍：查询操作一条记录
     */
    public static Map<String, Object> queryOne(String sql, Object[] params) {
        ResultSet rs = null;
        Map<String, Object> map = null;
        Connection connection = Starter.dataSourceFactory.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            rs = preparedStatement.executeQuery();
            stopWatch.stop();
            LOGGER.info("UserBigTableService sql-queryOne-time: " + stopWatch.getTime() + "ms");
            //移动光标，如果新的当前行有效，则返回 true；如果不存在下一行，则返回 false
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                map = new HashMap<>(16);
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                //若有多条记录，取第一条。
                break;
            }
            return map;

        } catch (Exception e) {
            LOGGER.error("execute query one fail! cause by:",e);
        } finally {
            //关闭资源
            Starter.dataSourceFactory.releaseConnection(connection);
        }
        return null;
    }

}
