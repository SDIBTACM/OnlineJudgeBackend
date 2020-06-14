package cn.edu.sdtbu.debug;

import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.entity.contest.ContestResultEntity;
import cn.edu.sdtbu.model.entity.problem.ProblemDescEntity;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.ContestRule;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.ContestParam;
import cn.edu.sdtbu.model.param.ContestProblemParam;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.repository.ProblemRepository;
import cn.edu.sdtbu.repository.contest.ContestResultRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.ContestService;
import cn.edu.sdtbu.util.TimeUtil;
import com.github.javafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-09 16:23
 */
@Component
public class GeneratorFakeData {
    Faker faker = new Faker(Locale.CHINA);
    @Resource
    OnlineJudgeProperties   properties;
    @Resource
    UserRepository          userRepository;
    @Resource
    ContestService          contestService;
    @Resource
    ProblemRepository       problemRepository;
    @Resource
    ProblemDescRepository   descRepository;
    @Resource
    ContestResultRepository contestResultRepository;

    public void generatorAll(int total) {
        generatorUsers(total, true);
        generatorProblem(total);
        generatorContest();
        generatorContestResult();
    }

    private void generatorContestResult() {
        ContestResultEntity entity = new ContestResultEntity();
        entity.setAcAt(TimeUtil.now());
        entity.setContestId(1L);
        entity.setProblemOrder(1);
        entity.setUserId(1L);
        entity.setSubmitCount(2L);
        contestResultRepository.save(entity);
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

    public void generatorContest() {
        long               nowMills = System.currentTimeMillis();
        UserEntity         entity   = userRepository.findById(1L).get();
        List<ContestParam> params   = new LinkedList<>();
        params.add(generatorCommon(nowMills, true));
        params.add(generatorCommon(nowMills, false));
        params.add(generatorNeedRegisterContest(generatorCommon(nowMills, true), nowMills));
        params.add(generatorOIContest(generatorCommon(nowMills, true)));
        params.add(generatorProtectedContest(generatorCommon(nowMills, true)));
        params.forEach(i -> contestService.createContest(i, entity));
    }

    public void generatorUsers(int total, boolean save2Db) {
        int             offset  = properties.getDebug().getGeneratorData() ? 5 : 1;
        HashSet<String> nameSet = new HashSet<>();
        while (nameSet.size() != total) {
            nameSet.add(faker.company().name());
        }
        HashSet<String> emailSet = new HashSet<>();
        while (emailSet.size() != total) {
            emailSet.add(faker.internet().emailAddress());
        }
        Iterator<String> iterator      = nameSet.iterator();
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
            userEntity.setDeleteAt(OnlineJudgeConstant.TIME_ZERO);
            userEntities.add(userEntity);
        }
        if (save2Db) {
            userRepository.saveAll(userEntities);
        }
    }

    private ContestParam generatorNeedRegisterContest(ContestParam param, long now) {
        param.setPrivilege(ContestPrivilege.NEED_REGISTER);
        param.setRegisterBegin(now);
        param.setRegisterEnd(param.getStartAt());
        return param;
    }

    private ContestParam generatorProtectedContest(ContestParam param) {
        param.setPassword("password");
        param.setPrivilege(ContestPrivilege.PROTECT);
        return param;
    }

    private ContestParam generatorOIContest(ContestParam param) {
        param.setContestRule(ContestRule.OI);
        return param;
    }

    private ContestParam generatorCommon(long now, boolean isRunning) {
        ContestParam param = new ContestParam();
        param.setName(faker.name().title());
        param.setContestRule(ContestRule.ACM);
        param.setDescription("need register");
        param.setEndBefore(now + TimeUnit.HOURS.toMillis(3));
        param.setStartAt(now + (isRunning ? 0 : TimeUnit.HOURS.toMillis(1)));
        param.setPrivilege(ContestPrivilege.PUBLIC);
        List<ContestProblemParam> problems = new LinkedList<>();
        problems.add(new ContestProblemParam(1L, null));
        param.setProblems(problems);
        return param;
    }
}
