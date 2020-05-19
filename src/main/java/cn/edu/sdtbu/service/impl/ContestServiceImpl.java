package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.contest.ContestEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.vo.contest.ContestDetailVO;
import cn.edu.sdtbu.model.vo.contest.ContestProblemVO;
import cn.edu.sdtbu.model.vo.contest.ContestsVO;
import cn.edu.sdtbu.repository.ContestRepository;
import cn.edu.sdtbu.service.ContestService;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.SpringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-18 07:36
 */
@Service
public class ContestServiceImpl extends AbstractBaseService<ContestEntity, Long> implements ContestService {
    @Override
    public Page<ContestsVO> fetchContests(UserEntity userEntity, Pageable page) {
        Page<ContestEntity> contestPage = listAll(page);
        List<ContestEntity> list = contestPage.getContent();
        List<ContestsVO> content = new LinkedList<>();
        list.forEach(item -> {
            ContestsVO vo = new ContestsVO();
            SpringUtil.cloneWithoutNullVal(item, vo);
            //TODO privilege
            vo.setAllowed(true);
            vo.setOwner(userService.getById(item.getOwnerId()).getUsername());
            content.add(vo);
        });
        return new PageImpl<>(content, page, contestPage.getTotalElements());
    }

    @Override
    public void fetchDetailContestInfo(Long contestId) {
        ContestDetailVO vo = new ContestDetailVO();
        SpringUtil.cloneWithoutNullVal(getById(contestId), vo);
        List<ContestProblemVO> problems = new LinkedList<>();
        // TODO finish
    }
    @Resource
    ProblemService problemService;
    @Resource
    UserService userService;
    protected ContestServiceImpl(ContestRepository repository) {
        super(repository);
    }
}
