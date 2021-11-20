package ru.levelup.battleship.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;
import ru.levelup.battleship.services.impl.BattleServiceImpl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BattleServiceImpl.class)
public class BattleServiceImplTest {

    @Autowired
    private BattleServiceImpl battleService;

    @MockBean
    private ShipService shipService;

    @MockBean
    private CellService cellService;

    private User user;
    private Ship ship;

    @Before
    public void init() {
        user = new User("user001", "123");
        ship = new Ship();
    }

    @Test
    public void resultMissTest() {
        Result expected = Result.MISS;
        int x = 8;
        int y = 4;

        when(cellService.findCell(user, x, y)).thenReturn(null);
        Result actual = battleService.hit(user, x, y);

        Assert.assertEquals(expected, actual);

        verify(cellService, times(1)).findCell(user, x, y);
    }

    @Test
    public void resultHitTest() {
        Result expected = Result.HIT;
        int x = 5;
        int y = 5;
        Cell cell = new Cell(5, 5);
        cell.setShip(ship);

        when(cellService.findCell(user, x, y)).thenReturn(cell);
        when(cellService.countCellsByShip(ship)).thenReturn(5L);
        Result actual = battleService.hit(user, x, y);

        Assert.assertEquals(expected, actual);

        verify(cellService, times(1)).findCell(user, x, y);
        verify(cellService, times(1)).deleteCell(cell);
        verify(cellService, times(1)).countCellsByShip(ship);
    }

    @Test
    public void resultSinkTest() {
        Result expected = Result.SINK;
        int x = 6;
        int y = 6;
        Cell cell = new Cell(6, 6);
        cell.setShip(ship);

        when(cellService.findCell(user, x, y)).thenReturn(cell);
        when(cellService.countCellsByShip(ship)).thenReturn(0L);
        when(shipService.countShipsByPlayer(user)).thenReturn(5L);
        Result actual = battleService.hit(user, x, y);

        Assert.assertEquals(expected, actual);

        verify(cellService, times(1)).findCell(user, x, y);
        verify(cellService, times(1)).deleteCell(cell);
        verify(cellService, times(1)).countCellsByShip(ship);
        verify(shipService, times(1)).deleteShip(ship);
        verify(shipService, times(1)).countShipsByPlayer(user);
    }

    @Test
    public void resultWinTest() {
        Result expected = Result.WIN;
        int x = 2;
        int y = 7;
        Cell cell = new Cell(2, 7);
        cell.setShip(ship);

        when(cellService.findCell(user, x, y)).thenReturn(cell);
        when(cellService.countCellsByShip(ship)).thenReturn(0L);
        when(shipService.countShipsByPlayer(user)).thenReturn(0L);
        Result actual = battleService.hit(user, x, y);

        Assert.assertEquals(expected, actual);

        verify(cellService, times(1)).findCell(user, x, y);
        verify(cellService, times(1)).deleteCell(cell);
        verify(cellService, times(1)).countCellsByShip(ship);
        verify(shipService, times(1)).deleteShip(ship);
        verify(shipService, times(1)).countShipsByPlayer(user);
    }
}
