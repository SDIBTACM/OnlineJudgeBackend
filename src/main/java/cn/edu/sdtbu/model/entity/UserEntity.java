package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.UUID;

/**
 * user entity
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:40
 */
@Data
@Entity
@Table(name = "user", indexes = {
        @Index(name = "uk_username_delete", columnList = "username", unique = true),
        @Index(name = "uk_email_delete", columnList = "email", unique = true),
        @Index(name = "uk_remember_token", columnList = "remember_token", unique = true),
})
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntityWithDeleteTs {

    @Column(name = "username", length = 64, nullable = false)
    String username;

    @Column(name = "nickname", length = 64, nullable = false)
    String nickname;

    @Column(nullable = false)
    String password;

    @Column(length = 32, nullable = false)
    String school;

    @Column(name = "email", nullable = false)
    String email;

    @Column(length = 64, nullable = false)
    UserRole role;

    @Column
    UserStatus status;

    /**
     * used UUID
     */
    @Column(name = "remember_token", length = 36)
    String rememberToken;

    public static UserEntity getUserEntityWithDefault() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(UserRole.STUDENT);
        userEntity.setStatus(UserStatus.NORMAL);
        userEntity.setRememberToken(UUID.randomUUID().toString());
        return userEntity;
    }
}
