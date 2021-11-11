package ru.levelup.battleship.web_socket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonDeserialize(as = JoinMessage.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JoinMessage {
    @JsonProperty
    private String login;

    @JsonProperty
    private boolean joined;
}