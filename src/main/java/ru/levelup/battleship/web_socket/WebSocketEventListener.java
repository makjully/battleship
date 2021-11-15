package ru.levelup.battleship.web_socket;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.ShipService;
import ru.levelup.battleship.services.UserService;
import ru.levelup.battleship.web_socket.messages.ExitMessage;
import ru.levelup.battleship.web_socket.messages.JoinMessage;

import java.util.Objects;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private SimpMessagingTemplate messagingTemplate;
    private RoomService roomService;
    private UserService userService;
    private ShipService shipService;
    private GameService gameService;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String login = Objects.requireNonNull(headers.getUser()).getName();
        long roomId = Long.parseLong(Objects.requireNonNull(headers.getFirstNativeHeader("room")));

        messagingTemplate.convertAndSend("/room/" + roomId, new JoinMessage(login, true));
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String login = Objects.requireNonNull(headers.getUser()).getName();
        User user = userService.findByLogin(login);

        Room room = roomService.findRoomByUser(user);
        if (room != null) {
            Game game = room.getGame();
            if ((room.getInviter() != null && room.getAccepting() != null) && (game == null || !game.isCompleted()))
                messagingTemplate.convertAndSend("/room/" + room.getId(),
                        new ExitMessage(login, true));
            removeDataAfterExit(room);
        }
    }

    private void removeDataAfterExit(Room room) {
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

        roomService.deleteGameRoom(room);
    }

    private void removeGameData(User user) {
        if (shipService.countShipsByPlayer(user) > 0)
            shipService.deleteAll(user);

        user.setPlayerFieldArranged(false);
        userService.update(user);
    }
}