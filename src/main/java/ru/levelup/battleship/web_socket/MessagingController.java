package ru.levelup.battleship.web_socket;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;
import ru.levelup.battleship.services.BattleService;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.UserService;
import ru.levelup.battleship.web_socket.model.HitMessage;
import ru.levelup.battleship.web_socket.model.ReadyMessage;
import ru.levelup.battleship.web_socket.model.ServerMessage;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class MessagingController {

    private RoomService roomService;
    private BattleService battleService;
    private UserService userService;
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;
    private static final double POINTS = 10.5;

    @MessageMapping("/hit/{id}")
    public void getMessages(@PathVariable("id") Long roomId,
                            @Payload HitMessage hitMessage) {
        String toMove;
        Room room = roomService.findById(roomId);
        User user = userService.findByLogin(hitMessage.getLogin());
        User opponent = Objects.equals(user, room.getInviter()) ? room.getAccepting() : room.getInviter();

        Result result = battleService.hit(opponent, hitMessage.getTarget().getX(), hitMessage.getTarget().getY());

        if (result.equals(Result.WIN)) {
            gameService.endGame(room.getGame(), true, user);
            userService.updateRating(user, POINTS);
            toMove = user.getLogin();
        } else {
            toMove = result.equals(Result.MISS) ? opponent.getLogin() : user.getLogin();
        }
        ServerMessage response = new ServerMessage(toMove, hitMessage.getTarget(), result.name());

        messagingTemplate.convertAndSend("/room/" + roomId, response);
    }

    @MessageMapping("/ready/{id}")
    public void setReadyStatus(@PathVariable("id") Long roomId,
                               @Payload ReadyMessage readyMessage) {
        User user = userService.findByLogin(readyMessage.getLogin());
        userService.updateWhenBoardPrepared(user);

        messagingTemplate.convertAndSend("/room/" + roomId, readyMessage);
    }

    @MessageMapping("/start/{id}")
    public void setReadyStatus(@PathVariable("id") Long roomId) {
        Room room = roomService.findById(roomId);

        User user_1 = room.getInviter();
        User user_2 = room.getAccepting();

        Game game = gameService.createGame(user_1, user_2);
        room.setGame(game);
        roomService.updateRoom(room);

        messagingTemplate.convertAndSend("/room/" + roomId,
                new ServerMessage(game.getPlayerToMove().getLogin(), null, null));
    }
}