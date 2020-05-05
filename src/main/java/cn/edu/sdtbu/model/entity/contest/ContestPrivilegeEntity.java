package cn.edu.sdtbu.model.entity.contest;

import cn.edu.sdtbu.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-02 19:32
 */
@Data
@Entity
@Table(name = "contest_privilege")
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContestPrivilegeEntity extends BaseEntity {
     @Column
     Long contestId;

     @Column
     Long userId;

     @Column
     Long type;
}
