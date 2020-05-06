package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 21:13
 */

@Data
@Entity
@Table(name = "cache", indexes = {
    @Index(name = "uk_cache_key", columnList = "cache_key", unique = true)
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class CacheEntity extends BaseEntity {
    @Column(name = "cache_key", nullable = false)
    String key;

    @Lob
    @Column(name = "cache_value")
    String value;
}
