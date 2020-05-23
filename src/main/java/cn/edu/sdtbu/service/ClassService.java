package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.user.ClassEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.user.UserClassParam;
import cn.edu.sdtbu.model.vo.user.UserClassesVO;
import cn.edu.sdtbu.service.base.BaseService;

import java.util.Collection;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-21 14:49
 */
public interface ClassService extends BaseService<ClassEntity, Long> {
    UserClassesVO createClass(UserClassParam param, UserEntity manager);

    List<UserClassesVO> fetchAllByManagerId(Long managerId);

    void deleteClass(Collection<Long> classIds);
}
