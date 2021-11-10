package ru.levelup.battleship.web_socket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HitMessage {
    @JsonProperty
    private String login;

    @JsonProperty
    private Target target;
}