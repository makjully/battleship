package ru.levelup.battleship.web_socket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonDeserialize(as = ReadyMessage.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReadyMessage {
   @JsonProperty
   private String login;

   @JsonProperty
   private boolean isReady;
}