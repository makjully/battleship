package ru.levelup.battleship.process;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Result {

    HIT("hit the cell of the ship"),
    MISS("not hit off target"),
    SINK("sink an opponent's ship"),
    WIN("sink all opponent's ships. End of the game");

    private final String description;
}