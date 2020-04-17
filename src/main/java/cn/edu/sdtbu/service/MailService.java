package cn.edu.sdtbu.service;


import cn.edu.sdtbu.model.entity.MailEntity;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:45
 */

public interface MailService {

    /**
     * send email
     * @param mailEntity email info
     */
    void sendEmail(MailEntity mailEntity);
}
