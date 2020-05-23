package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.user.ClassEntity;
import cn.edu.sdtbu.model.entity.user.UserClassEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.user.UserClassParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import cn.edu.sdtbu.model.vo.user.UserClassesVO;
import cn.edu.sdtbu.repository.user.ClassRepository;
import cn.edu.sdtbu.repository.user.UserClassRepository;
import cn.edu.sdtbu.service.ClassService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.SpringUtil;
import cn.edu.sdtbu.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-21 14:49
 */
@Slf4j
@Service
public class ClassServiceImpl extends AbstractBaseService<ClassEntity, Long> implements ClassService {

    @Override
    public UserClassesVO createClass(UserClassParam param, UserEntity manager) {
        ClassEntity entity = new ClassEntity();
        UserClassesVO vo = new UserClassesVO();
        vo.setUsersInfo(new LinkedList<>());

        entity.setName(param.getName());
        vo.setName(param.getName());

        entity.setOwnerId(manager.getId());
        entity = save(entity);
        vo.setId(entity.getId());

        Set<UserClassEntity> userClassEntities = new HashSet<>();
        if (CollectionUtils.isNotEmpty(param.getUsernames())) {
            List<UserEntity> userEntities = userService.getAllByUsername(param.getUsernames());
            final ClassEntity finalEntity = entity;
            userEntities.forEach(i -> {
                UserClassEntity obj = new UserClassEntity();
                obj.setClassId(finalEntity.getId());
                obj.setUserId(i.getId());
                obj.setDeleteAt(Const.TIME_ZERO);
                vo.getUsersInfo().add(new BaseUserVO(i.getId(), i.getUsername(), i.getNickname()));
                userClassEntities.add(obj);
            });
        }
        userClassRepository.saveAll(userClassEntities);

        return vo;
    }

    @Override
    public List<UserClassesVO> fetchAllByManagerId(Long managerId) {
        // classes
        List<ClassEntity> list = classRepository.findAllByOwnerIdAndDeleteAt(managerId, Const.TIME_ZERO);

        // fetch classes' user
        List<UserClassEntity> classEntities = userClassRepository.findAllByClassIdIn(
            list.stream().map(ClassEntity::getId).collect(Collectors.toList())
        );

        // <UserId, UserEntity> map
        Map<Long, UserEntity> userEntities = userService.getByIds(
            classEntities.stream().map(UserClassEntity::getUserId).collect(Collectors.toSet()),
            Pageable.unpaged())
            .getContent().stream().collect(Collectors.toMap(UserEntity::getId, entity -> entity));
        // <ClassId, List<BaseUserVO>> map
        Map<Long, LinkedList<BaseUserVO>> baseUsers = new HashMap<>(list.size());
        classEntities.forEach(i -> {
            LinkedList<BaseUserVO> buffer = baseUsers.get(i.getClassId());
            if (buffer == null) {
                buffer = new LinkedList<>();
            }
            BaseUserVO vo = new BaseUserVO();
            SpringUtil.cloneWithoutNullVal(userEntities.get(i.getUserId()), vo);
            buffer.add(vo);
            baseUsers.put(i.getClassId(), buffer);
        });

        List<UserClassesVO> res = new LinkedList<>();
        list.forEach(i -> {
            UserClassesVO vo = new UserClassesVO();
            vo.setId(i.getId());
            vo.setName(i.getName());
            vo.setUsersInfo(baseUsers.get(i.getId()));
            res.add(vo);
        });
        return res;
    }

    @Override
    public void deleteClass(Collection<Long> classIds) {
        List<ClassEntity> classEntities = classRepository.findAllById(classIds);
        Timestamp now = TimeUtil.now();
        classEntities.forEach(i -> i.setDeleteAt(now));
        classRepository.saveAll(classEntities);
    }
    @Resource
    UserService userService;
    @Resource
    UserClassRepository userClassRepository;
    @Resource
    ClassRepository classRepository;
    protected ClassServiceImpl(ClassRepository repository) {
        super(repository);
    }
}
