package controller;

import cn.edu.sdtbu.Application;
import cn.edu.sdtbu.model.param.UserRegisterParam;
import com.alibaba.fastjson.JSON;
import jdk.jfr.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.JUnit4;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
public class UserControllerTest {
    @Resource
    public WebApplicationContext applicationContext;

    public MockMvc mvc;

    @Before
    public void init() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Test
    public void registerTest() throws Exception {
        UserRegisterParam userRegisterParam = new UserRegisterParam();
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
                // assert is registered
                .andExpect(MockMvcResultMatchers.content().string("registered"))
                // get result
                .andReturn();
    }
}
