package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameRoomService;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class MainPageController {

    private UserService userService;
    private GameService gameService;
    private GameRoomService roomService;
    private static final String MAIN_PAGE = "main";

    @GetMapping("app/main/{room_id}")
    public String showMain(Model model,
                           @PathVariable("room_id") Long roomId,
                           Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        model.addAttribute("user", user);

        GameRoom room = roomService.findById(roomId);
        model.addAttribute("room", room);

        return MAIN_PAGE;
    }

    @PostMapping("app/main/join")
    public RedirectView joinRoom(@RequestParam("id") Long roomId,
                                 Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        GameRoom room = roomService.findById(roomId);

        if (room != null && room.getAccepting() == null) {
            roomService.updateRoomWhenAccept(room, user);
        } else
            return new RedirectView("/app/rooms");

        return new RedirectView("/app/main/" + room.getId());
    }

    @PostMapping("app/main/create")
    public RedirectView createRoom(Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        GameRoom room = roomService.createRoom(user);

        return new RedirectView("/app/main/" + room.getId());
    }

    @PostMapping("app/game/ready")
    public RedirectView readyToPlay(@RequestParam("id") Long roomId,
                                    Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        userService.updateWhenBoardPrepared(user);

        return new RedirectView("/app/main/" + roomId);
    }

    @PostMapping("app/game/start")
    public RedirectView startGame(@RequestParam("id") Long roomId) {
        GameRoom room = roomService.findById(roomId);

        User user_1 = room.getInviter();
        User user_2 = room.getAccepting();

        Game game = gameService.createGame(user_1, user_2);

        return new RedirectView("/app/main/" + roomId);
    }
}