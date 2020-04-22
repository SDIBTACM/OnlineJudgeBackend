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
    @PutMapping
    public ResponseEntity<Void> register(@RequestBody @Validated(UserParam.Resister.class) UserParam registerAo) {
        log.debug("registered: {}", registerAo.toString());
        userService.addUser(registerAo);
        return ResponseEntity.ok().build();
    }
}
