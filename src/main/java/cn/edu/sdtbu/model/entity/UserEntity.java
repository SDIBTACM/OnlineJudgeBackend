package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * user entity
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:40
 */
@Data
@Entity
@Table(name = "user") //TODO create index
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity{

    @Column(length = 64, nullable = false)
    String userName;

    @Column(length = 64, nullable = false)
    String nickname;

    @Column(nullable = false)
    String password;

    @Column(length = 32, nullable = false)
    String school;

    @Column(nullable = false)
    String email;

    @Column(length = 64, nullable = false)
    String role;

    @Column
    UserStatus status = UserStatus.BAN;

    /**
     * used UUID
     */
    @Column(length = 36)
    String rememberToken;
}
