package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.ProblemDescVO;
import cn.edu.sdtbu.model.vo.contest.ContestDetailVO;
import cn.edu.sdtbu.model.vo.contest.ContestsVO;
import cn.edu.sdtbu.service.ContestService;
import cn.edu.sdtbu.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 15:14
 */
@RestController
@RequestMapping("/api/contest")
public class ContestController {
    @Resource
    ContestService service;

    @GetMapping("/all")
    public ResponseEntity<Page<ContestsVO>> listContestByPage(
        @PageableDefault(size = 15, direction = Sort.Direction.ASC, sort = "startAt") Pageable page,
        @ApiIgnore HttpSession session) {
        //TODO just fetch allowed
        UserEntity userEntity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        return ResponseEntity.ok(service.fetchContests(userEntity, page));
    }


    @GetMapping("/{id}")
    @ApiOperation("获取contest详情")
    public ResponseEntity<ContestDetailVO> getContestDetailById(@PathVariable("id") Long contestId,
                                                                @ApiIgnore HttpSession session) {
        UserEntity userEntity = RequestUtil.fetchUserEntityFromSession(true, session);
        return ResponseEntity.ok(service.fetchDetailContestInfo(contestId, userEntity == null ? null : userEntity.getId()));
    }

    @GetMapping("/problem")
    @ApiOperation("获取contest内problem详细信息")
    @SourceSecurity(SecurityType.STUDENT_OR_LOGIN)
    public ResponseEntity<ProblemDescVO> getProblemDesc(Long contestId,
                                                        Integer order,
                                                        @ApiIgnore HttpSession session) {
        UserEntity userEntity = RequestUtil.fetchUserEntityFromSession(false, session);
        ProblemDescVO vo = service.getContestProblemDesc(contestId, order, userEntity == null ? null : userEntity.getId());
        return ResponseEntity.ok(vo);
    }
}
