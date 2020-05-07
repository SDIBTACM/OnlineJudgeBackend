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
 * @date 2020-05-04 15:00
 */
@Data
@Entity
@Table(name = "option")
@ToString
@EqualsAndHashCode(callSuper = true)
public class OptionEntity extends BaseEntity {
    @Column(length = 64)
    String key;

    @Lob
    @Column
    String value;

    @Lob
    @Column
    String comment;
}
