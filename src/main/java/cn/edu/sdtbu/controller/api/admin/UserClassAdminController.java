package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.exception.UnauthorizedException;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.model.param.user.UserClassParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.UserClassesVO;
import cn.edu.sdtbu.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-21 11:53
 */
@RestController
@RequestMapping("/api/admin/class")
public class UserClassAdminController {
    @Resource
    ClassService classService;
    @PutMapping
    public ResponseEntity<UserClassesVO> createClass(@Validated(UserClassParam.Create.class)
                                                     @RequestBody(required = false) UserClassParam param,
                                                     @ApiIgnore HttpSession session) {
        UserEntity userEntity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        if (userEntity != null) {
            return ResponseEntity.ok(classService.createClass(param, userEntity));
        }
        else {
            throw new UnauthorizedException("log in plz");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserClassesVO>> fetchAllClassesByManagerId(@ApiIgnore HttpSession session) {
        UserEntity userEntity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        if (userEntity != null) {
            return ResponseEntity.ok(classService.fetchAllByManagerId(userEntity.getId()));
        }
        else {
            throw new UnauthorizedException("log in plz");
        }
    }
    @DeleteMapping
    @SourceSecurity(SecurityType.AT_LEAST_TEACHER)
    public ResponseEntity<Void> deleteByClassIds(@RequestBody Collection<Long> ids) {
        classService.deleteClass(ids);
        return ResponseEntity.ok(null);
    }
    @PutMapping("/append}")
    public ResponseEntity<Void> appendUser(Long appendedUserId,
                                           Long classIs) {
        return ResponseEntity.ok(null);
    }
}
