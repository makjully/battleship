package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

@Repository
public interface ShipsRepository extends JpaRepository<Ship, Long> {

    default Ship saveShip(Ship ship, User player) {
        ship.setPlayer(player);
        return (save(ship));
    }

    long countShipsByPlayer(User player);
}