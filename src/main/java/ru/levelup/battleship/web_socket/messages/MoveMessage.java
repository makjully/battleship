package ru.levelup.battleship.web_socket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonDeserialize(as = MoveMessage.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoveMessage {
    @JsonProperty
    private String login;

    @JsonProperty
    private Target target;
}