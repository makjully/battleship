package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.List;

public interface CellsRepository extends JpaRepository<Cell, Long> {

    @Query("from Cell cl where cl.coordinateX = :x and cl.coordinateY = :y and cl.ship.id in " +
            "(select s.id from Ship s where s.player = :user)")
    Cell findCell(@Param("user") User user, int x, int y);

    long countCellsByShip(Ship ship);

    @Query("from Cell cl where cl.ship.id in (select s.id from Ship s where s.player = :user)")
    List<Cell> findAll(@Param("user") User user);
}