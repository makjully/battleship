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
    @Transactional(rollbackFor = Exception.class)
    public User updateRating(User user, double winnerPoints) {
        return repository.updateRating(user, winnerPoints);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(String login, String password) {
        return repository.createUser(login, password);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWhenBoardPrepared(User user) {
        repository.updateWhenBoardPrepared(user);
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User update(User user) {
        return repository.save(user);
    }
}