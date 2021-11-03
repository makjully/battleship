package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameRoomService;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
@RequestMapping("app/main")
public class MainPageController {

    private UserService userService;
    private GameService gameService;
    private GameRoomService roomService;
    private static final String MAIN_PAGE = "main";

    @GetMapping("/{room_id}")
    public String showMain(Model model,
                           @PathVariable("room_id") Long roomId,
                           Authentication authentication) {

        return MAIN_PAGE;
    }

    @PostMapping("/join")
    public String joinRoom(@RequestParam("id") Long roomId,
                           Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        GameRoom room = roomService.findById(roomId);

        if (room != null && room.getAccepting() == null) {
            roomService.updateRoomWhenAccept(room, user);
        } else
            return "redirect:rooms";

        return "redirect:/" + roomId;
    }

    @PostMapping("/create")
    public String createRoom(Model model,
                             Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        GameRoom room = roomService.createRoom(user);
        model.addAttribute("user", user);
        model.addAttribute("room", room);

        return "redirect:/" + room.getId();
    }

    @PostMapping("/startGame")
    public String startGame(Model model,
                            @ModelAttribute("room") GameRoom room) {
        User user_1 = room.getInviter();
        User user_2 = room.getInviter();

        Game game = gameService.createGame(user_1, user_2);
        model.addAttribute("game", game);

        return MAIN_PAGE;
    }
}