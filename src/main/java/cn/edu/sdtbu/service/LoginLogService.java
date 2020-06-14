package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 10:43
 */
public interface LoginLogService extends BaseService<LoginLogEntity, Long> {
    /**
     * record login ip
     *
     * @param userId user
     * @param ip     login ip
     */
    void login(Long userId, String ip);

    /**
     * select login info by pageable
     *
     * @param userId   user
     * @param pageable page limit
     * @return result
     */
    Page<LoginLogEntity> select(Long userId, Pageable pageable);

    @Async
    void logout(Long userId);
}
