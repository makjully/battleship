package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.levelup.battleship.model.Cell;

public interface CellsRepository extends JpaRepository<Cell, Integer> {

    default Cell saveCell(int x, int y) {
        return save(new Cell(x, y));
    }

    @Query
    Cell findCellByCoordinateXAndCoordinateY(int x, int y);

    @Query
    int countCellsByShipId(int shipId);

    @Query
    void deleteCellByCoordinateXAndAndCoordinateY(int x, int y);
}