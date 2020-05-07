package user;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.entity.LoginLogEntity;
import cn.edu.sdtbu.service.LoginLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 15:28
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LoginLogTest {
    @Autowired
    LoginLogService logService;
    @Test
    public void LoginTest(){
        Sort sort = Sort.unsorted();
        logService.login(1L, "127.0.0.1");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<LoginLogEntity> page = logService.select(1L, pageable);
        assert 1 == page.getTotalElements();

        for (int i = 0; i < 11; i++) {
            logService.login(2L, "127.0.0.1");
        }
        page = logService.select(2L, pageable);
        assert 10 == pageable.getPageSize();
    }
}
