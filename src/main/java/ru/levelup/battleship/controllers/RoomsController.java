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
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class RoomsController {

    public static final int PAGE_RESULTS = 9;

    private RoomService roomService;
    private UserService userService;

    @GetMapping({"/", "/app"})
    public RedirectView index() {
        return new RedirectView("app/rooms");
    }

    @GetMapping("app/rooms")
    public String returnGameRooms(Model model,
                                  @RequestParam(defaultValue = "false") boolean inaccessible,
                                  @RequestParam(defaultValue = "1") int page,
                                  Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("page", page);
        model.addAttribute("inaccessible", inaccessible);

        Page<Room> rooms = roomService.findAvailableRooms(user, PageRequest.of(page - 1, PAGE_RESULTS));
        model.addAttribute("rooms", rooms);

        return "rooms";
    }
}