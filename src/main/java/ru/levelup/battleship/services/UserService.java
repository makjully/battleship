package ru.levelup.battleship.services;

import ru.levelup.battleship.model.User;

public interface UserService {

    User createUser(String login, String password);

    User updateRating(User user, double winnerPoints);

    void updateWhenBoardPrepared(User user);
}
