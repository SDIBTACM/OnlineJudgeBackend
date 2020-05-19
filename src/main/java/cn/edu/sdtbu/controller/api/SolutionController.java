package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.param.SubmitCodeParam;
import cn.edu.sdtbu.model.vo.TokenVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 09:37
 */
@RestController
@RequestMapping("/api/solution")
public class SolutionController {
    @PutMapping("/submit")
    public ResponseEntity<TokenVO> submitCode(@RequestBody SubmitCodeParam param) {
        return ResponseEntity.ok(null);
    }
}
