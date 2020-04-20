package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.LoginLogEntity;
import cn.edu.sdtbu.model.entity.UserEntity;
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
 * register | update | list login log
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 16:06
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    @Resource
    UserService userService;
    @Resource
    LoginLogService logService;

    @GetMapping("/loginLog/{userId}")
    public ResponseEntity<Page<LoginLogEntity>> loginLog(@PathVariable Long userId,
                                                         @PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
                                                         @ApiIgnore HttpSession session) {
        //TODO authentication
        return ResponseEntity.ok(logService.select(userId, pageable));
    }
    @PostMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUserById(
            @RequestBody @Validated(UserParam.Update.class) UserParam userParam,
            @PathVariable Long userId) {

        UserEntity userEntity = userParam.transformToEntity();
        userEntity.setId(userId);
        return ResponseEntity.ok(userService.update(userEntity, userId));
    }

    @PutMapping
    public ResponseEntity<Void> register(@RequestBody @Validated(UserParam.Resister.class) UserParam registerAo) {
        log.debug("registered: {}", registerAo.toString());
        userService.addUser(registerAo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.fetchById(userId).orElseThrow(() ->
            new NotFoundException(
                String.format("user not found, id: [%d], please check it", userId)
            )));
    }
}
