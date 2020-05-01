package cache;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.manager.RedisManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 23:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {
    @Autowired
    RedisManager manager;
    @Test
    public void managerTest(){
        manager.put("1", "abc", 100, TimeUnit.MINUTES);
        assert manager.get("1").equals("abc");
        manager.put("1", "1", 100, TimeUnit.SECONDS);
        manager.inc("1", 1);
        assert manager.get("1").equals("2");
    }
}
