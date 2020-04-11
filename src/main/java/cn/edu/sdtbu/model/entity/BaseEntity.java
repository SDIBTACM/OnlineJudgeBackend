package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.util.TimeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * TODO
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 10:33
 */


@Data
@ToString
@EqualsAndHashCode
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Timestamp createAt;

    @Column
    private Timestamp updateAt;

    @PrePersist
    protected void prePersist() {
        Timestamp now = TimeUtil.now();
        if (createAt == null) {
            createAt = now;
        }
        if (updateAt == null) {
            updateAt = now;
        }
    }
    @PreUpdate
    protected  void preUpdate() {
        updateAt = TimeUtil.now();
    }
}
