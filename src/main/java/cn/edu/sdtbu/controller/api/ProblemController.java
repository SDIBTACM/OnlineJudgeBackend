package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.ProblemDescEntity;
import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public ResponseEntity<ProblemDescEntity> getProblemDesc(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemDesc(id));
    }

}
