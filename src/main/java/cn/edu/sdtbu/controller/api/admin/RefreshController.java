package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-20 18:45
 */

@RestController
@RequestMapping("/api/admin/refresh")
public class RefreshController {
    @Resource
    ProblemService problemService;

    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    @GetMapping("/problemSolutionCount")
    public ResponseEntity<Void> refreshProblemSolutionCount(@RequestParam(required = false) Long problemId) {
        problemService.refreshSolutionCount(problemId);
        return ResponseEntity.ok(null);
    }
}
