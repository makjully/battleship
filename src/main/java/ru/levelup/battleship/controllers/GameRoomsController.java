package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameRoomService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class GameRoomsController {

    public static final int PAGE_RESULTS = 10;

    private GameRoomService roomService;
    private UserService userService;

    @GetMapping("app/rooms")
    public String returnGameRooms(Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "") String opponentLogin) {
        User opponent = userService.findByLogin(opponentLogin);
        model.addAttribute("opponent", opponent);

        model.addAttribute("page", page);

        Page<GameRoom> rooms = roomService.findActualGameRooms(PageRequest.of(page - 1, PAGE_RESULTS));
        model.addAttribute("rooms", rooms);

        return "rooms";
    }
}