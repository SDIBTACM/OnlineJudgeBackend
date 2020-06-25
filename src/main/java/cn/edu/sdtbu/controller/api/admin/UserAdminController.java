package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.model.constant.ExceptionConstant;
import cn.edu.sdtbu.model.constant.WebContextConstant;
import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.user.UserParam;
import cn.edu.sdtbu.model.vo.user.UserOnlineInfoVO;
import cn.edu.sdtbu.model.vo.user.UserSimpleInfoVO;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestUtil;
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
import javax.servlet.http.HttpServletRequest;
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
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAdminController {
    @Resource
    UserService     userService;
    @Resource
    LoginLogService logService;
    @Resource
    ServletContext  context;

    @PostMapping("/lockUser")
    @SuppressWarnings("unchecked")
    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    public ResponseEntity<Void> lockUser(@RequestBody Long userId, HttpServletRequest request) {
        UserEntity admin = (UserEntity) request.getSession().getAttribute(WebContextConstant.USER_SESSION_INFO);
        if (admin == null || admin.getRole() != UserRole.ADMIN) {
            log.warn("some user try across the permissions, id is {}", RequestUtil.getClientIp(request));
            throw ExceptionConstant.UNAUTHORIZED;
        }
        userService.lockUser(userId);
        Set<HttpSession> sessions = (Set<HttpSession>) context.getAttribute(WebContextConstant.SESSION_SET);
        sessions.forEach(session -> {
            UserEntity entity = (UserEntity) session.getAttribute(WebContextConstant.USER_SESSION_INFO);
            if (entity != null && entity.getId().equals(userId)) {
                session.invalidate();
            }
        });
        return ResponseEntity.ok(null);
    }


    @GetMapping("/role")
    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    public ResponseEntity<Page<UserSimpleInfoVO>> listUsersByRole(UserRole role,
                                                                  @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.listUserByRole(role, pageable));
    }

    @GetMapping("/onlinePeopleList")
    @SuppressWarnings("unchecked")
    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    public ResponseEntity<List<UserOnlineInfoVO>> onlinePeople() {
        List<UserOnlineInfoVO> onlineUsers = new LinkedList<>();
        Set<HttpSession>       sessions    = (Set<HttpSession>) context.getAttribute(WebContextConstant.SESSION_SET);
        sessions.forEach(item -> {
            UserEntity entity = (UserEntity) item.getAttribute(WebContextConstant.USER_SESSION_INFO);
            if (entity != null) {
                UserOnlineInfoVO onlineInfoVO = new UserOnlineInfoVO();
                onlineInfoVO.setId(entity.getId());
                onlineInfoVO.setIp(item.getAttribute(WebContextConstant.USER_IP).toString());
                onlineInfoVO.setLoginTime(new Timestamp(item.getCreationTime()));
                onlineInfoVO.setUsername(entity.getUsername());
                onlineInfoVO.setEmail(entity.getEmail());
                onlineInfoVO.setNickname(entity.getNickname());
                onlineUsers.add(onlineInfoVO);
            }
        });
        return ResponseEntity.ok(onlineUsers);
    }

    @GetMapping("/loginLog/{userId}")
    @SourceSecurity(value = SecurityType.AT_LEAST_TEACHER, throwException = false)
    public ResponseEntity<Page<LoginLogEntity>> loginLog(@PathVariable Long userId,
                                                         @PageableDefault(sort = "updateAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(logService.select(userId, pageable));
    }

    @PostMapping("/{userId}")
    @SourceSecurity(SecurityType.STUDENT_OR_LOGIN)
    public ResponseEntity<UserEntity> updateUserById(
        @RequestBody @Validated(UserParam.Update.class) UserParam userParam,
        @PathVariable Long userId,
        @ApiIgnore HttpSession session) {
        UserEntity entity = RequestUtil.fetchUserEntityFromSession(false, session);
        if (!entity.getId().equals(userId)) {
            throw ExceptionConstant.NO_PERMISSION;
        }
        UserEntity userEntity = userParam.transformToEntity();
        userEntity.setId(userId);
        userEntity = userService.update(userEntity, userId);
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/{userId}")
    @SourceSecurity(SecurityType.AT_LEAST_ADMIN)
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }
}
