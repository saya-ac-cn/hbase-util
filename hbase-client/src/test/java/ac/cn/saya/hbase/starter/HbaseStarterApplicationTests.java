package ac.cn.saya.hbase.starter;

import ac.cn.saya.hbase.starter.service.HbaseService;
import ac.cn.saya.hbase.starter.service.impl.HbaseServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseStarterApplicationTests {

    @Autowired
    private HbaseServiceImpl hbaseService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testIsExists() {
        System.out.println(hbaseService.isExists("user"));
    }

}
