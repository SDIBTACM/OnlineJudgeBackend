package cn.edu.sdtbu.model.enums;

import cn.edu.sdtbu.model.entity.user.UserEntity;

/**
 * user status, {@link UserEntity} used this enum.
 * can be changed status by ADMIN {@link cn.edu.sdtbu.model.enums.UserRole}
 * @author bestsort
 * @version 1.0
 * @date 2020/4/6 下午8:45
 */
public enum UserStatus {
    // lock
    LOCK,
    // normal
    NORMAL;
}
