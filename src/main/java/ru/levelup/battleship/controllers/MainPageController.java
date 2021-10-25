package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class MainPageController {

    private UserService userService;
    private GameService gameService;

    @GetMapping({"/", "/app"})
    public RedirectView index() {
        return new RedirectView("app/main");
    }

    @GetMapping("/app/main")
    public String getMainPage(Model model) {
        return "main";
    }

}
