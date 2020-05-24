package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.SolutionListNode;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.SolutionService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.SpringUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 20:29
 */
@Service
public class SolutionServiceImpl extends AbstractBaseService<SolutionEntity, Long> implements SolutionService {
    protected SolutionServiceImpl(SolutionRepository repository) {
        super(repository);
    }

    @Resource
    SolutionRepository solutionRepository;
    @Resource
    UserRepository userRepository;

    @Override
    public Page<SolutionListNode> listSubmit(SolutionEntity query, UserRole role, Pageable pageable) {
        Page<SolutionEntity> page = solutionRepository.findAll(Example.of(query), pageable);
        List<SolutionListNode> list = new LinkedList<>();

        Map<Long, UserEntity> idEntityMap = userRepository.findAllByIdInAndDeleteAt(
            page.getContent().stream().map(SolutionEntity::getOwnerId).collect(Collectors.toSet()),
            Const.TIME_ZERO)
            .stream().collect(Collectors.toMap(UserEntity::getId, u -> u));

        page.getContent().forEach(i -> {
            SolutionListNode node = new SolutionListNode();
            SpringUtil.cloneWithoutNullVal(i, node);
            if (role.getValue() < UserRole.TEACHER.getValue()) {
                node.setSimilarAt(null);
                node.setSimilarPercent(null);
            }
            node.setUserInfo(BaseUserVO.fetchBaseFromEntity(idEntityMap.get(i.getOwnerId())));
            list.add(node);
        });
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }
}
