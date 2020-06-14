package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.repository.user.LoginLogRepository;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.TimeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Page<LoginLogEntity> page = repository.findAllByUserId(userId, pageable);
        List<LoginLogEntity> loginLogEntities = page.getContent().stream().filter(
            i -> !i.getCreateAt().equals(i.getLogoutTime())
        ).collect(Collectors.toList());
        return new PageImpl<>(loginLogEntities, pageable, page.getTotalElements());
    }

    @Override
    public void logout(Long userId) {
        LoginLogEntity entity = repository.findFirstByUserIdOrderByCreateAtDesc(userId);
        if (entity != null) {
            entity.setLogoutTime(TimeUtil.now());
            save(entity);
        }
    }
}
