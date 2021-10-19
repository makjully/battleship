package ru.levelup.battleship.services;

import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.User;


import java.util.List;

public interface ShipsArrangeService {

    List<Cell> arrangeShips(User user);
}