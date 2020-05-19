package cn.edu.sdtbu.model.entity.user;

import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import cn.edu.sdtbu.model.enums.UserRole;
import com.alibaba.fastjson.JSON;
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
                @UniqueConstraint(name = "uk_username_delete", columnNames = {"username", "deleteAt"}),
                @UniqueConstraint(name = "uk_email_delete", columnNames = {"email", "deleteAt"}),
                @UniqueConstraint(name = "uk_token_delete", columnNames = {"rememberToken", "deleteAt"})
        })
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntityWithDeleteTs {

    @Column(length = 128, nullable = false)
    String username;

    @Column(length = 64, nullable = false)
    String nickname;

    @Column(nullable = false)
    String password;

    @Column(length = 32, nullable = false)
    String school;

    @Column(length = 128, nullable = false)
    String email;

    @Column(nullable = false)
    UserRole role;

    /**
     * used UUID
     */
    @Column(length = 36)
    String rememberToken;


    public static UserEntity getDefaultValue() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(UserRole.STUDENT);
        return userEntity;
    }
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
    @PrePersist
    protected void prePersist() {
        rememberToken = UUID.randomUUID().toString();
    }
}
