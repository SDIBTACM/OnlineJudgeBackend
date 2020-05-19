package cn.edu.sdtbu.model.entity.contest;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-27 10:12
 */
@Data
@Entity
@Table(name = "contest_ip_limit", indexes = {
    @Index(name = "uk_contest_id", columnList = "contestId", unique = true)
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContestIpLimitEntity extends BaseEntity {
    @Column
    Long contestId;

    @Lob
    @Column
    String denyIps;

    @Lob
    @Column
    String allowIps;
}
