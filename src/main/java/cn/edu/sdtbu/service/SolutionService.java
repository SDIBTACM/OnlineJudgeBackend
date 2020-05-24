package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.vo.SolutionListNode;
import cn.edu.sdtbu.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 20:29
 */
public interface SolutionService extends BaseService<SolutionEntity, Long> {
    Page<SolutionListNode> listSubmit(SolutionEntity query, UserRole role, Pageable pageable);
}
