package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.*;

@Service
@AllArgsConstructor
public class RemoveDataServiceImpl implements RemoveDataService {

    private RoomService roomService;
    private UserService userService;
    private ShipService shipService;
    private GameService gameService;

    @Override
    public void removeDataAfterExit(Room room) {
        Game game = room.getGame();

        if (game != null && !game.isCompleted()) {
            game = gameService.findGameById(room.getGame().getId());
            room.setGame(null);
            roomService.updateRoom(room);
            gameService.deleteUnfinishedGame(game);
        }

        User player_1 = userService.findByLogin(room.getInviter().getLogin());
        removeGameData(player_1);

        if (room.getAccepting() != null) {
            User player_2 = userService.findByLogin(room.getAccepting().getLogin());
            removeGameData(player_2);
        }

        roomService.deleteRoom(room);
    }

    private void removeGameData(User user) {
        if (shipService.countShipsByPlayer(user) > 0)
            shipService.deleteAll(user);

        user.setPlayerFieldArranged(false);
        userService.update(user);
    }
}