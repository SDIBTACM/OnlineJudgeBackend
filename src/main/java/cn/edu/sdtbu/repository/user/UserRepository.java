package cn.edu.sdtbu.repository.user;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
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
     * @param deleteAt deleted time
     * @return         result
     */
    @Query(value = "select count(*) from user u where " +
            "(u.username = ?1 and u.delete_at = ?3) or (u.email = ?2 and u.delete_at = ?3)",nativeQuery = true)
    int countByUserNameOrEmail(String username, String email, Timestamp deleteAt);

    /**
     * fetch user info by role
     * @param role user role, {@link UserRole}
     * @param deleteAt time zero, {@link cn.edu.sdtbu.model.properties.Const#TIME_ZERO}
     * @return list
     */
    Page<UserEntity> getAllByRoleAndAndDeleteAt(UserRole role, Timestamp deleteAt, Pageable page);

    UserEntity getByUsernameAndDeleteAt(String username, Timestamp deleteAt);
    /**
     * find user info by user name
     * @param username name
     * @param deleteAt deleted time
     * @return user info
     */
    Optional<UserEntity> findByUsernameAndDeleteAtEquals(String username, Timestamp deleteAt);
    /**
     * find user info by user name and password
     * @param email name
     * @param deleteAt deleted time
     * @return user info
     */
    Optional<UserEntity> findByEmailAndDeleteAtEquals(String email, Timestamp deleteAt);

    /**
     * find by token
     * @param token not null
     * @param deleteAt deleted time
     * @return token
     */
    Optional<UserEntity> findByRememberTokenAndDeleteAtEquals(String token, Timestamp deleteAt);

    List<UserEntity> findAllByUsernameInAndDeleteAt(Collection<String> usernames, Timestamp deleteAt);
    List<UserEntity> findAllByIdInAndDeleteAt(Collection<Long> ids, Timestamp deleteAt);
}
