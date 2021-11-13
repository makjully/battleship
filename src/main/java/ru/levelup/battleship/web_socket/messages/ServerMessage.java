package ru.levelup.battleship.web_socket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ServerMessage {
    @JsonProperty
    private String userToMove;

    @JsonProperty
    private Target target;

    @JsonProperty
    private String result;
}