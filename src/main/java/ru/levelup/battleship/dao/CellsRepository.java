package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;

public interface CellsRepository extends JpaRepository<Cell, Integer> {

    @Transactional
    default Cell saveCell(int x, int y) {
        return save(new Cell(x, y));
    }

    Cell findCellByCoordinateXAndCoordinateY(int x, int y);

    @Query("select count(cl) from Cell cl where cl.ship =:ship")
    long countCellsByShip(@Param("ship") Ship ship);

    @Modifying
    default Cell updateCellWithShip(Cell cell, Ship ship) {
        cell.setShip(ship);
        return save(cell);
    }

    @Transactional
    void deleteCellByCoordinateXAndCoordinateY(int x, int y);
}