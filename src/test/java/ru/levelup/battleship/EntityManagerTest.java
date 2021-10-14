package ru.levelup.battleship;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.dao.*;
import ru.levelup.battleship.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class EntityManagerTest {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private GamesRepository gamesRepository;
    @Autowired
    private ShipsRepository shipsRepository;
    @Autowired
    private CellsRepository cellsRepository;

    @Test
    public void smokeTest() {
        User player1 = new User("rick127", "12345");
        usersRepository.save(player1);
        User player2 = new User("morty09", "54321");
        usersRepository.save(player2);

        Game game = new Game(player1, player2);
        gamesRepository.save(game);

        Cell cell1 = new Cell(0, 1);
        cellsRepository.save(cell1);
        Cell cell2 = new Cell(1, 1);
        cellsRepository.save(cell2);
        Cell cell3 = new Cell(3, 4);
        cellsRepository.save(cell3);

        Ship ship1 = new Ship(player1, Arrays.asList(cell1, cell2));
        shipsRepository.save(ship1);
        Ship ship2 = new Ship(player2, List.of(cell3));
        shipsRepository.save(ship2);

        Optional<User> foundUser = usersRepository.findById(player1.getId());
        Assert.assertTrue(foundUser.isPresent());

        foundUser = usersRepository.findById(player2.getId());
        Assert.assertTrue(foundUser.isPresent());

        Optional<Game> foundGame = gamesRepository.findById(game.getId());
        Assert.assertTrue(foundGame.isPresent());

        Optional<Ship> foundShip = shipsRepository.findById(ship1.getId());
        Assert.assertTrue(foundShip.isPresent());

        foundShip = shipsRepository.findById(ship2.getId());
        Assert.assertTrue(foundShip.isPresent());

        Optional<Cell> foundCell = cellsRepository.findById(cell1.getId());
        Assert.assertTrue(foundCell.isPresent());

        foundCell = cellsRepository.findById(cell3.getId());
        Assert.assertTrue(foundCell.isPresent());
    }
}