package cn.edu.sdtbu.manager;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:45
 */

public interface MailManager {

    /**
     * send email
     * @param sendTo send to email
     */
    void sendSignUpMail(String token, String username, String sendTo);
}
