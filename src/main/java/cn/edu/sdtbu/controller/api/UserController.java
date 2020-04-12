package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.LoginLogEntity;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    public Page<LoginLogEntity> loginLog(@PathVariable Long userId,
                                         @PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
                                         @ApiIgnore HttpSession session) {
        //TODO authentication
        return logService.select(userId, pageable);
    }
    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUserById(
            @RequestBody UserEntity userEntity,
            @PathVariable Long userId) {
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ResponseEntity.ok("success");
    }

    @PutMapping
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterParam registerAo) {
        log.debug("registered: {}", registerAo.toString());
        userService.addUser(registerAo);
        return ResponseEntity.ok("registered");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.queryUserById(userId));
    }
}
