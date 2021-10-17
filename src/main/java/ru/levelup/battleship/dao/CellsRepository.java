package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;

@Repository
public interface CellsRepository extends JpaRepository<Cell, Integer> {

    default Cell saveCell(int x, int y) {
        return save(new Cell(x, y));
    }

    Cell findCellByCoordinateXAndCoordinateY(int x, int y);

    long countCellsByShip(Ship ship);

    default Cell updateCell(Cell cell, Ship ship) {
        cell.setShip(ship);
        return save(cell);
    }
}