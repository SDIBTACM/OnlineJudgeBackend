package cn.edu.sdtbu.manager.impl;

import cn.edu.sdtbu.manager.MailManager;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:57
 */

@Slf4j
@Service
public class MailManagerImpl implements MailManager {
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private OnlineJudgeProperties properties;

    @Override
    //@Async
    public void sendSignUpMail(String token, String username, String sendTo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(Objects.requireNonNull(((JavaMailSenderImpl) javaMailSender).getUsername()));
            helper.setTo(sendTo);
            helper.setSubject("OnlineJudge-用户注册激活邮件");
            helper.setText(generatorSignUpMail(token,username), true);
            javaMailSender.send(mimeMessage);
            log.info("sign up mail send success. to {}", username);
        } catch (Throwable e) {
            log.error("mail send to [{}] failed, plz check it. error message:[{}], error trace:{}",
                username, e.getMessage(), e.getStackTrace());
        }
    }

    public String generatorSignUpMail(String token,String account) {
        Context context = new Context();
        String activeUrl = properties.getUrl() +
            "/api/user/activate?" +
            "token=" + token;
        context.setVariable("homeUrl", properties.getUrl());
        context.setVariable("activeUrl", activeUrl);
        context.setVariable("userAccount", account);
        return templateEngine.process("mail", context);
    }
}
