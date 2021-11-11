package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.UserService;

import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class MainPageController {

    private UserService userService;
    private RoomService roomService;

    @GetMapping("app/main/{room_id}")
    public String showMain(Model model,
                           @PathVariable("room_id") Long roomId,
                           Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        model.addAttribute("user", user);

        Room room = roomService.findById(roomId).orElseThrow(NoSuchElementException::new);
        model.addAttribute("room", room);

        return "main";
    }

    @PostMapping("app/main/join")
    public RedirectView joinRoom(@RequestParam("id") Long roomId,
                                 Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        Room room = roomService.findById(roomId).orElseThrow(NoSuchElementException::new);

        if (room != null && room.getAccepting() == null) {
            roomService.updateRoomWhenAccept(room, user);
        } else
            return new RedirectView("/app/rooms");

        return new RedirectView("/app/main/" + room.getId());
    }

    @PostMapping("app/main/create")
    public RedirectView createRoom(Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        Room room = roomService.createRoom(user);

        return new RedirectView("/app/main/" + room.getId());
    }
}