package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class MainPageController {

    private UserService userService;
    private GameService gameService;

    @GetMapping("/app/main")
    public String getMainPage(Model model,
                              @ModelAttribute("room") GameRoom gameRoom,
                              Authentication authentication) {
        User me = userService.findByLogin(authentication.getName());
        model.addAttribute("me", me);

        return "main";
    }

}