package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.constant.WebContextConstant;
import cn.edu.sdtbu.model.entity.problem.ProblemDescEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.ProblemSubmitParam;
import cn.edu.sdtbu.model.vo.ProblemDescVO;
import cn.edu.sdtbu.model.vo.ProblemSimpleListVO;
import cn.edu.sdtbu.service.CountService;
import cn.edu.sdtbu.service.ProblemDescService;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.util.RequestUtil;
import cn.edu.sdtbu.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 17:08
 */

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProblemController {
    @Resource
    private ProblemService     problemService;
    @Resource
    private ProblemDescService descService;
    @Resource
    private CountService       countService;

    @GetMapping("/problems")
    public ResponseEntity<Page<ProblemSimpleListVO>> listProblems(@PageableDefault Pageable pageable,
                                                                  @ApiIgnore HttpSession session) {
        UserEntity userEntity = RequestUtil.fetchUserEntityFromSession(true, session);
        return ResponseEntity.ok(problemService.listSimpleLists(userEntity, pageable));
    }

    @GetMapping("/problem/{id}")
    public ResponseEntity<ProblemDescVO> getProblemDesc(@PathVariable Long id,
                                                        @ApiIgnore HttpSession session) {
        UserEntity        entity     = (UserEntity) session.getAttribute(WebContextConstant.USER_SESSION_INFO);
        ProblemDescEntity descEntity = descService.getById(id);
        ProblemDescVO     vo         = new ProblemDescVO();
        SpringUtil.cloneWithoutNullVal(descEntity, vo);
        vo = problemService.getProblemDescVoById(vo, id, null, entity == null ? null : entity.getId());
        return ResponseEntity.ok(vo);
    }


    @PostMapping("/problem/{id}/submit")
    public ResponseEntity<Void> submitProblem(@RequestBody ProblemSubmitParam param,
                                              @PathVariable Long id) {
        //TODO publish event to MQ

        return ResponseEntity.ok().build();
    }
}
