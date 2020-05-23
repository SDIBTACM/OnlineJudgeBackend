package cn.edu.sdtbu.service;

import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.user.UserParam;
import cn.edu.sdtbu.model.vo.user.UserCenterVO;
import cn.edu.sdtbu.model.vo.user.UserRankListVO;
import cn.edu.sdtbu.model.vo.user.UserSimpleInfoVO;
import cn.edu.sdtbu.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:01
 */
public interface UserService extends BaseService<UserEntity, Long> {
    UserCenterVO generatorUserCenterVO(UserCenterVO centerVO, Long userId);
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
     * @throws ForbiddenException it mean someone try to act as a user
     * @throws NotFoundException user not found
     * @return user info
     */
    UserEntity login(String rememberToken, String requestIp) throws ForbiddenException, NotFoundException;

    /**
     * login by remember token
     * @param entity user info
     * @param requestIp where user login from
     * @return jwt type string
     */
    String generateRememberToken(UserEntity entity, String requestIp);
    @Async
    void appendLoginLog(UserEntity entity, String ip, Timestamp logoutTime);

    Page<LoginLogEntity> loginLogs(Long userId, Pageable pageable);

    Long fetchSubmitCount(Long userId);

    Long fetchAcceptedCount(Long userId);

    Page<UserRankListVO> fetchRankList(Pageable pageable);

    UserEntity getByUsername(String username);

    void lockUser(Long userId);

    void changePassword(UserEntity entity, String oldPassword, String newPassword);

    List<UserEntity> getAllByUsername(Collection<String> usernames);
    Page<UserSimpleInfoVO> listUserByRole(UserRole role, Pageable pageable);
}
