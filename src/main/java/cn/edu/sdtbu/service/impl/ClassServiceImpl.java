package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.constant.ExceptionConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.entity.user.ClassEntity;
import cn.edu.sdtbu.model.entity.user.UserClassEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.user.UserClassParam;
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

    @Resource
    UserService         userService;
    @Resource
    UserClassRepository userClassRepository;
    @Resource
    ClassRepository     classRepository;

    protected ClassServiceImpl(ClassRepository repository) {
        super(repository);
    }

    @Override
    public UserClassesVO createClass(UserClassParam param, UserEntity manager) {
        ClassEntity   entity = new ClassEntity();
        UserClassesVO vo     = new UserClassesVO();
        vo.setUsersInfo(new LinkedList<>());

        entity.setName(param.getName());
        vo.setName(param.getName());

        entity.setOwnerId(manager.getId());
        entity = save(entity);
        vo.setId(entity.getId());

        Set<UserClassEntity> userClassEntities = new HashSet<>();
        if (CollectionUtils.isNotEmpty(param.getUsernames())) {
            List<UserEntity>  userEntities = userService.getAllByUsername(param.getUsernames());
            final ClassEntity finalEntity  = entity;
            userEntities.forEach(i -> {
                UserClassEntity obj = new UserClassEntity();
                obj.setClassId(finalEntity.getId());
                obj.setUserId(i.getId());
                obj.setDeleteAt(OnlineJudgeConstant.TIME_ZERO);
                vo.getUsersInfo().add(new BaseUserVO(i.getId(), i.getUsername(), i.getNickname()));
                userClassEntities.add(obj);
            });
        }
        userClassRepository.saveAll(userClassEntities);
        vo.setIsOwner(true);
        log.info("created class [{}] by [{}]", param.getName(), manager.getUsername());
        return vo;
    }

    @Override
    public List<UserClassListNode> fetchAllByManagerId(Long managerId) {
        // classes
        List<ClassEntity>       list               = classRepository.findAllByOwnerIdAndDeleteAt(managerId, OnlineJudgeConstant.TIME_ZERO);
        List<UserClassListNode> userClassListNodes = new LinkedList<>();
        UserClassEntity         exampleCase        = new UserClassEntity();
        exampleCase.setDeleteAt(OnlineJudgeConstant.TIME_ZERO);
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
        Timestamp         now           = TimeUtil.now();
        classEntities.forEach(i -> i.setDeleteAt(now));
        classRepository.saveAll(classEntities);
    }

    @Override
    public void appendUser(List<Long> userIds, Long classId, UserEntity userEntity) {
        isClassManager(classId, userEntity, true);
        Set<Long> existIds = userClassRepository.findAllByClassIdAndDeleteAt(classId, OnlineJudgeConstant.TIME_ZERO)
            .stream().map(UserClassEntity::getUserId).collect(Collectors.toSet());

        List<UserClassEntity> list = new LinkedList<>();
        userIds.stream().filter(i -> !existIds.contains(i)).collect(Collectors.toList()).forEach(i -> {
            UserClassEntity classEntity = new UserClassEntity();
            classEntity.setClassId(classId);
            classEntity.setUserId(i);
            list.add(classEntity);
        });
        userClassRepository.saveAll(list);
    }

    @Override
    public UserClassesVO fetchUsersByClassId(Long classId, UserEntity user) {
        Set<Long> users = userClassRepository.findAllByClassIdAndDeleteAt(classId, OnlineJudgeConstant.TIME_ZERO).stream()
            .map(UserClassEntity::getUserId).collect(Collectors.toSet());
        UserClassesVO vo = new UserClassesVO();
        vo.setIsOwner(isClassManager(classId, user, false));
        vo.setName(getById(classId).getName());
        vo.setId(classId);
        List<BaseUserVO> baseUsers = new LinkedList<>();
        userService.getByIds(users, Pageable.unpaged()).getContent()
            .forEach(i -> baseUsers.add(BaseUserVO.fetchBaseFromEntity(i)));
        vo.setUsersInfo(baseUsers);
        return vo;
    }

    @Override
    public void removeUser(List<Long> userIds, Long classId, UserEntity userEntity) {
        isClassManager(classId, userEntity, true);
        Timestamp             now   = TimeUtil.now();
        List<UserClassEntity> users = userClassRepository.findAllByClassIdAndDeleteAt(classId, OnlineJudgeConstant.TIME_ZERO);
        users.stream().filter(item -> userIds.contains(item.getUserId())).collect(Collectors.toList())
            .forEach(i -> i.setDeleteAt(now));
        userClassRepository.saveAll(users);
    }

    private boolean isClassManager(Long classId, UserEntity user, boolean throwException) {
        boolean res = user != null && (user.getRole() == UserRole.ADMIN || classId.equals(getById(classId).getOwnerId()));
        if (throwException && !res) {
            throw ExceptionConstant.UNAUTHORIZED;
        }
        return res;
    }
}
