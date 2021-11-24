package ru.levelup.battleship;

import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.UserService;

public class TestUserService implements UserService {
    User user;

    public TestUserService() {
        user = new User("ws", "123");
    }

    @Override
    public User createUser(String login, String password) {
        return user;
    }

    @Override
    public User updateRating(User user, double winnerPoints) {
        return null;
    }

    @Override
    public void updateWhenBoardPrepared(User user) {
        user.setPlayerFieldArranged(true);
    }

    @Override
    public User findByLogin(String login) {
        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }
}
