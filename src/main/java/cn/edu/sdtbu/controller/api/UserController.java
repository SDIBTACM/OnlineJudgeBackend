package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.ao.UserAO;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.vo.ResultVO;
import cn.edu.sdtbu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 16:06
 */

@RestController
public class UserController {
    @Resource
    UserService userService;


    @PostMapping("/user/{userId}")
    public ResultVO updateUserById(UserEntity userEntity,
                               @PathVariable int userId) {
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ResultVO.success();
    }

    @PutMapping("/user")
    public ResultVO addUser(UserAO userAO) {
        userService.addUser(userAO);
        return ResultVO.success();
    }

    @GetMapping("/user/{userId}")
    public UserEntity queryUserById(@PathVariable int userId) {
        return userService.queryUserById(userId);
    }

}
