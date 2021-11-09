package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.ShipService;
import ru.levelup.battleship.services.UserService;

@Controller
@AllArgsConstructor
public class GameController {

    private UserService userService;
    private GameService gameService;
    private RoomService roomService;
    private ShipService shipService;

    @PostMapping("app/game/ready")
    public RedirectView readyToPlay(@RequestParam("room_id") Long roomId,
                                    Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        userService.updateWhenBoardPrepared(user);

        return new RedirectView("/app/main/" + roomId);
    }

    @PostMapping("app/game/start")
    public RedirectView startGame(@RequestParam("room_id") Long roomId) {
        Room room = roomService.findById(roomId);

        User user_1 = room.getInviter();
        User user_2 = room.getAccepting();

        Game game = gameService.createGame(user_1, user_2);
        room.setGame(game);
        roomService.updateRoom(room);

        return new RedirectView("/app/main/" + roomId);
    }

    @PostMapping("app/game/exit")
    public RedirectView exitGame(@RequestParam("room_id") Long room_id) {
        Room room = roomService.findById(room_id);

        if (room.getGame() != null) {
            Game game = gameService.findGameById(room.getGame().getId());
            gameService.deleteUnfinishedGame(game);
        }

        User player_1 = userService.findByLogin(room.getInviter().getLogin());
        removeGameData(player_1);

        if (room.getAccepting() != null) {
            User player_2 = userService.findByLogin(room.getAccepting().getLogin());
            removeGameData(player_2);
        }

        roomService.deleteGameRoom(room);

        return new RedirectView("/app/rooms");
    }

    private void removeGameData(User user) {
        if (shipService.countShipsByPlayer(user) > 0)
            shipService.deleteAll(user);

        user.setPlayerFieldArranged(false);
        userService.update(user);
    }
}