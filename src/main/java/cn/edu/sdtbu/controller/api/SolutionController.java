package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.SubmitCodeParam;
import cn.edu.sdtbu.model.query.SolutionQuery;
import cn.edu.sdtbu.model.vo.SolutionListNode;
import cn.edu.sdtbu.model.vo.TokenVO;
import cn.edu.sdtbu.service.SolutionService;
import cn.edu.sdtbu.util.RequestUtil;
import cn.edu.sdtbu.util.SpringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 09:37
 */
@RestController
@RequestMapping("/api/solution")
public class SolutionController {
    @Resource
    SolutionService solutionService;
    @Resource
    private CacheHandler handler;

    @PutMapping("/submit")
    public ResponseEntity<TokenVO> submitCode(@RequestBody SubmitCodeParam param) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<Page<SolutionListNode>> listSubmitByPage(SolutionQuery query,
                                                                   @PageableDefault(size = 30) Pageable pageable,
                                                                   @ApiIgnore HttpSession session) {
        UserEntity     userEntity  = RequestUtil.fetchUserEntityFromSession(true, session);
        SolutionEntity queryEntity = new SolutionEntity();
        SpringUtil.cloneWithoutNullVal(query, queryEntity);
        return ResponseEntity.ok(solutionService
            .listSubmit(queryEntity, userEntity == null ? UserRole.STUDENT : userEntity.getRole(), pageable));
    }


    @GetMapping("/status")
    public ResponseEntity<JudgeResult> checkJudgeStatus(String token) {
        //TODO current limiting( up to N visits per unit time )
        try {
            return ResponseEntity.ok(
                JudgeResult.valueOf(
                    handler.fetchCacheStore().get(token)
                ));
        } catch (IllegalArgumentException ignore) {
            return ResponseEntity.ok(JudgeResult.PENDING);
        }
    }
}
