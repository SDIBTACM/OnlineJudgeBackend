package user;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.impl.UserServiceImpl;
import cn.edu.sdtbu.util.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.fail;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 21:08
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @Autowired
    UserServiceImpl userService;
    @Test
    public void userEntityCrudTest() throws Exception{
        UserEntity entity = insertUserEntityTest();
        updateUserEntityTest(entity);
        insertUserEntityTest();
        assert 1 == countUserEntityTest(entity);
        try {
            insertUserEntityTest();
            fail();
        } catch (DataIntegrityViolationException ignore){ }
    }


    @Test
    public void transformToEntityTest(){
        UserRegisterParam userRegisterParam = new UserRegisterParam();
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
        UserEntity userEntity = UserEntity.getUserEntityWithDefault();
        userEntity.setEmail("email");
        userEntity.setNickname("nickName");
        userEntity.setPassword("password");
        userEntity.setSchool("school");
        userEntity.setUsername("name");
        userEntity =  repository.saveAndFlush(userEntity);
        assert userEntity.getId() != null;
        return userEntity;
    }

    void updateUserEntityTest(UserEntity before){
        UserEntity after = new UserEntity();
        after.setRememberToken("afterUpdate");
        after.setId(before.getId());
        after.setDeleteAt(TimeUtil.now());
        userService.updateUser(after);
        after = userService.queryUserById(before.getId());
        assert before.getNickname().equals(after.getNickname());
        assert !before.getRememberToken().equals(after.getRememberToken());
        assert after.getRememberToken().equals("afterUpdate");
    }

    UserEntity queryUserEntityById(Long userId){
        return userService.queryUserById(userId);
    }


}
