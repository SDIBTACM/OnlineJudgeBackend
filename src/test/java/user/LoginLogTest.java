package user;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.service.LoginLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 15:28
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class LoginLogTest {
    @Autowired
    LoginLogService logService;
    @Test
    public void LoginTest() throws InterruptedException {
        Sort sort = Sort.unsorted();
        loginLogout(1L, "127.0.0.1");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<LoginLogEntity> page = logService.select(1L, pageable);

        assert 1 <= page.getTotalElements();
        for (int i = 0; i < 11; i++) {
            loginLogout(2L, "127.0.0.1");
        }
        page = logService.select(2L, pageable);
        assert 10 <= page.getTotalElements();
    }
    private void loginLogout(Long userId, String ip) throws InterruptedException {
        logService.login(userId, ip);
        Thread.sleep(1000);
        logService.logout(userId);
    }
}
