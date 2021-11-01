package ru.levelup.battleship.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;

import java.time.LocalDateTime;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {

    void deleteGameRoomsByAcceptingIsNullAndTimestampIsLessThanEqual(LocalDateTime time);

    Page<GameRoom> findGameRoomsByAcceptingIsNull(Pageable pageable);

    default GameRoom createRoom(User inviter) {
        return save(new GameRoom(inviter));
    }

    default GameRoom updateRoom(GameRoom room, User accepting) {
        room.setAccepting(accepting);
        return save(room);
    }
}