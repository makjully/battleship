package ru.levelup.battleship.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.security.UserDetailsServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserDetailsServiceImpl.class)
public class UserServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    UserService userService;

    @Test
    public void existingUserTest() {
        ru.levelup.battleship.model.User user
                = new ru.levelup.battleship.model.User("user001", "encrypted123");
        when(userService.findByLogin("user001")).thenReturn(user);

        UserDetails expectedResult = User.withUsername("user001")
                .password("encrypted123")
                .roles("user")
                .build();
        UserDetails actualResult = userDetailsService.loadUserByUsername("user001");

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void nonExistingUserTest() {
        Assert.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("user001"));
    }
}