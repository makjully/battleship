package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

@Repository
public interface CellsRepository extends JpaRepository<Cell, Long> {

    @Query("from Cell cl where cl.coordinateX = :x and cl.coordinateY = :y and cl.ship.id = " +
            "(select s.id from Ship s where s.player = :user)")
    Cell findCell(@Param("user") User user, int x, int y);

    long countCellsByShip(Ship ship);
}