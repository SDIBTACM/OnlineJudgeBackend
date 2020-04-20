package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.vo.MailVO;
import cn.edu.sdtbu.service.MailService;
import cn.edu.sdtbu.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:57
 */

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private TaskExecutor taskExecutor;
    @Resource
    private JavaMailSender javaMailSender;
    @Value("spring.mail.username")
    private String sendFrom;
    @Value("SDTBU-OJ")
    private String dName;
    @Value("Notice")
    private String title;

    @Override
    @Async
    public void sendMail(String sendTo, String text) {
        MailUtil mailUtil = new MailUtil();
        MailVO mailVO = new MailVO();
        mailVO.setTo(sendTo);
        mailVO.setSubject(title);
        mailVO.setText(mailUtil.getText(sendTo));
        addSendMail(mailVO);
    }

    private void addSendMail(MailVO mailVO) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(new InternetAddress(sendFrom,dName,"UTF-8"));
            helper.setTo(mailVO.getTo());
            helper.setSubject(mailVO.getSubject());
            helper.setText(mailVO.getText(), true);
            javaMailSender.send(mimeMessage);
            log.info("success,send to {}",mailVO.getTo());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
