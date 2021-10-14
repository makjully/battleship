package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.List;

public interface ShipsRepository extends JpaRepository<Ship, Integer> {

    default Ship saveShip(User player, List<Cell> location) {
        return (save(new Ship(player, location)));
    }

    @Query
    int countShipsByPlayer(User player);

    @Query
    void deleteShipById(int id);
}
