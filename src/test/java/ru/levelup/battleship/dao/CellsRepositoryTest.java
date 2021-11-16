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
import java.util.List;
import java.util.Optional;

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
    private User player;
    private Cell cell_2_3;
    private Cell cell_2_4;
    private Cell cell_2_5;

    @Before
    public void init() {
        cell_2_3 = new Cell(2, 3);
        cell_2_4 = new Cell(2, 4);
        cell_2_5 = new Cell(2, 5);

        player = new User("darthVader", "order66");
        usersRepository.save(player);

        ship = new Ship(new ArrayList<>(Arrays.asList(cell_2_3, cell_2_4, cell_2_5)));
        shipsRepository.saveShip(ship, player);
    }

    @Test
    public void countCellsByShipTest() {
        Assert.assertEquals(3, cellsRepository.countCellsByShip(ship));

        cellsRepository.deleteAll();
        Assert.assertEquals(0, cellsRepository.countCellsByShip(ship));
    }

    @Test
    public void findCellTest() {
        Cell found = cellsRepository.findCell(player, 2, 4);
        Assert.assertEquals(cell_2_4, found);

        found = cellsRepository.findCell(player, 5, 5);
        Assert.assertNull(found);
    }

    @Test
    public void findCellsByUserTest() {
        List<Cell> found = cellsRepository.findAll(player);

        Assert.assertEquals(cell_2_3, found.get(0));
        Assert.assertEquals(3, found.size());
    }
}