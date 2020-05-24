package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.user.ClassEntity;
import cn.edu.sdtbu.model.entity.user.UserClassEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.user.UserClassParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import cn.edu.sdtbu.model.vo.user.UserClassListNode;
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
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

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
    public List<UserClassListNode> fetchAllByManagerId(Long managerId) {
        // classes
        List<ClassEntity> list = classRepository.findAllByOwnerIdAndDeleteAt(managerId, Const.TIME_ZERO);
        List<UserClassListNode> userClassListNodes = new LinkedList<>();
        UserClassEntity exampleCase = new UserClassEntity();
        list.forEach(i -> {
            exampleCase.setClassId(i.getId());
            UserClassListNode buffer = new UserClassListNode();
            SpringUtil.cloneWithoutNullVal(i, buffer);
            buffer.setTotal(userClassRepository.count(Example.of(exampleCase)));
            userClassListNodes.add(buffer);
        });
        return userClassListNodes;
    }

    @Override
    public void deleteClass(Collection<Long> classIds) {
        List<ClassEntity> classEntities = classRepository.findAllById(classIds);
        Timestamp now = TimeUtil.now();
        classEntities.forEach(i -> i.setDeleteAt(now));
        classRepository.saveAll(classEntities);
    }

    @Override
    public void appendUser(List<Long> userIds, Long classId, UserEntity userEntity) {

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
