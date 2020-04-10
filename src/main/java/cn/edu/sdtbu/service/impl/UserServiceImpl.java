package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.ao.UserRegisterAO;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.EncryptionUtil;
import cn.edu.sdtbu.util.SpringBeanUtil;
import com.google.common.primitives.UnsignedLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TODO
 *
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
    public boolean addUser(UserRegisterAO ao) {
        ao.setPassword(EncryptionUtil.sha256(ao.getPassword(), ao.getUserName()));
        userRepository.saveAndFlush(
            ao.transformToEntity(UserEntity.getUserEntityWithDefault())
        );
        return true;
    }

    @Override
    public boolean updateUser(UserEntity userEntity) {
        UserEntity entity = userRepository.findById(userEntity.getId()).orElseThrow(() ->
            new NotFoundException("not such user")
        );
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
}
