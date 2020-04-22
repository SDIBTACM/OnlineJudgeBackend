package cn.edu.sdtbu.service;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:45
 */

public interface MailService {

    /**
     * send email
     * @param sendTo send to email
     */
    void sendMail(String sendTo);
}
