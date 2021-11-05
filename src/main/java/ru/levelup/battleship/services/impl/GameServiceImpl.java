package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.GamesRepository;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameService;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private GamesRepository repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Game createGame(User player1, User player2) {
        return repository.createGame(player1, player2);
    }

    @Override
    @Transactional
    public Game endGame(Game game, boolean isCompleted, User winner) {
        return repository.endGame(game, true, winner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Game updateGame(Game game, User playerToMove) {
        return repository.updateGame(game, playerToMove);
    }

    @Override
    public long countGamesByWinner(User user) {
        return repository.countGamesByWinner(user);
    }

    @Override
    public long countAllGamesByUser(User user) {
        return repository.countGamesByUser(user);
    }

    @Override
    public Game findGameById(Long id) {
        return repository.getById(id);
    }
}