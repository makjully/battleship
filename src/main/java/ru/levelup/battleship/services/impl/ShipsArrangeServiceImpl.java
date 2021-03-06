package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.ShipsArrange;
import ru.levelup.battleship.services.ShipService;
import ru.levelup.battleship.services.ShipsArrangeService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShipsArrangeServiceImpl implements ShipsArrangeService {

    private ShipService shipService;

    @Override
    public List<Cell> arrangeShips(User user) {
        if (shipService.countShipsByPlayer(user) != 0)
            shipService.deleteAll(user);

        ShipsArrange board = new ShipsArrange();
        List<Cell> cells = new ArrayList<>();

        board.getArrangedShips().forEach(ship -> {
            ship = shipService.saveShip(ship, user);
            cells.addAll(ship.getLocation());
        });

        return cells;
    }
}