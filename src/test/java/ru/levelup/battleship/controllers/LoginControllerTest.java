package ru.levelup.battleship.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.levelup.battleship.TestMvcConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMvcConfiguration.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void LoginAlreadyLoggedInTest() throws Exception {
        mvc.perform(get("/login")
                        .with(user("user001"))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("app/rooms"));
    }

    @Test
    public void LoginDefaultTest() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}