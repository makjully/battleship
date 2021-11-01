package ru.levelup.battleship.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(Authentication authentication) {
        if (authentication != null)
            return "redirect: app/rooms";
        return "login";
    }
}