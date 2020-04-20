package cn.edu.sdtbu.util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

/**
 *
 * @author Soul
 * @version 1.0
 * @date 2020-04-18 20:03
 */

public class MailUtil {
    private TemplateEngine templateEngine = new TemplateEngine();

    private String getActiveCode() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public String getText(String to) {
        Context context = new Context();
        context.setVariable("username", to);
        context.setVariable("code",getActiveCode());
        return templateEngine.process("mail",context);
    }
}