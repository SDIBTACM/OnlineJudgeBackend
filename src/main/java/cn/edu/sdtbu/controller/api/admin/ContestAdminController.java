package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.exception.UnauthorizedException;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.ContestParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.ContestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:28
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/admin/contest")
public class ContestAdminController {
    @Resource
    ContestService service;

    @PutMapping
    @SourceSecurity(SecurityType.AT_LEAST_TEACHER)
    public ResponseEntity<Void> addContest(@RequestBody @Validated(ContestParam.Create.class)
                                           ContestParam param,
                                           @ApiIgnore HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        if (entity.getRole().getValue() < UserRole.TEACHER.getValue()) {
            throw new UnauthorizedException();
        }
        service.createContest(param, entity);
        return ResponseEntity.ok(null);
    }
}
