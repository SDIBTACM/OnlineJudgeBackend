package controller;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.constant.KeyPrefixConstant;
import cn.edu.sdtbu.model.param.user.LoginParam;
import cn.edu.sdtbu.model.param.user.UserParam;
import cn.edu.sdtbu.model.vo.user.UserLoginInfoVO;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.CacheUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 17:14
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AuthTest {
    @Resource
    public WebApplicationContext applicationContext;
    @Resource
    UserService userService;
    public MockMvc mvc;
    @Resource
    CacheHandler handler;
    @Before
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Test
    public void registerTest() throws Exception {
        UserParam userRegisterParam = new UserParam();
        userRegisterParam.setEmail("emaidTest@email");
        userRegisterParam.setNickname("nicknameTest");
        userRegisterParam.setPassword("password1");
        userRegisterParam.setUsername("usernameTest");
        userRegisterParam.setSchool("school");
        handler.fetchCacheStore().delete(
            CacheUtil.defaultKey(String.class, userRegisterParam.getEmail(), KeyPrefixConstant.REGISTERED_EMAIL));
        handler.fetchCacheStore().delete(
            CacheUtil.defaultKey(String.class, userRegisterParam.getUsername(), KeyPrefixConstant.REGISTERED_USERNAME));

        MvcResult result = mvc.perform(
                // build request
                MockMvcRequestBuilders.put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(userRegisterParam)))
                // print debug info to console
                .andDo(MockMvcResultHandlers.print())
                // assert http status is 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                // get result
                .andReturn();
        String password = userRegisterParam.getPassword();
        userService.addUser(userRegisterParam);
        userRegisterParam.setPassword(password);
        loginTest(userRegisterParam);
    }
    void loginTest(UserParam param) throws Exception {
        LoginParam loginParam = new LoginParam();
        loginParam.setIdentify(param.getUsername());
        loginParam.setPassword(param.getPassword());
        loginParam.setRemember(false);
        MvcResult result = mvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(loginParam)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        UserLoginInfoVO loginInfo = JSON.parseObject(result.getResponse().getContentAsString(), UserLoginInfoVO.class);
        assert loginInfo.getUsername().equals(param.getUsername());
    }
}
