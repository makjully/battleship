package ru.levelup.battleship.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.TestConfiguration;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CellsRepositoryTest {

    @Autowired
    private CellsRepository cellsRepository;
    @Autowired
    private ShipsRepository shipsRepository;
    @Autowired
    private UsersRepository usersRepository;

    private Ship ship;
    private Cell cell_2_3;
    private Cell cell_2_4;
    private Cell cell_2_5;

    @Before
    public void config() {
        cell_2_3 = cellsRepository.saveCell(2, 3);
        cell_2_4 = cellsRepository.saveCell(2, 4);
        cell_2_5 = cellsRepository.saveCell(2, 5);

        User player = new User("darthVader", "order66");
        usersRepository.save(player);

        ship = new Ship(player, new ArrayList<>(Arrays.asList(cell_2_3, cell_2_4, cell_2_5)));
        shipsRepository.save(ship);

        cellsRepository.updateCellWithShip(cell_2_3, ship);
        cellsRepository.updateCellWithShip(cell_2_4, ship);
        cellsRepository.updateCellWithShip(cell_2_5, ship);
    }

    @Test
    public void findCellByCoordinateXAndCoordinateY() {
        Cell cell = cellsRepository.findCellByCoordinateXAndCoordinateY(2, 4);
        Assert.assertEquals(cell_2_4, cell);

        cell = cellsRepository.findCellByCoordinateXAndCoordinateY(2, 3);
        Assert.assertEquals(cell_2_3, cell);

        cell = cellsRepository.findCellByCoordinateXAndCoordinateY(2, 5);
        Assert.assertEquals(cell_2_5, cell);

        cell = cellsRepository.findCellByCoordinateXAndCoordinateY(6, 6);
        Assert.assertNull(cell);
    }

    @Test
    public void countCellsByShip() {
        long cellCount = cellsRepository.countCellsByShip(ship);

        Assert.assertEquals(3, cellCount);
    }

    @Test
    public void deleteCell() {
        cellsRepository.delete(cell_2_5);
        Cell cell = cellsRepository.findCellByCoordinateXAndCoordinateY(2, 5);
        Assert.assertNull(cell);
        Assert.assertEquals(2, cellsRepository.countCellsByShip(ship));

        cellsRepository.deleteCellByCoordinateXAndCoordinateY(2, 3);
        cell = cellsRepository.findCellByCoordinateXAndCoordinateY(2, 3);
        Assert.assertNull(cell);
        Assert.assertEquals(1, cellsRepository.countCellsByShip(ship));
    }
}