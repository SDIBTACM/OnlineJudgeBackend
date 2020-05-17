package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.UserCenterVO;
import cn.edu.sdtbu.model.vo.user.UserRankListVO;
import cn.edu.sdtbu.service.ProblemService;
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
import java.util.Set;

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
    ServletContext context;

    @GetMapping("/onlinePeople")
    public ResponseEntity<Integer> onlinePeopleCount() {
        return ResponseEntity.ok(((Set) context.getAttribute(Const.SESSION_SET)).size());
    }

    @PutMapping
    public ResponseEntity<Void> register(@RequestBody @Validated(UserParam.Resister.class) UserParam registerAo) {
        log.debug("registered: {}", registerAo.toString());
        userService.addUser(registerAo);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/center")
    public ResponseEntity<UserCenterVO> userCenter(String username,
                                                   @ApiIgnore HttpSession session) {
        UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        UserEntity userEntity = userService.getByUsername(username);
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
}
