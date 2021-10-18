package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;

@Repository
public interface CellsRepository extends JpaRepository<Cell, Integer> {

    default Cell saveCell(Cell cell) {
        return save(cell);
    }

    Cell findCellByCoordinateXAndCoordinateY(int x, int y);

    long countCellsByShip(Ship ship);
}