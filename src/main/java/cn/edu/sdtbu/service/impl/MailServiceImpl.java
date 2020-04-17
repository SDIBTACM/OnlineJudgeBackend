package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.MailEntity;
import cn.edu.sdtbu.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    @Override
    public void sendEmail(MailEntity mailEntity) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String dName = "SDTBU-OJ";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(new InternetAddress(mailEntity.getFrom(),dName,"UTF-8"));
            helper.setTo(mailEntity.getTo());
            helper.setSubject(mailEntity.getSubject());
            helper.setText(mailEntity.getText(), true);
            addSendMailTask(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void addSendMailTask(final MimeMessage mimeMessage) {
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    javaMailSender.send(mimeMessage);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
