package ru.levelup.battleship.web_socket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReadyMessage {
   private String login;
   private boolean isReady;
}