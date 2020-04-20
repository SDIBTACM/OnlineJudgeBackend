package cn.edu.sdtbu.model.vo;

import lombok.Data;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-04-16 11:08
 */

@Data
public class MailVO {
    private String to;
    private String subject;
    private String text;
}
