package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.ProblemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 17:08
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/problem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProblemController {
    @GetMapping("/offset")
    public ResponseEntity<Page<ProblemEntity>> lists(Pageable pageable) {
        return null;
    }
}
