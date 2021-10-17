package ru.levelup.battleship.services;

import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

public interface ShipService {

    Ship saveShip(Ship ship);

    long countShipsByPlayer(User player);

    void deleteShip(Ship ship);
}
