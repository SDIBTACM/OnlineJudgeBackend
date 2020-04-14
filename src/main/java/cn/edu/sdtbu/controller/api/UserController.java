package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.model.vo.ResponseVO;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseVO loginLog(@PathVariable Long userId,
                                         @PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
                                         @ApiIgnore HttpSession session) {
        //TODO authentication
        return ResponseVO.builder()
                .data(logService.select(userId, pageable))
                .code(HttpStatus.OK)
                .build();
    }
    @PostMapping("/{userId}")
    public ResponseVO updateUserById(
            @RequestBody UserEntity userEntity,
            @PathVariable Long userId) {
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ResponseVO.ok();
    }

    @PutMapping
    public ResponseVO register(@RequestBody @Valid UserRegisterParam registerAo) {
        log.debug("registered: {}", registerAo.toString());
        userService.addUser(registerAo);
        return ResponseVO.ok();
    }

    @GetMapping("/{userId}")
    public ResponseVO queryUserById(@PathVariable Long userId) {
        return ResponseVO.builder()
                .code(HttpStatus.OK)
                .data(userService.queryUserById(userId))
                .build();
    }
}
