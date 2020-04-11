package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.LoginLogEntity;
import cn.edu.sdtbu.repository.LoginLogRepository;
import cn.edu.sdtbu.service.LoginLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 10:46
 */
@Service
public class LoginLogServiceImpl implements LoginLogService {
    @Resource
    LoginLogRepository repository;

    @Override
    public void login(Long userId, String ip) {
        LoginLogEntity entity = new LoginLogEntity();
        entity.setIp(ip);
        entity.setUserId(userId);
        repository.saveAndFlush(entity);
    }

    @Override
    public Page<LoginLogEntity> select(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }
}
