package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.util.Optional;

/**
 * user repository
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:56
 */
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    /**
     * count by user name or email
     * @param username user name
     * @param email    email
     * @return         result
     */
    int countByUsernameOrEmail(String username, String email);

    /**
     * find user info by user name and password
     * @param username name
     * @return         user info
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * find user info by user name and password
     * @param email name
     * @return         user info
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * find by token
     * @param token token
     * @return      token
     */
    Optional<UserEntity> findByRememberToken(String token);
}
