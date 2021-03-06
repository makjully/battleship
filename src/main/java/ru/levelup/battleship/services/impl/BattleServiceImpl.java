package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;
import ru.levelup.battleship.services.BattleService;
import ru.levelup.battleship.services.CellService;
import ru.levelup.battleship.services.ShipService;

@Service
@AllArgsConstructor
public class BattleServiceImpl implements BattleService {

    private ShipService shipService;
    private CellService cellService;

    @Override
    public Result hit(User user, int x, int y) {
        Result result;
        Cell cellToHit = cellService.findCell(user, x, y);
        if (cellToHit == null)
            return Result.MISS;

        Ship shipHit = cellToHit.getShip();
        cellService.deleteCell(cellToHit);

        if (cellService.countCellsByShip(shipHit) > 0)
            result = Result.HIT;
        else {
            shipService.deleteShip(shipHit);
            if (shipService.countShipsByPlayer(user) == 0)
                result = Result.WIN;
            else
                result = Result.SINK;
        }

        return result;
    }
}