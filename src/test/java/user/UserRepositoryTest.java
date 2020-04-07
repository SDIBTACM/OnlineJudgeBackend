package user;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.ao.UserAO;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    UserService userService;
    @Test
    public void userEntityCrudTest(){
        UserEntity entity = insertUserEntityTest();
        updateUserEntityTest(entity);
    }

    @Test
    public void transformToEntityTest(){
        UserAO userAO = new UserAO();
        userAO.setUserName("123");
        userAO.setEmail("123");
        userAO.setNickname("123");
        UserEntity userEntity = userAO.transformToEntity();
        assert userEntity.getUserName().equals(userAO.getUserName());
        assert userEntity.getEmail().equals(userAO.getEmail());
        assert userEntity.getNickname().equals(userAO.getNickname());
    }

    UserEntity insertUserEntityTest(){
        UserEntity userEntity = UserEntity.getUserEntityWithDefault();
        userEntity.setEmail("email");
        userEntity.setNickname("nickName");
        userEntity.setPassword("password");
        userEntity.setSchool("school");
        userEntity.setUserName("name");
        userEntity =  repository.saveAndFlush(userEntity);
        assert userEntity.getId() != null;
        return userEntity;
    }

    void updateUserEntityTest(UserEntity before){
        UserEntity after = new UserEntity();
        after.setRememberToken("afterUpdate");
        after.setId(before.getId());
        userService.updateUser(after);
        after = userService.queryUserById(before.getId());
        assert before.getNickname().equals(after.getNickname());
        assert !before.getRememberToken().equals(after.getRememberToken());
        assert after.getRememberToken().equals("afterUpdate");
    }

    UserEntity queryUserEntityById(int userId){
        return userService.queryUserById(userId);
    }


}
