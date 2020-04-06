package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public boolean insert(UserEntity userEntity) {
        Object obj= userRepository.saveAndFlush(userEntity);
        return true;
    }

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
}
