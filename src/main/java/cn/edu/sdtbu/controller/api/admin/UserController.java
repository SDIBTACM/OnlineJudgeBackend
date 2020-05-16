package cn.edu.sdtbu.controller.api.admin;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.entity.user.LoginLogEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.BannedLoginVO;
import cn.edu.sdtbu.model.vo.user.UserOnlineInfoVO;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.RequestIpUtil;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    @Resource
    CacheHandler cacheHandler;

    @PostMapping("/bannedLogin")
    public ResponseEntity<Void> banedUserLogin(String username, Long millisecond, HttpServletRequest request) {
        UserEntity admin = (UserEntity)request.getSession().getAttribute(Const.USER_SESSION_INFO);
        if (admin == null || admin.getRole() != UserRole.ADMIN) {
            log.warn("some user try across the permissions, id is {}", RequestIpUtil.getClientIp(request));
            throw new ForbiddenException("who you are?");
        }

        cacheHandler.fetchCacheStore().put(
            CacheUtil.defaultKey(String.class, username, KeyPrefix.BANED_USER),
            admin.getUsername(),
            millisecond,
            TimeUnit.MILLISECONDS
        );
        Set<HttpSession> sessions = (Set<HttpSession>) context.getAttribute(Const.SESSION_SET);
        sessions.forEach(session -> {
            UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
            if (entity != null && entity.getUsername().equals(username)) {
                session.invalidate();
            }
        });
        return ResponseEntity.ok(null);
    }

    @PostMapping("/unbanLogin")
    public ResponseEntity<Void> unbanLogin(String username) {
        cacheHandler.fetchCacheStore().delete(CacheUtil.defaultKey(String.class, username, KeyPrefix.BANED_USER));
        return ResponseEntity.ok(null);
    }

    @GetMapping("/bannedLogin")
    public ResponseEntity<List<BannedLoginVO>> listBannedLogin() {
        List<BannedLoginVO> list = new LinkedList<>();
        CacheStore<String, String> store = cacheHandler.fetchCacheStore();
        Map<String, String> map = store.fetchAll(KeyPrefix.BANED_USER.toString());
        map.forEach((first, second) -> {
            BannedLoginVO loginVO = new BannedLoginVO();
            loginVO.setBannedBy(second);
            loginVO.setEndTime(new Timestamp(store.ttl(first)));
            String[] buffer = first.split(CacheUtil.SEPARATOR);
            loginVO.setUsername(buffer[buffer.length - 1]);
            list.add(loginVO);
        });
        return ResponseEntity.ok(list);
    }

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
                onlineInfoVO.setEmail(entity.getEmail());
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
