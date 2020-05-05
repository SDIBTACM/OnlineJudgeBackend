package cn.edu.sdtbu.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-04 14:57
 */
@Data
@Entity
@Table(name = "mail_context")
@ToString
@EqualsAndHashCode(callSuper = true)
public class MailContextEntity extends BaseEntity {
    @Lob
    @Column
    String context;
}
