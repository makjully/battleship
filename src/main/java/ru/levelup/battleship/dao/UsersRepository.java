package ru.levelup.battleship.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.levelup.battleship.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {

    default User createUser(String login, String password) {
        return save(new User(login, password));
    }

    default User updateRating(User user, double winnerPoints) {
        user.setRating(user.getRating() + winnerPoints);
        return save(user);
    }

    @Query
    User findUserByIdAndPlayerFieldArranged(int id, boolean playerFieldArranged);
}