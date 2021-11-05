package ru.levelup.battleship.services;

import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;

public interface GameService {

    Game createGame(User player1, User player2);

    Game endGame(Game game, boolean isCompleted, User winner);

    Game updateGame(Game game, User playerToMove);

    long countGamesByWinner(User user);

    long countAllGamesByUser(User user);

    Game findGameById(Long id);
}