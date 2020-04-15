package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.service.base.BaseService;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:01
 */
public interface UserService extends BaseService<UserEntity, Long> {
    /**
     * insert user info
     * @param userRegisterParam    user entity info
     * @return          is inserted
     */
    boolean addUser(UserParam userRegisterParam);

    /**
     * use email or username as identify and login with password
     * @param identify username or email
     * @param password string
     * @param requestIp where user login from
     * @return  user info
     */
    UserEntity login(String identify, String password, String requestIp);

    /**
     * login by remember token
     * @param rememberToken a jwt string to verify user identify
     * @param requestIp where user login from
     * @return user info
     */
    UserEntity login(String rememberToken, String requestIp);

    /**
     * login by remember token
     * @param entity user info
     * @param requestIp where user login from
     * @return jwt type string
     */
    String generateRememberToken(UserEntity entity, String requestIp);
}
