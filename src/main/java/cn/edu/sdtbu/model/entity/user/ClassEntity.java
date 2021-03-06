package cn.edu.sdtbu.model.entity.user;

import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-04 14:44
 */
@Data
@Entity
@Table(name = "class")
@ToString
@EqualsAndHashCode(callSuper = true)
public class ClassEntity extends BaseEntityWithDeleteTs {
    @Column(length = 64)
    String name;

    @Column
    Long ownerId;

}
