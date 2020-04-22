package mail;

import cn.edu.sdtbu.service.impl.MailServiceImpl;
import cn.edu.sdtbu.util.MailUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-22 15:40
 */

public class MailServiceImplTest {
    MailServiceImpl mailService = new MailServiceImpl();
    MailUtil mailUtil = new MailUtil();

    @Test
    public void sendMailTest() {
        /**
         *  sendTo Test email
         */
        mailService.sendMail("");
    }

}
