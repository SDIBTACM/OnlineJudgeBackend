package cn.edu.sdtbu.model.entity.base;

import cn.edu.sdtbu.model.properties.Const;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.sql.Timestamp;

/**
 * default filed
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:26
 */

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class BaseEntityWithDeleteTs extends BaseEntity {
    @Column
    private Timestamp deleteAt;

    @PrePersist
    protected void prePersistDelete() {
        if (deleteAt == null) {
            deleteAt = Const.TIME_ZERO;
        }
    }
}