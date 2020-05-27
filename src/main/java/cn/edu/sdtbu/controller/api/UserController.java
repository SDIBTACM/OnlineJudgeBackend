package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.manager.MailManager;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.model.param.user.ChangePasswordParam;
import cn.edu.sdtbu.model.param.user.UserParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.UserCenterVO;
import cn.edu.sdtbu.model.vo.user.UserRankListVO;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.CacheUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiParam;
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
import java.util.Set;
import java.util.UUID;

/**
 * register | update | list login log
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 16:06
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Resource
    UserService userService;
    @Resource
    ProblemService problemService;
    @Resource
    MailManager mailManager;
    @Resource
    CacheHandler handler;
    @Resource
    ServletContext context;

    @SourceSecurity(SecurityType.AT_LEAST_TEACHER)
    @GetMapping("/onlinePeople")
    public ResponseEntity<Integer> onlinePeopleCount() {
        return ResponseEntity.ok(((Set) context.getAttribute(Const.SESSION_SET)).size());
    }

    @PutMapping
    public ResponseEntity<String> register(@RequestBody @Validated(UserParam.Resister.class) UserParam registerAo) {
        userService.userMustNotExist(registerAo);
        String token = UUID.randomUUID().toString();
        String json = JSON.toJSONString(registerAo);
        log.debug("user register {}", json);
        handler.fetchCacheStore().put(
            CacheUtil.defaultKey(String.class, registerAo.getEmail(), KeyPrefix.REGISTERED_EMAIL), "true");
        handler.fetchCacheStore().put(
            CacheUtil.defaultKey(String.class, registerAo.getUsername(), KeyPrefix.REGISTERED_USERNAME), "true");

        handler.fetchCacheStore().put(
            CacheUtil.defaultKey(UserParam.class, token, KeyPrefix.REGISTER_USER), json);
        mailManager.sendSignUpMail(token, registerAo.getUsername(), registerAo.getEmail());
        return ResponseEntity.ok("激活邮件已发送至您的邮箱, 链接有效期30分钟. 请注意查收. 若未收到, 请查看是否被归类至垃圾邮件");
    }

    @GetMapping("/activate")
    public ResponseEntity<Void> activeRegisteredUser(String token) {
        String json;
        String key = CacheUtil.defaultKey(UserParam.class, token, KeyPrefix.REGISTER_USER);
        if ((json = handler.fetchCacheStore().get(key)) != null) {
            userService.addUser(JSON.parseObject(json, UserParam.class));
        } else {
            throw new NotFoundException("Not found such user, maybe he's been activated");
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/center")
    public ResponseEntity<UserCenterVO> userCenter(@ApiParam("用户唯一标识, id 或 username") @RequestParam String user,
                                                   @RequestParam(defaultValue = "true") Boolean isUserId,
                                                   @ApiIgnore HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        UserEntity userEntity = isUserId ? userService.getById(Long.parseLong(user)) : userService.getByUsername(user);
        UserCenterVO vo = userService.generatorUserCenterVO(
            problemService.fetchAllUserSubmitStatus(userEntity.getId()),
            userEntity.getId());
        vo.setIsOwner(entity != null && entity.getId().equals(userEntity.getId()));
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/rank")
    public ResponseEntity<Page<UserRankListVO>> rankList(@PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(userService.fetchRankList(pageable));
    }
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordParam param,
                                               @ApiIgnore HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        userService.changePassword(entity, param.getOldPassword(), param.getNewPassword());
        return ResponseEntity.ok(null);
    }
}
