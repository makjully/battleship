package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.GameBoard;
import ru.levelup.battleship.services.CellService;
import ru.levelup.battleship.services.ShipService;
import ru.levelup.battleship.services.ShipsArrangeService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShipsArrangeServiceImpl implements ShipsArrangeService {

    private GameBoard board;
    private ShipService shipService;
    private CellService cellService;

    @Override
    public List<Cell> arrangeShips(User user) {
        board.setPlayer(user);
        List<Ship> ships = board.arrangeAllShips();
        List<Cell> cells = new ArrayList<>();

        ships.forEach(ship -> {
            ship = shipService.saveShip(ship);
            for (Cell cell : ship.getLocation()) {
                cell.setShip(ship);
                cellService.saveCell(cell);
                cells.add(cell);
            }
        });

        return cells;
    }
}