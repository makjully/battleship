package ru.levelup.battleship.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.levelup.battleship.TestMvcConfiguration;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.UserService;
import ru.levelup.battleship.validation.SignupForm;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMvcConfiguration.class)
public class SignupControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    public void signupDefaultTest() throws Exception {
        mvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attribute("signupForm", new SignupForm()));
    }

    @Test
    public void signupSuccessfulTest() throws Exception {
        User user = new User("user001", "encoded123");
        when(passwordEncoder.encode("123")).thenReturn("encoded123");
        when(service.createUser("user001", "encoded123")).thenReturn(user);
        Authentication token = new UsernamePasswordAuthenticationToken("user001", "123");
        when(authenticationProvider.supports(token.getClass())).thenReturn(true);
        when(authenticationProvider.authenticate(token)).thenReturn(token);

        SignupForm signupForm = new SignupForm();
        signupForm.setLogin("user001");
        signupForm.setPassword("123");
        signupForm.setRepeatPassword("123");
        mvc.perform(post("/signup")
                        .param("login", "user001")
                        .param("password", "123")
                        .param("repeatPassword", "123")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(service, times(1)).createUser("user001", "encoded123");
        verify(passwordEncoder, times(1)).encode("123");
    }

    @Test
    public void signupPasswordsUnmatched() throws Exception {
        SignupForm signupForm = new SignupForm();
        signupForm.setLogin("user001");
        signupForm.setPassword("123");
        signupForm.setRepeatPassword("124");
        mvc.perform(post("/signup")
                        .param("login", "user001")
                        .param("password", "123")
                        .param("repeatPassword", "124")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("signupForm", signupForm))
                .andExpect(model().attributeHasErrors("signupForm"));
        verifyNoInteractions(service);
    }
}