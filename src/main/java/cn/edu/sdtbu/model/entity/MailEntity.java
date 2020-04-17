package cn.edu.sdtbu.model.entity;

import lombok.Data;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:08
 */

@Data
public class MailEntity {
    private String from;
    private String to;
    private String subject;
    private String text;
}
