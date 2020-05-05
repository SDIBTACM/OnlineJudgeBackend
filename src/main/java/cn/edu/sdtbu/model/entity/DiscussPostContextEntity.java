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
 * @date 2020-05-04 14:38
 */
@Data
@Entity
@Table(name = "discuss_post_context")
@ToString
@EqualsAndHashCode(callSuper = true)
public class DiscussPostContextEntity extends BaseEntity {
    @Lob
    @Column
    String context;
}
