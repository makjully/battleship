package ru.levelup.battleship.services;

import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;

public interface CellService {

    Cell saveCell(Cell cell);

    Cell findCellByCoordinateXAndCoordinateY(int x, int y);

    long countCellsByShip(Ship ship);

    Cell updateCellWithShip(Cell cell, Ship ship);

    void deleteCell(Cell cell);
}