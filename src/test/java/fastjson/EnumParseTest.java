package fastjson;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.enums.IntValueEnum;
import cn.edu.sdtbu.model.enums.JudgeResult;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-10 20:36
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EnumParseTest {
    @Test
    public void  parseTest() {
        String res = JSON.toJSONString(JudgeResult.PENDING);
        Object ress = JSON.parseObject("-4", IntValueEnum.class);
        System.out.println(res);
    }
}
