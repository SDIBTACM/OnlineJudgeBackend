package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 14:49
 */
@Data
@Entity
@Table(name = "count", indexes = {
    @Index(name = "uk_count_key", columnList = "count_key", unique = true)
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class CountEntity extends BaseEntity {
    @Column(name = "count_key", nullable = false)
    String countKey;

    @Column(nullable = false)
    Long total;
}
