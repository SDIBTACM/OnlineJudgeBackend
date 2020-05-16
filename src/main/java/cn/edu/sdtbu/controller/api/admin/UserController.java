package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.UserOnlineInfoVO;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 16:21
 */

@Slf4j
@RestController("admin-user-controller")
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Resource
    UserService userService;
    @Resource
    LoginLogService logService;
    @Resource
    ServletContext context;


    @GetMapping("/onlinePeopleList")
    public ResponseEntity<List<UserOnlineInfoVO>> onlinePeople() {
        List<UserOnlineInfoVO> onlineUsers = new LinkedList<>();
        Set<HttpSession> sessions = (Set<HttpSession>) context.getAttribute(Const.SESSION_SET);
        sessions.forEach(item -> {
            UserEntity entity = (UserEntity) item.getAttribute(Const.USER_SESSION_INFO);
            if (entity != null) {
                UserOnlineInfoVO onlineInfoVO = new UserOnlineInfoVO();
                onlineInfoVO.setId(entity.getId());
                onlineInfoVO.setIp(item.getAttribute(Const.USER_IP).toString());
                onlineInfoVO.setLoginTime(new Timestamp(item.getCreationTime()));
                onlineInfoVO.setUsername(entity.getUsername());
                onlineInfoVO.setNickname(entity.getNickname());
                onlineUsers.add(onlineInfoVO);
            }
        });
        return ResponseEntity.ok(onlineUsers);
    }

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
