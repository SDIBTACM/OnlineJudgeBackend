package cn.edu.sdtbu.model.entity.contest;

import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import cn.edu.sdtbu.model.enums.ContestPrivilege;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-27 09:26
 */

@Data
@Entity
@Table(name = "contest", indexes = {
    @Index(name = "idx_owner_id_start_at", columnList = "ownerId"),
    @Index(name = "idx_owner_id_start_at", columnList = "startAt"),
    @Index(name = "idx_privilege_start_at", columnList = "privilege"),
    @Index(name = "idx_privilege_start_at", columnList = "startAt"),
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContestEntity extends BaseEntityWithDeleteTs {
    @Column
    Long ownerId;

    @Column
    String name;

    @Column
    Timestamp startAt;

    @Column
    Timestamp endBefore;

    @Column
    Timestamp lockRankAt;

    @Column
    ContestPrivilege privilege;

    @Column
    String privilegeInfo;
}
