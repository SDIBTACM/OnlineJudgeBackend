package controller;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.param.LoginParam;
import cn.edu.sdtbu.model.param.UserParam;
import cn.edu.sdtbu.model.vo.UserLoginInfo;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class AuthTest {
    @Resource
    public WebApplicationContext applicationContext;

    public MockMvc mvc;

    @Before
    public void init() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Test
    public void registerTest() throws Exception {
        UserParam userRegisterParam = new UserParam();
        userRegisterParam.setEmail("email@email");
        userRegisterParam.setNickname("nickname");
        userRegisterParam.setPassword("password1");
        userRegisterParam.setUsername("username");
        userRegisterParam.setSchool("school");
        MvcResult result = mvc.perform(
                // build request
                MockMvcRequestBuilders.put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(userRegisterParam)))
                // print debug info to console
                .andDo(MockMvcResultHandlers.print())
                // assert http status is 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                // get result
                .andReturn();
        loginTest(userRegisterParam);
    }
    void loginTest(UserParam param) throws Exception {
        LoginParam loginParam = new LoginParam();
        loginParam.setIdentify(param.getUsername());
        loginParam.setPassword(param.getPassword());
        loginParam.setRemember(false);
        MvcResult result = mvc.perform(
            MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginParam)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
        UserLoginInfo loginInfo = JSON.parseObject(result.getResponse().getContentAsString(), UserLoginInfo.class);
        assert loginInfo.getUsername().equals(param.getUsername());
    }
}
