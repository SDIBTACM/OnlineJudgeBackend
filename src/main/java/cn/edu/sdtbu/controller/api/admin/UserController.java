package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 16:21
 */

@Slf4j
@RestController("admin-user-controller")
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    @Resource
    UserService userService;
    @Resource
    LoginLogService logService;

    @GetMapping("/loginLog/{userId}")
    public ResponseEntity<Page<LoginLogEntity>> loginLog(@PathVariable Long userId,
                                                         @PageableDefault(sort = "updateAt", direction = DESC) Pageable pageable,
                                                         @ApiIgnore HttpSession session) {
        return ResponseEntity.ok(logService.select(userId, pageable));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUserById(
        @RequestBody @Validated(UserParam.Update.class) UserParam userParam,
        @PathVariable Long userId) {
        UserEntity userEntity = userParam.transformToEntity();
        userEntity.setId(userId);
        userEntity = userService.update(userEntity, userId);
        return ResponseEntity.ok(userEntity);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }
}
