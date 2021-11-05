package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameRoomService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class GameRoomsController {

    public static final int PAGE_RESULTS = 9;

    private GameRoomService roomService;
    private UserService userService;

    @GetMapping({"/", "/app"})
    public RedirectView index() {
        return new RedirectView("app/rooms");
    }

    @GetMapping("app/rooms")
    public String returnGameRooms(Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        model.addAttribute("user", user);

        model.addAttribute("page", page);

        Page<GameRoom> rooms = roomService.findActualGameRooms(PageRequest.of(page - 1, PAGE_RESULTS));
        model.addAttribute("rooms", rooms);

        return "rooms";
    }
}