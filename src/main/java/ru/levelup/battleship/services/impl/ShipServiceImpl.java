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
    @Transactional(rollbackFor = Exception.class)
    public Ship saveShip(Ship ship, User player) {
        return repository.saveShip(ship, player);
    }

    @Override
    public long countShipsByPlayer(User player) {
        return repository.countShipsByPlayer(player);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShip(Ship ship) {
        repository.delete(ship);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(User user) {
        repository.deleteAllByPlayer(user);
    }
}