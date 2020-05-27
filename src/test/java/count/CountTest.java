package count;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.service.CountService;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeoutException;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-07 14:59
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class CountTest {
    @Autowired
    ProblemService problemService;
    @Autowired
    CountService countService;
    @Autowired
    UserService userService;

    @Test
    public void test() throws InterruptedException, TimeoutException {
        Long userId = 1L;
        String key = CacheUtil.defaultKey(UserEntity.class, userId, KeyPrefix.USER_ACCEPTED_COUNT);
        problemService.fetchAllUserSubmitStatus(userId);
        // wait count submit( async )
        Thread.sleep(200);

        long before = userService.fetchAcceptedCount(userId);
        countService.incCount(key);
        int cnt = 0;
        while (countService.fetchCount(key) - 1L != before) {
            Thread.sleep(20);
            log.info("test thread has ben sleep [ {} ] mills,add before: [{}], target: [{}]",
                (++cnt) * 10,
                before,
                countService.fetchCount(key));
            if (cnt * 10 == 1000) {
                throw new TimeoutException("time out");
            }
        }
    }
}
