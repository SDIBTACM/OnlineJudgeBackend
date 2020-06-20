package user;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.user.UserParam;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.impl.UserServiceImpl;
import cn.edu.sdtbu.util.TimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.junit.Assert.fail;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:08
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    UserRepository repository;
    @Autowired
    UserServiceImpl userService;
    @Test
    public void userEntityCrudTest(){
        UserEntity entity = insertUserEntityTest();
        assert 1 == countUserEntityTest(entity);
        updateUserEntityTest(entity);
        assert 0 == countUserEntityTest(entity);
    }


    @Test
    public void transformToEntityTest(){
        UserParam userRegisterParam = new UserParam();
        userRegisterParam.setUsername("123");
        userRegisterParam.setEmail("123");
        userRegisterParam.setNickname("123");
        UserEntity userEntity = userRegisterParam.transformToEntity();
        assert userEntity.getUsername().equals(userRegisterParam.getUsername());
        assert userEntity.getEmail().equals(userRegisterParam.getEmail());
        assert userEntity.getNickname().equals(userRegisterParam.getNickname());
    }

    int countUserEntityTest(UserEntity entity){
        return userService.countByUserNameOrEmail(entity.getUsername(), entity.getEmail());
    }
    UserEntity insertUserEntityTest(){
        UserEntity userEntity = UserEntity.getDefaultValue();
        userEntity.setEmail(RandomStringUtils.randomAlphanumeric(8) + "@email.com");
        userEntity.setNickname("nickName");
        userEntity.setPassword("password");
        userEntity.setSchool("school");
        userEntity.setUsername(RandomStringUtils.randomAlphabetic(8));
        userEntity.setRememberToken("token");
        userEntity =  repository.saveAndFlush(userEntity);
        assert userEntity.getId() != null;
        return userEntity;
    }

    void updateUserEntityTest(UserEntity before){
        before.setRememberToken("afterUpdate");
        before.setDeleteAt(TimeUtil.now());
        userService.update(before, before.getId());
    }

}
