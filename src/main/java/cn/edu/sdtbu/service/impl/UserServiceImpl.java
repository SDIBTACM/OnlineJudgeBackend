package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.ExistException;
import cn.edu.sdtbu.exception.NotAcceptableException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.EncryptionUtil;
import cn.edu.sdtbu.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:02
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
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
        ao.setPassword(EncryptionUtil.sha256(ao.getPassword(), ao.getUserName()));
        if (userRepository.countByUserNameOrEmail(ao.getUserName(), ao.getEmail()) != 0) {
            throw new ExistException("user name or email is registered");
        }
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
    public UserEntity login(String userName, String password) {
        Optional<UserEntity> optional= userRepository.findByUserNameAndPassword(userName, EncryptionUtil.sha256(password, userName));
        UserEntity entity = optional.orElseThrow(() -> new NotAcceptableException("account or password error"));
        entity.setRememberToken(UUID.randomUUID().toString());
        userRepository.saveAndFlush(entity);
        return entity;
    }

    @Override
    public UserEntity login(String rememberToken) {
        return userRepository.findByRememberToken(rememberToken).orElseThrow(()->new NotFoundException("not found such user"));
    }
}
