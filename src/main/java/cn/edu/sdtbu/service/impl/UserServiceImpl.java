package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.ExistException;
import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.SpringBeanUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl implements UserService {
    private final LoginLogService loginLogService;
    private final UserRepository userRepository;
    public UserServiceImpl(LoginLogService loginLogService, UserRepository userRepository) {
        this.loginLogService = loginLogService;
        this.userRepository = userRepository;
    }

    public UserEntity findById(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return userEntity.orElseThrow(() ->
            new NotFoundException(
                String.format("user not found, id: [%d], please check it", userId)
            )
        );
    }
    @Override
    public boolean addUser(UserRegisterParam ao) {
        if (countByUserNameOrEmail(ao.getUsername(), ao.getEmail()) != 0) {
            throw new ExistException("user name or email is registered");
        }
        // Use BCrypt for other language service
        ao.setPassword(BCrypt.hashpw(ao.getPassword(), BCrypt.gensalt()));
        userRepository.saveAndFlush(
            ao.transformToEntity(UserEntity.getUserEntityWithDefault())
        );
        return true;
    }

    @Override
    public boolean updateUser(UserEntity userEntity) {
        UserEntity entity = userRepository.findById(
                userEntity.getId()).orElseThrow(() -> new NotFoundException("not such user"));
        BeanUtils.copyProperties(userEntity, entity, SpringBeanUtil.getNullPropertyNames(userEntity));
        userRepository.saveAndFlush(entity);
        return true;
    }

    @Override
    public UserEntity queryUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
            new NotFoundException("not found this user witch id is " + userId)
        );
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
    public UserEntity login(String rememberToken, String requestIp) {
        DecodedJWT jwtUnVerify = JWT.decode(rememberToken);
        Optional<UserEntity> optional = userRepository.findByUsernameAndDeleteAtEquals(
                jwtUnVerify.getClaim("username").asString(), Const.TIME_ZERO);
        UserEntity entity = optional.orElseThrow(() -> new NotFoundException("who are you"));
        userLogin(entity, requestIp);

        Algorithm algorithm = Algorithm.HMAC256(entity.getPassword() + entity.getRememberToken());
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(rememberToken);
        } catch (Exception e) {
            log.info("well, someone try to act as " + entity.getUsername());
            throw new ForbiddenException("not found such user");
        }
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
        loginLogService.login(user.getId(), requestIp);
    }
}
