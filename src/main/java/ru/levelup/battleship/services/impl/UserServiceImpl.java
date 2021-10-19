package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.UsersRepository;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UsersRepository repository;

    @Override
    @Transactional
    public User updateRating(User user, double winnerPoints) {
        return repository.updateRating(user, winnerPoints);
    }

    @Override
    @Transactional
    public User createUser(String login, String password) {
        return repository.createUser(login, password);
    }

    @Override
    @Transactional
    public void updateWhenBoardPrepared(User user) {
        repository.updateWhenBoardPrepared(user);
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

}