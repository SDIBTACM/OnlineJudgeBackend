package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 16:19
 */

@Slf4j
@RestController("admin-problem-controller")
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProblemController {
    @Resource
    private ProblemService problemService;

    @PutMapping("/problem")
    public ResponseEntity<Void> putProblem(@Validated ProblemParam param) {
        problemService.generatorProblem(param);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/problem/example")
    public ResponseEntity<ProblemParam> getDefaultProblemParam() {
        return ResponseEntity.ok(new ProblemParam());
    }
}
