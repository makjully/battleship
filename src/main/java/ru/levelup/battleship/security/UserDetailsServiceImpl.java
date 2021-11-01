package ru.levelup.battleship.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.UserService;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userService.findByLogin(username);

        if (found == null)
            throw new UsernameNotFoundException("User " + username + " not found");

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(found.getPassword())
                .roles("user")
                .build();
    }
}