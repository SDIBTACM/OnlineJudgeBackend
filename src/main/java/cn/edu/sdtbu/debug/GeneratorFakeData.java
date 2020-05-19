package cn.edu.sdtbu.debug;

import cn.edu.sdtbu.model.entity.problem.ProblemDescEntity;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.repository.ProblemRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
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
public class GeneratorFakeData {
    Faker faker = new Faker(Locale.CHINA);
    @Resource
    OnlineJudgeProperties properties;
    @Resource
    UserRepository userRepository;
    @Resource
    ProblemRepository problemRepository;
    @Resource
    ProblemDescRepository descRepository;

    public void generatorAll(int total) {
        generatorProblem(total);
        generatorUsers(total, true);
    }

    public void generatorProblem(int total) {
        // generator problem
        List<ProblemEntity> problemEntityList = new LinkedList<>();
        for (int i = 1; i <= total; i++) {
            ProblemEntity entity = ProblemEntity.getDefaultValue();
            entity.setTitle("this is a problem, id is [" + i + "]");
            entity.setOwnerId(1L);
            entity.setId((long) i);
            problemEntityList.add(entity);
        }
        problemRepository.saveAll(problemEntityList);

        // generator problem desc
        List<ProblemDescEntity> descEntities = new LinkedList<>();
        for (int i = 1; i <= total; i++) {
            ProblemDescEntity entity = new ProblemDescEntity();
            entity.setId((long) i);
            entity.setHint(faker.leagueOfLegends().rank());
            entity.setDescription(faker.leagueOfLegends().quote());
            entity.setInput(faker.company().name());
            entity.setOutput(faker.internet().safeEmailAddress());
            entity.setProblemId((long) i);
            entity.setSample(faker.howIMetYourMother().quote());
            descEntities.add(entity);
        }
        descRepository.saveAll(descEntities);
    }

    public List<UserEntity> generatorUsers(int total, boolean save2Db) {
        int offset = properties.getDebug().getGeneratorData() ? 5 : 1;
        HashSet<String> nameSet = new HashSet<>();
        while (nameSet.size() != total) {
            nameSet.add(faker.company().name());
        }
        HashSet<String> emailSet = new HashSet<>();
        while (emailSet.size() != total) {
            emailSet.add(faker.internet().emailAddress());
        }
        Iterator<String> iterator = nameSet.iterator();
        Iterator<String> emailIterator = emailSet.iterator();

        List<UserEntity> userEntities = new LinkedList<>();
        for (int i = offset; i <= total; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
            userEntity.setEmail(emailIterator.next());
            userEntity.setUsername(iterator.next());
            userEntity.setNickname(faker.name().name());
            userEntity.setRole(UserRole.STUDENT);
            userEntity.setSchool("school");
            userEntity.setId((long) i);
            userEntity.setDeleteAt(Const.TIME_ZERO);
            userEntities.add(userEntity);
        }
        if (save2Db) {
            userRepository.saveAll(userEntities);
        }
        return userEntities;
    }
}
