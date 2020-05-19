package cn.edu.sdtbu.model.entity.user;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-18 18:24
 */

@Data
@Entity
@Table(name = "user_class")
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserClassEntity extends BaseEntity {
    Long userId;
    Long classId;
}
