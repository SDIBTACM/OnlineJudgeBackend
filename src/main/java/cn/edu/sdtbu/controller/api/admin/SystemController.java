package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.service.RefreshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-20 16:53
 */
@RestController
@RequestMapping("/api/admin/sys")
public class SystemController {

    @Resource
    RefreshService refreshService;

    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    @PostMapping("/refreshRankList")
    public ResponseEntity<Void> refreshRankList(@RequestParam(defaultValue = "false") Boolean reloadCount,
                                                @RequestParam(defaultValue = "false") Boolean refreshUserEntities) {
        refreshService.refreshOverAllRankList(reloadCount, refreshUserEntities);
        return ResponseEntity.ok(null);
    }
}
