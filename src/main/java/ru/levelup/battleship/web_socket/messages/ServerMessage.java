package ru.levelup.battleship.web_socket.messages;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerMessage {
    private String userToMove;
    private Target target;
    private String result;
}