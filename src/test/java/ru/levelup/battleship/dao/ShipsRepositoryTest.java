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

    private Ship ship_1;
    private Ship ship_2;
    private User player;

    @Before
    public void config() {
        player = new User("padme", "r2d2_c3po");
        usersRepository.save(player);

        ship_1 = new Ship(player, Collections.emptyList());
        shipsRepository.save(ship_1);

        ship_2 = new Ship(player, Collections.emptyList());
        shipsRepository.save(ship_2);
    }

    @Test
    public void countShipsByPlayer() {
        Assert.assertEquals(2, shipsRepository.countShipsByPlayer(player));

        shipsRepository.delete(ship_1);
        Assert.assertEquals(1, shipsRepository.countShipsByPlayer(player));
    }

    @Test
    public void saveShip() {
        Ship newShip = new Ship(player, Collections.emptyList());
        newShip = shipsRepository.saveShip(newShip);
        Optional<Ship> savedShip = shipsRepository.findById(newShip.getId());

        Assert.assertTrue(savedShip.isPresent());
        Assert.assertEquals(3, shipsRepository.countShipsByPlayer(player));
    }

    @Test
    public void deleteShip() {
        shipsRepository.delete(ship_2);
        Optional<Ship> ship = shipsRepository.findById(ship_2.getId());

        Assert.assertFalse(ship.isPresent());
        Assert.assertEquals(1, shipsRepository.countShipsByPlayer(player));
    }
}