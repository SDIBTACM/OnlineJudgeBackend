package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.ContestParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.contest.ContestDetailVO;
import cn.edu.sdtbu.model.vo.contest.ContestsVO;
import cn.edu.sdtbu.service.ContestService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:28
 */

@Slf4j
@RestController("admin-contest-controller")
@RequestMapping(value = "/api/admin/contest")
public class ContestController {
    @Resource
    ContestService service;

    @GetMapping("/all")
    public ResponseEntity<Page<ContestsVO>> getContestById(
        @PageableDefault(size = 15, direction = Sort.Direction.ASC, sort = "startAt") Pageable page,
        @ApiParam("是否只显示自己能够访问的比赛") Boolean justAllowed,
        @ApiIgnore HttpSession session) {
        //TODO just fetch allowed
        UserEntity userEntity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        return ResponseEntity.ok(service.fetchContests(userEntity, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestDetailVO> getWithProblemList(@PathVariable("id") Long contestId) {
        service.fetchDetailContestInfo(contestId);
        return null;
    }

    @PutMapping
    public ResponseEntity<Void> addContest(ContestParam param) {
        return null;
    }
}
