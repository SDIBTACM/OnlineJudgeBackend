package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
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
 * @date 2020-05-04 14:49
 */
@Data
@Entity
@Table(name = "news_context")
@ToString
@EqualsAndHashCode(callSuper = true)
public class NewsContextEntity extends BaseEntity {
    @Lob
    @Column
    String context;
}
