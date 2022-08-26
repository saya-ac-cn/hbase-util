package ac.cn.saya.hbase.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @Title: HbaseProperties
 * @ProjectName hbase-starter
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2019-08-13 23:30
 * @Description:
 */
@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {

    private Map<String, String> config;
    public Map<String, String> getConfig() {
        return config;
    }
    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

}
