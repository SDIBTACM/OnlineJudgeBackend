package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.model.vo.contest.ContestDetailVO;
import cn.edu.sdtbu.model.vo.contest.ContestsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:28
 */

@Slf4j
@RestController("admin-contest-controller")
@RequestMapping(value = "/api/admin/contest")
public class ContestController {
    @GetMapping("/all")
    public ResponseEntity<ContestsVO> getContestById(@PageableDefault(size = 15)Pageable page) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestDetailVO> getWithProblemList(@PathVariable("id") Long contestId) {
        return null;
    }

    @PutMapping
    public ResponseEntity<Void> addContest() {
        return null;
    }
}
