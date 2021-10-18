package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.GameBoard;
import ru.levelup.battleship.services.CellService;
import ru.levelup.battleship.services.ShipService;
import ru.levelup.battleship.services.ShipsArrangeService;
import ru.levelup.battleship.services.UserService;

@Service
@AllArgsConstructor
public class ShipsArrangeServiceImpl implements ShipsArrangeService {

    private GameBoard board;
    private ShipService shipService;
    private CellService cellService;
    private UserService userService;

    @Override
    public void arrangeShips(User user) {
        board.setPlayer(user);
        board.arrangeAllShips();

        if (board.getShips().size() < 10)
            arrangeShips(user);

        board.getShips().forEach(ship -> {
            ship = shipService.saveShip(ship);
            for (Cell cell : ship.getLocation()) {
                cell.setShip(ship);
                cellService.saveCell(cell);
            }
        });

        userService.updateWhenBoardPrepared(user);
    }
}