package cn.edu.sdtbu.model.entity.user;

import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-18 18:24
 */

@Data
@Entity
@Table(name = "user_class", indexes = {
    @Index(name = "unique_class_id_delete", columnList = "classId", unique = true),
    @Index(name = "unique_class_id_delete", columnList = "deleteAt", unique = true)
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserClassEntity extends BaseEntityWithDeleteTs {
    Long userId;
    Long classId;
}
