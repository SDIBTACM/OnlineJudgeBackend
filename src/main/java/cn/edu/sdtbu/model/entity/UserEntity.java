package cn.edu.sdtbu.model.entity;

import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

/**
 * user entity
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:40
 */
@Data
@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_username_delete", columnNames = {"username", "deleted"}),
                @UniqueConstraint(name = "uk_email_delete", columnNames = {"email", "deleted"}),
                @UniqueConstraint(name = "uk_token_delete", columnNames = {"remember_token", "deleted"})
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

    @Column(length = 64, name = "email", nullable = false)
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
