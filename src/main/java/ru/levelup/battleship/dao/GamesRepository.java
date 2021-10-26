package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;

import java.util.Random;

@Repository
public interface GamesRepository extends JpaRepository<Game, Long> {

    default Game createGame(User player1, User player2) {
        Game game = new Game(player1, player2);
        User[] players = {player1, player2};
        game.setPlayerToMove(players[new Random().nextInt(2)]);
        return save(game);
    }

    default Game endGame(Game game, boolean isCompleted, User winner) {
        game.setCompleted(isCompleted);
        game.setWinner(winner);
        return save(game);
    }

    default Game updateGame(Game game, User playerToMove) {
        game.setPlayerToMove(playerToMove);
        return save(game);
    }

    long countGamesByWinner(User user);

    @Query("select count(g) from Game g where (g.player1 = :user or g.player2 = :user) and g.isCompleted = true")
    long countGamesByUser(User user);
}