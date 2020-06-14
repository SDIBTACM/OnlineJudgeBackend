package cn.edu.sdtbu.model.entity.user;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
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
 * @date 2020-04-11 10:37
 */
@Data
@Entity
@Table(name = "login_log", indexes = {
    @Index(name = "idx_user_id", columnList = "userId")
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class LoginLogEntity extends BaseEntity {
    @Column
    Long userId;

    @Column(length = 40)
    String ip;

    @Column
    String username;

    @Column
    Timestamp logoutTime;
}
