package com.sujya.prj;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PrjApplicationTests {
    @Autowired
    private MockMvc mvc;

    private static String token;

    private static String username = "sj";
    private static String password = "sj";


    @Before
    public void beforeTest() throws Exception{
        // 1. login and get jwt token
        String body = "{ \"username\" : \"" + username + "\", \"password\": \"" + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .content(body)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        String[] temp = response.split(":");
        String tempToken = temp[1].replace("\"", "");
        token = tempToken.replace("}", "");

    }


    @Test
    public void testRefreshSuccess() throws Exception {


        mvc.perform(MockMvcRequestBuilders.post("/auth/token")
        .header("Authorization", "Bearer " + token))
                .andDo(print());
    }




}
