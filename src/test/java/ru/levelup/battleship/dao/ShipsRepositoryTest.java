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
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.TestConfiguration;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ShipsRepositoryTest {

    @Autowired
    private ShipsRepository shipsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CellsRepository cellsRepository;

    private Ship ship_1;
    private Ship ship_2;
    private User player;

    @Before
    public void init() {
        player = new User("padme", "r2d2_c3po");
        usersRepository.save(player);

        ship_1 = new Ship(Collections.emptyList());
        ship_1.setPlayer(player);

        ship_2 = new Ship(Collections.emptyList());
        ship_2.setPlayer(player);
        shipsRepository.saveAll(List.of(ship_1, ship_2));
    }

    @Test
    public void countShipsByPlayerTest() {
        Assert.assertEquals(2, shipsRepository.countShipsByPlayer(player));

        shipsRepository.delete(ship_1);
        Assert.assertEquals(1, shipsRepository.countShipsByPlayer(player));
    }

    @Test
    public void saveShipTest() {
        Ship newShip = new Ship(Collections.emptyList());
        newShip = shipsRepository.saveShip(newShip, player);
        Optional<Ship> savedShip = shipsRepository.findById(newShip.getId());

        Assert.assertTrue(savedShip.isPresent());
        Assert.assertEquals(3, shipsRepository.countShipsByPlayer(player));
    }

    @Test
    public void deleteShipTest() {
        shipsRepository.delete(ship_2);
        Optional<Ship> ship = shipsRepository.findById(ship_2.getId());

        Assert.assertFalse(ship.isPresent());
        Assert.assertEquals(1, shipsRepository.countShipsByPlayer(player));

        Cell cell_3_3 = new Cell(3, 3);
        Cell cell_3_4 = new Cell(3, 4);
        Cell cell_3_5 = new Cell(3, 5);

        ship_1.setLocation(List.of(cell_3_3, cell_3_4, cell_3_5));
        cellsRepository.saveAll(List.of(cell_3_3, cell_3_4, cell_3_5));
        shipsRepository.save(ship_1);
        Optional<Cell> found = cellsRepository.findById(cell_3_5.getId());
        Assert.assertTrue(found.isPresent());

        shipsRepository.delete(ship_1);
        Assert.assertEquals(0, shipsRepository.countShipsByPlayer(player));

        Optional<Cell> deleted = cellsRepository.findById(cell_3_3.getId());
        Assert.assertFalse(deleted.isPresent());
    }

    @Test
    @Transactional
    public void deleteShipsByPlayerTest() {
        shipsRepository.deleteAllByPlayer(player);
        Assert.assertEquals(0, shipsRepository.countShipsByPlayer(player));
    }
}