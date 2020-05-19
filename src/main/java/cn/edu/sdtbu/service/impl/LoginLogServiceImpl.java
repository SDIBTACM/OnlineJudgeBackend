package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.repository.user.LoginLogRepository;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 10:46
 */
@Service
public class LoginLogServiceImpl extends AbstractBaseService<LoginLogEntity, Long> implements LoginLogService {

    final LoginLogRepository repository;

    public LoginLogServiceImpl(LoginLogRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void login(Long userId, String ip) {
        LoginLogEntity entity = new LoginLogEntity();
        entity.setIp(ip);
        entity.setUserId(userId);
        save(entity);
    }

    @Override
    public Page<LoginLogEntity> select(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }
}
