package ru.levelup.battleship.services;

import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.List;

public interface ShipService {

    Ship saveShip(Ship ship, User player);

    long countShipsByPlayer(User player);

    void deleteShip(Ship ship);

    void deleteAll(User user);
}
