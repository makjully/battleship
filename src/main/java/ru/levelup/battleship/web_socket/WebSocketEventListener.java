package ru.levelup.battleship.web_socket;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import ru.levelup.battleship.web_socket.messages.JoinMessage;

import java.util.Objects;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String login = Objects.requireNonNull(headers.getUser()).getName();
        long roomId = Long.parseLong(Objects.requireNonNull(headers.getFirstNativeHeader("room")));

        messagingTemplate.convertAndSend("/room/" + roomId, new JoinMessage(login, true));
    }
}