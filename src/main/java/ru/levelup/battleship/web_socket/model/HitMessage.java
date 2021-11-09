package ru.levelup.battleship.web_socket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class HitMessage {
    private String login;
    private Target target;
}