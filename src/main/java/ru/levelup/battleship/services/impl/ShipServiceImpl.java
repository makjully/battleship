package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.ShipsRepository;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.ShipService;

@Service
@AllArgsConstructor
public class ShipServiceImpl implements ShipService {

    private ShipsRepository repository;

    @Override
    @Transactional
    public Ship saveShip(Ship ship) {
        return repository.saveShip(ship.getPlayer(), ship.getLocation());
    }

    @Override
    public long countShipsByPlayer(User player) {
        return repository.countShipsByPlayer(player);
    }

    @Override
    @Transactional
    public void deleteShip(Ship ship) {
        repository.delete(ship);
    }
}