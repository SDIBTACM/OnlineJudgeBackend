package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.ExistException;
import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.UserCenterVO;
import cn.edu.sdtbu.model.vo.UserSimpleInfoVO;
import cn.edu.sdtbu.repository.LoginLogRepository;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.SpringUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import static cn.edu.sdtbu.model.properties.Const.REMEMBER_TOKEN_EXPRESS_TIME;


/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:02
 */

@Service
@Slf4j
public class UserServiceImpl extends AbstractBaseService<UserEntity, Long> implements UserService {
    private final LoginLogRepository loginLogRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(LoginLogRepository loginLogRepository, UserRepository userRepository) {
        super(userRepository);
        this.loginLogRepository = loginLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserCenterVO generatorUserCenterVO(UserCenterVO centerVO, Long userId) {
        centerVO.setLastLogin(lastLogin(userId));
        UserSimpleInfoVO simpleInfoVO = new UserSimpleInfoVO();
        SpringUtil.cloneWithoutNullVal(getById(userId), simpleInfoVO);
        simpleInfoVO.setRank(userRank(userId));
        centerVO.setUserInfo(simpleInfoVO);
        return centerVO;
    }


    @Override
    public boolean addUser(UserParam ao) {
        if (countByUserNameOrEmail(ao.getUsername(), ao.getEmail()) != 0) {
            throw new ExistException("user name or email is registered");
        }
        // Use BCrypt for other language service
        ao.setPassword(BCrypt.hashpw(ao.getPassword(), BCrypt.gensalt()));
        save(ao.transformToEntity(UserEntity.getDefaultValue()));
        return true;
    }

    @Override
    public UserEntity login(String identify, String password, String requestIp) {
        Optional<UserEntity> optional;
        if (EmailValidator.getInstance(true, false).isValid(identify)) {
            optional = userRepository.findByEmailAndDeleteAtEquals(identify, Const.TIME_ZERO);
        } else {
            optional = userRepository.findByUsernameAndDeleteAtEquals(identify, Const.TIME_ZERO);
        }
        if (optional.isEmpty() || !BCrypt.checkpw(password, optional.get().getPassword())) {
            throw new ForbiddenException("identify or password error");
        }
        userLogin(optional.get(), requestIp);
        return optional.get();
    }

    @Override
    public UserEntity login(String rememberToken, String requestIp) throws ForbiddenException, NotFoundException {
        DecodedJWT jwtUnVerify = JWT.decode(rememberToken);
        Optional<UserEntity> optional = userRepository.findByUsernameAndDeleteAtEquals(
                jwtUnVerify.getClaim("username").asString(), Const.TIME_ZERO);
        UserEntity entity = optional.orElseThrow(() -> new NotFoundException("who are you"));

        Algorithm algorithm = Algorithm.HMAC512(entity.getPassword() + entity.getRememberToken());
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(rememberToken);
        } catch (Exception e) {
            log.info("well, someone try to act as " + entity.getUsername());
            throw new ForbiddenException("not found user");
        }
        userLogin(entity, requestIp);
        return entity;
    }

    @Override
    public String generateRememberToken(UserEntity entity, String requestIp) {
        // 使用用户的密码 hash + remember token 作为 remember token jwt 的加密密钥
        // 如果用户修改了密码，则 token 会全部失效
        Algorithm algorithm = Algorithm.HMAC512(entity.getPassword() + entity.getRememberToken());

        return JWT.create()
            .withClaim("username", entity.getUsername())
            .withClaim("ip when sign", requestIp)
            .withSubject("oh, it's you")
            .withIssuer("cn.edu.sdtbu.acm.UserService.remember")
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + REMEMBER_TOKEN_EXPRESS_TIME))
            .sign(algorithm);
    }

    public int countByUserNameOrEmail(String name, String email) {
        return userRepository.countByUserNameOrEmail(name, email, Const.TIME_ZERO);
    }

    private void userLogin(UserEntity user, String requestIp) {
        log.info("user [{}] login from [{}]", user.getUsername(), requestIp);
        appendLoginLog(user.getId(), requestIp);
    }

    private void appendLoginLog(Long userId, String ip) {
        LoginLogEntity entity = new LoginLogEntity();
        entity.setIp(ip);
        entity.setUserId(userId);
        loginLogRepository.saveAndFlush(entity);
    }

    @Override
    public Page<LoginLogEntity> loginLogs(Long userId, Pageable pageable) {
        return loginLogRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Long fetchSubmitCount(Long userId) {
        return fetchCount(userId, KeyPrefix.TOTAL_SUBMIT_COUNT);
    }

    @Override
    public Long fetchAcceptedCount(Long userId) {
        return fetchCount(userId, KeyPrefix.USER_ACCEPTED_COUNT);
    }

    private Long fetchCount(Long userId, KeyPrefix prefix) {
        String key = CacheUtil.countKey(UserEntity.class, userId, prefix);
        String res = cache().get(key);
        return StringUtils.isEmpty(res) ? countService.fetchCount(key) : Long.parseLong(res);
    }

    private Timestamp lastLogin(Long userId) {
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createAt");
        Page<LoginLogEntity> page = loginLogRepository.findAllByUserId(userId,pageable);
        return page.hasContent() ? page.getContent().get(0).getCreateAt() : null;
    }

    private Integer userRank(Long userId) {
        return -1;
    }
}
