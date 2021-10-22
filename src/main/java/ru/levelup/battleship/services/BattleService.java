package ru.levelup.battleship.services;

import ru.levelup.battleship.model.User;

public interface BattleService {

    String hit(User user, int x, int y);
}
