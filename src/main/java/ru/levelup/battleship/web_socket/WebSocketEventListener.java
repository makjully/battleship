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
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.web_socket.messages.ExitMessage;
import ru.levelup.battleship.web_socket.messages.JoinMessage;

import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private SimpMessagingTemplate messagingTemplate;
    private RoomService roomService;

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
        long roomId = Long.parseLong(Objects.requireNonNull(headers.getFirstNativeHeader("room")));

        Room room = roomService.findById(roomId).orElseThrow(NoSuchElementException::new);
        Game game = room.getGame();

        if ((room.getInviter() != null && room.getAccepting() != null) && (game == null || !game.isCompleted()))
            messagingTemplate.convertAndSend("/room/" + roomId, new ExitMessage(login, true));
    }
}