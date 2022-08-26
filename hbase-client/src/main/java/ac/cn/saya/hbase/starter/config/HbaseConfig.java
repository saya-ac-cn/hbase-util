package ac.cn.saya.hbase.starter.config;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.Set;
/**
 * @Title: HbaseConfig
 * @ProjectName hbase-starter
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2019-08-13 23:33
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseConfig {

    private final HbaseProperties properties;
    public HbaseConfig(HbaseProperties properties) {
        this.properties = properties;
    }
    public org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        Map<String, String> config = properties.getConfig();
        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }
        return configuration;
    }

}
