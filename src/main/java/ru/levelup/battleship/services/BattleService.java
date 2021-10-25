package ru.levelup.battleship.services;

import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;

public interface BattleService {

    Result hit(User user, int x, int y);
}
