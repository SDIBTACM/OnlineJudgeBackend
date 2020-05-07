package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-04 14:43
 */
@Data
@Entity
@Table(name = "news")
@ToString
@EqualsAndHashCode(callSuper = true)
public class NewsEntity extends BaseEntity {
    @Column
    Long ownerId;

    @Column(length = 64)
    String title;

    @Column
    Boolean status;
}
