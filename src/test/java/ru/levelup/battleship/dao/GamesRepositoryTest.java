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
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class GamesRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private GamesRepository gamesRepository;

    User player_1;
    User player_2;
    Game game;

    @Before
    public void config() {
        player_1 = usersRepository.createUser("obiVan", "jedi_165");
        player_2 = usersRepository.createUser("darthMaul", "dark_side13");
        game = gamesRepository.createGame(player_1, player_2);
    }

    @Test
    public void checkGameCreated() {
        Assert.assertTrue(gamesRepository.findById(game.getId()).isPresent());
        Assert.assertNotNull(game.getPlayerToMove());

        System.out.println(game.getPlayerToMove().getLogin());
    }

    @Test
    public void updateGameWhenFinnish() {
        game = gamesRepository.endGame(game, true, player_1);

        Assert.assertTrue(game.isCompleted());
        Assert.assertEquals(player_1, game.getWinner());
    }

    @Test
    public void updateGameWhenMove() {
        game = gamesRepository.updateGame(game, player_2);
        Assert.assertEquals(player_2, game.getPlayerToMove());

        game = gamesRepository.updateGame(game, player_1);
        Assert.assertEquals(player_1, game.getPlayerToMove());
    }

    @Test
    public void countGamesByWinner() {
        Game game_1 = gamesRepository.createGame(player_1, player_2);
        gamesRepository.endGame(game_1, true, player_1);
        Game game_2 = gamesRepository.createGame(player_1, player_2);
        gamesRepository.endGame(game_2, true, player_1);
        Game game_3 = gamesRepository.createGame(player_1, player_2);
        gamesRepository.endGame(game_3, true, player_2);

        Assert.assertEquals(2, gamesRepository.countGamesByWinner(player_1));
        Assert.assertEquals(1, gamesRepository.countGamesByWinner(player_2));
    }

    @Test
    public void countGamesByUser() {
        Assert.assertEquals(0, gamesRepository.countGamesByUser(player_1));

        gamesRepository.endGame(game, true, player_1);
        Assert.assertEquals(1, gamesRepository.countGamesByUser(player_1));

        Game game = gamesRepository.createGame(player_1, player_2);
        gamesRepository.endGame(game, true, player_2);
        Assert.assertEquals(2, gamesRepository.countGamesByUser(player_1));
    }
}