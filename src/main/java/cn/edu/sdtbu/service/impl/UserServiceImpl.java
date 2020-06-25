package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.NotAcceptableException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.exception.UnAuthedException;
import cn.edu.sdtbu.model.constant.ExceptionConstant;
import cn.edu.sdtbu.model.constant.KeyPrefixConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.constant.WebContextConstant;
import cn.edu.sdtbu.model.dto.UserRankListDTO;
import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.user.UserParam;
import cn.edu.sdtbu.model.vo.user.UserCenterVO;
import cn.edu.sdtbu.model.vo.user.UserRankListVO;
import cn.edu.sdtbu.model.vo.user.UserSimpleInfoVO;
import cn.edu.sdtbu.repository.user.LoginLogRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:02
 */

@Service
@Slf4j
public class UserServiceImpl extends AbstractBaseService<UserEntity, Long> implements UserService {
    private final LoginLogRepository loginLogRepository;
    private final UserRepository     userRepository;

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
        simpleInfoVO.setRank(cache().zRank(KeyPrefixConstant.USERS_RANK_LIST_DTO, userId + "", false));
        centerVO.setUserInfo(simpleInfoVO);
        return centerVO;
    }


    @Override
    public boolean addUser(UserParam ao) {
        // Use BCrypt for other language service
        ao.setPassword(BCrypt.hashpw(ao.getPassword(), BCrypt.gensalt()));
        save(ao.transformToEntity(UserEntity.getDefaultValue()));
        return true;
    }

    @Override
    public void userMustNotExist(UserParam ao) {
        if (cache().get(CacheUtil.defaultKey(String.class, ao.getEmail(), KeyPrefixConstant.REGISTERED_EMAIL)) != null ||
            cache().get(CacheUtil.defaultKey(String.class, ao.getUsername(), KeyPrefixConstant.REGISTERED_USERNAME)) != null ||
            countByUserNameOrEmail(ao.getUsername(), ao.getEmail()) != 0) {
            throw ExceptionConstant.REGISTER_ACCOUNT_EXISTED;
        }
    }

    @Override
    public UserEntity login(String identify, String password, String requestIp) {
        Optional<UserEntity> optional = userRepository.findByUsernameAndDeleteAtEquals(identify, OnlineJudgeConstant.TIME_ZERO);
        if (optional.isEmpty() || !BCrypt.checkpw(password, optional.get().getPassword())) {
            throw new UnAuthedException("identify or password error");
        }
        return isLocked(optional.get());
    }

    @Override
    public UserEntity login(String rememberToken, String requestIp) {
        DecodedJWT jwtUnVerify = JWT.decode(rememberToken);
        Optional<UserEntity> optional = userRepository.findByUsernameAndDeleteAtEquals(
            jwtUnVerify.getClaim("username").asString(), OnlineJudgeConstant.TIME_ZERO);
        if (optional.isPresent()) {
            return null;
        }
        UserEntity entity = optional.get();
        Algorithm   algorithm = Algorithm.HMAC512(entity.getPassword() + entity.getRememberToken());
        JWTVerifier verifier  = JWT.require(algorithm).build();
        try {
            verifier.verify(rememberToken);
        } catch (JWTVerificationException e) {
            log.info(String.format("well, someone try to act as %s, IP is %s", entity.getUsername(), requestIp));
            return null;
        }
        return isLocked(entity);
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
            .withExpiresAt(new Date(System.currentTimeMillis() + WebContextConstant.REMEMBER_TOKEN_EXPRESS_TIME))
            .sign(algorithm);
    }

    public int countByUserNameOrEmail(String name, String email) {
        return userRepository.countByUserNameOrEmail(name, email, OnlineJudgeConstant.TIME_ZERO);
    }


    @Override
    public void appendLoginLog(UserEntity entity, String ip, Timestamp logoutTime) {
        LoginLogEntity loginEntity = new LoginLogEntity();
        loginEntity.setIp(ip);
        loginEntity.setUsername(entity.getUsername());
        loginEntity.setUserId(entity.getId());
        loginEntity.setLogoutTime(logoutTime);
        loginLogRepository.saveAndFlush(loginEntity);
    }

    @Override
    public Page<LoginLogEntity> loginLogs(Long userId, Pageable pageable) {
        return loginLogRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Long fetchSubmitCount(Long userId) {
        return fetchCount(userId, KeyPrefixConstant.TOTAL_SUBMIT_COUNT);
    }

    @Override
    public Long fetchAcceptedCount(Long userId) {
        return fetchCount(userId, KeyPrefixConstant.USER_ACCEPTED_COUNT);
    }

    @Override
    public Page<UserRankListVO> fetchRankList(Pageable pageable) {
        //TODO init rank list from db
        long                  total = cache().totalElementOfList(KeyPrefixConstant.USERS_RANK_LIST_DTO);
        List<UserRankListDTO> list  = new LinkedList<>();
        cache().fetchRanksByPage(KeyPrefixConstant.USERS_RANK_LIST_DTO, pageable, false)
            .forEach(i -> list.add(UserRankListDTO.converByTuple(i)));

        Map<Long, UserEntity> userEntities = getByIds(list.stream()
            .map(UserRankListDTO::getId)
            .collect(Collectors.toList()), Pageable.unpaged())
            .getContent().stream()
            .collect(Collectors.toMap(UserEntity::getId, u -> u));

        List<UserRankListVO> vos = new ArrayList<>(list.size());
        list.forEach(item -> {
            UserRankListVO rankListVO = new UserRankListVO();
            rankListVO.setAcceptedCount(item.getAcceptedCount());
            rankListVO.setSubmitCount(item.getSubmitCount());
            rankListVO.setId(item.getId());
            UserEntity entity = userEntities.get(item.getId());
            rankListVO.setNickname(entity.getNickname());
            rankListVO.setUsername(entity.getUsername());
            vos.add(rankListVO);
        });
        return new PageImpl<>(vos, pageable, total);
    }

    @Override
    public UserEntity getByUsername(String username) {
        String user = cache().get(CacheUtil.defaultKey(UserEntity.class, username, KeyPrefixConstant.USERNAME));
        if (user == null) {
            UserEntity userEntity = userRepository.getByUsernameAndDeleteAt(username, OnlineJudgeConstant.TIME_ZERO);
            cache().put(CacheUtil.defaultKey(UserEntity.class, username, KeyPrefixConstant.USERNAME), JSON.toJSONString(userEntity));
            if (userEntity == null) {
                throw new NotFoundException("未找到此用户，用户名为 " + username);
            }
            return userEntity;
        }
        return JSON.parseObject(user, UserEntity.class);
    }

    @Override
    public void lockUser(Long userId) {
        UserEntity userEntity = getById(userId);
        userEntity.setRole(UserRole.LOCKED);
        save(userEntity);
    }

    @Override
    public void changePassword(UserEntity entity, String oldPassword, String newPassword) {
        if (entity == null) {
            throw ExceptionConstant.NO_PERMISSION;
        }
        if (!BCrypt.checkpw(oldPassword, entity.getPassword())) {
            throw new UnAuthedException("old password error");
        }
        if (!(newPassword.length() >= 7 && newPassword.length() < 1 << 10)) {
            throw new NotAcceptableException("password must be longer than 7 and shorter than 20");
        }
        entity = getById(entity.getId());
        entity.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        save(entity);
    }

    @Override
    public List<UserEntity> getAllByUsername(Collection<String> usernames) {
        return userRepository.findAllByUsernameInAndDeleteAt(usernames, OnlineJudgeConstant.TIME_ZERO);
    }

    @Override
    public Page<UserSimpleInfoVO> listUserByRole(UserRole role, Pageable pageable) {
        Page<UserEntity>       entities = userRepository.getAllByRoleAndAndDeleteAt(role, OnlineJudgeConstant.TIME_ZERO, pageable);
        List<UserSimpleInfoVO> list     = new LinkedList<>();
        entities.getContent().forEach(item -> {
            UserSimpleInfoVO simpleInfoVO = new UserSimpleInfoVO();
            SpringUtil.cloneWithoutNullVal(item, simpleInfoVO);
            list.add(simpleInfoVO);
        });
        return new PageImpl<>(list, pageable, entities.getTotalElements());
    }

    private Long fetchCount(Long userId, String prefix) {
        String key = CacheUtil.defaultKey(UserEntity.class, userId, prefix);
        String res = cache().get(key);
        return StringUtils.isEmpty(res) ? countService.fetchCount(key) : Long.parseLong(res);
    }

    private Timestamp lastLogin(Long userId) {
        Pageable             pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createAt");
        Page<LoginLogEntity> page     = loginLogRepository.findAllByUserId(userId, pageable);
        return page.hasContent() ? page.getContent().get(0).getCreateAt() : null;
    }

    private UserEntity isLocked(UserEntity entity) {
        if (entity.getRole().equals(UserRole.LOCKED)) {
            throw new UnAuthedException("you have been locked");
        }
        return entity;
    }
}
