package ac.cn.saya.phoenix.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PhoenixDataSourceFactory {


    private String jdbcUrl;

    private Properties config;

    /**
     *  The number of connections of initial pool
     */
    private int min;

    private int max;
    /**
     * The number of connections of polled and not released from poll
     */
    private int used;

    private Queue<Connection> pool = null;

    public PhoenixDataSourceFactory(String jdbcUrl, Properties config, int min, int max) throws Exception{
        this.jdbcUrl = jdbcUrl;
        this.config = config;
        this.min = min;
        this.max = max;
        this.used = 0;
        this.pool = new ConcurrentLinkedQueue<Connection>();
        init();
    }

    /**
     * 初始化数据源
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void init() throws SQLException{
        for (int i = 0; i < min; i++) {
            Connection conn = DriverManager.getConnection(jdbcUrl, config);
            // 初始完毕后的连接放入到池子中
            pool.add(conn);
        }
    }

    public Connection getConnection() {
        Connection conn  = null;
        if (pool.size() > 0) {
            conn = pool.poll();
            Thread connGen = new Thread(() -> {
                try {
                    Connection _conn = DriverManager.getConnection(jdbcUrl, config);
                    pool.add(_conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            connGen.start();
            used ++;
        } else if(used < max) {
            try {
                conn = DriverManager.getConnection(jdbcUrl, config);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                used--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
