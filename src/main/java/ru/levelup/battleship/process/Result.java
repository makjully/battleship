package ru.levelup.battleship.process;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString(includeFieldNames = false)
public enum Result {

    HIT("Hit"),
    MISS("Miss"),
    SINK("Sink"),
    WIN("Win");

    private final String description;
}