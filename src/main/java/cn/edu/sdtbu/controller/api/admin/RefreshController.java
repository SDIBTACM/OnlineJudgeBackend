package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.service.RefreshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    RefreshService refreshService;

    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    @PostMapping("/refreshRankList")
    public ResponseEntity<Void> refreshRankList(Boolean reloadCount) {
        refreshService.refreshRankList(reloadCount);
        return ResponseEntity.ok(null);
    }
}
