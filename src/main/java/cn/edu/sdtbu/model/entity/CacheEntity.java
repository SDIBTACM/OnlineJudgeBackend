package cn.edu.sdtbu.model.entity;

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
    @Column(name = "cache_value")
    String value;
}
