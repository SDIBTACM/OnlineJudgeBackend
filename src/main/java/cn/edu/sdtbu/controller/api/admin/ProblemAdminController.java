package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 16:19complete
 */

@Slf4j
@RestController("admin-problem-controller")
@RequestMapping(value = "/api/admin/problem", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProblemAdminController {
    @Resource
    private ProblemService problemService;

    @PutMapping
    public ResponseEntity<Void> putProblem(@RequestBody @Validated ProblemParam param) {
        problemService.generatorProblem(param);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/example")
    public ResponseEntity<ProblemParam> getDefaultProblemParam() {
        return ResponseEntity.ok(new ProblemParam());
    }
}
