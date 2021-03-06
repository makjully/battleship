package ru.levelup.battleship.services;

import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.List;

public interface CellService {

    long countCellsByShip(Ship ship);

    void deleteCell(Cell cell);

    Cell findCell(User user, int x, int y);

    List<Cell> findAll(User user);
}