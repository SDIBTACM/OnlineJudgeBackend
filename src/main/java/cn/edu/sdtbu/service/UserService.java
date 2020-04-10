package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.ao.UserRegisterAO;
import cn.edu.sdtbu.model.entity.UserEntity;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:01
 */
public interface UserService {
    /**
     * insert user info
     * @param userRegisterAO    user entity info
     * @return          is inserted
     */
    boolean addUser(UserRegisterAO userRegisterAO);

    /**
     * update user info
     * @param userEntity user entiry
     * @return           is updated
     */
    boolean updateUser(UserEntity userEntity);

    /**
     * query by id
     * @param userId    user id
     * @return          user info
     */
    UserEntity queryUserById(Long userId);
}
