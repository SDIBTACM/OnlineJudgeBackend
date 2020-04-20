package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.ProblemDescEntity;
import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 17:08
 */

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProblemController {
    @Resource
    private ProblemService problemService;

    @GetMapping("/problems")
    public ResponseEntity<Page<ProblemEntity>> listProblems(@PageableDefault Pageable pageable) {
        return null;
    }

    @GetMapping("/problem/{id}")
    public ResponseEntity<ProblemDescEntity> getProblemDesc(@PathVariable Long id){
        return ResponseEntity.ok(problemService.getProblemDesc(id));
    }

    @PutMapping("/problem")
    public ResponseEntity<Void> putProblem(@Validated ProblemParam param){
        problemService.generatorProblem(param);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/problem/example")
    public ResponseEntity<ProblemParam> getDefaultProblemParam(){
        return ResponseEntity.ok(new ProblemParam());
    }
}
