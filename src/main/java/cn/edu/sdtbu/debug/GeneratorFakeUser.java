package cn.edu.sdtbu.debug;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.enums.UserStatus;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.repository.UserRepository;
import cn.edu.sdtbu.service.UserService;
import com.github.javafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-09 16:23
 */
@Component
public class GeneratorFakeUser {
    Faker faker = new Faker(Locale.CHINA);
    @Resource
    OnlineJudgeProperties properties;
    @Resource
    UserRepository userRepository;

    public List<UserEntity> generatorUsers(int n, boolean save2Db) {
        int offset = properties.getDebug().getGeneratorData() ? 5 : 1;
        HashSet<String> nameSet = new HashSet<>();
        while (nameSet.size() != n) {
            nameSet.add(faker.name().username());
        }
        HashSet<String>emailSet = new HashSet<>();
        while (emailSet.size() != n) {
            emailSet.add(faker.internet().emailAddress());
        }
        Iterator<String> iterator = nameSet.iterator();
        Iterator<String> emailIterator = emailSet.iterator();

        List<UserEntity> userEntities = new LinkedList<>();
        for (int i = offset; i <= n; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
            userEntity.setEmail(emailIterator.next());
            userEntity.setUsername(iterator.next());
            userEntity.setNickname(faker.name().name());
            userEntity.setRole(UserRole.STUDENT);
            userEntity.setSchool("school");
            userEntity.setStatus(UserStatus.NORMAL);
            userEntity.setId((long) i);
            userEntities.add(userEntity);
        }
        if (save2Db) {
            userRepository.saveAll(userEntities);
        }
        return userEntities;
    }
}
