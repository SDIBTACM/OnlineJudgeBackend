package user;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.enums.UserStatus;
import cn.edu.sdtbu.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020/4/6 下午9:08
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserRepositoryTest {
    @Autowired
    private UserService userService;

    @Test
    public void insertTest(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("email");
        userEntity.setNickName("nickName");
        userEntity.setPassword("password");
        userEntity.setRememberToken("12312312321");
        userEntity.setRole("role");
        userEntity.setSchool("school");
        userEntity.setStatus(UserStatus.BAN);
        userEntity.setUserName("name");
        userService.insert(userEntity);
    }
}
