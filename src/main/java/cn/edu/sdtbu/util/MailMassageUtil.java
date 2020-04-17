package cn.edu.sdtbu.util;

/**
 * get a register code
 * @author Soul
 * @version 1.0
 * @date 2020-04-17 14:03
 */

public class MailMassageUtil {
    public String getRegisterMail(String email) {
        final StringBuffer sb = new StringBuffer();
        sb.append("<h2> Hello," + email + "! </h2>")
                .append("<p style='color:red'> The register code is :" + RandomUtil.randomCode());
        return sb.toString();
    }
}
