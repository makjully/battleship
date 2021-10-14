package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;

public interface GamesRepository extends JpaRepository<Game, Integer> {

    default Game createGame(User player1, User player2) {
        return save(new Game(player1, player2));
    }

    default Game updateGame(Game game, boolean isCompleted, User winner) {
        game.setCompleted(isCompleted);
        game.setWinner(winner);
        return save(game);
    }
}