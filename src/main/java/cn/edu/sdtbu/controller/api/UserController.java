package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.ao.UserRegisterAO;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.service.UserService;
import com.google.common.primitives.UnsignedLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.*;
import java.util.Set;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 16:06
 */

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUserById(
            @RequestBody UserEntity userEntity,
            @PathVariable Long userId) {
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ResponseEntity.ok("success");
    }

    @PutMapping
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterAO registerAO) {
        log.debug("registered: {}", registerAO.toString());
        userService.addUser(registerAO);
        return ResponseEntity.ok("registered");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.queryUserById(userId));
    }
}
