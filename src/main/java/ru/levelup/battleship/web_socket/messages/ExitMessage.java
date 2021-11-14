package ru.levelup.battleship.web_socket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExitMessage {
    @JsonProperty
    private String login;

    @JsonProperty
    private boolean disconnected;
}