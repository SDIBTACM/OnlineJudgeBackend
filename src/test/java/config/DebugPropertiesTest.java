package config;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.properties.DebugProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-12 07:54
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DebugPropertiesTest {
    @Resource
    DebugProperties config;
    @Value("${online-judge.debug.time-cost:false}")
    Boolean controllerTimeCostEnabled;

    @Test
    public void initTest() {
        assert config.getTimeCost().equals(controllerTimeCostEnabled);
    }
}
