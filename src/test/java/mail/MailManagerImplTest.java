package mail;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.manager.MailManager;
import cn.edu.sdtbu.manager.impl.MailManagerImpl;
import cn.edu.sdtbu.util.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-22 15:40
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MailManagerImplTest {
    @Resource
    MailManager mailService;
    @Test
    public void sendMailTest() {
        /**
         *  sendTo Test email
         */
        mailService.sendSignUpMail(UUID.randomUUID().toString(),"bestsort","bestsort@qq.com");
    }

}
