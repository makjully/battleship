package ru.levelup.battleship.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;

import java.time.LocalDateTime;

public interface RoomRepository extends JpaRepository<Room, Long> {

    void deleteGameRoomsByAcceptingIsNullAndTimestampIsLessThanEqual(LocalDateTime time);

    Page<Room> findGameRoomsByAcceptingIsNullOrderByTimestampDesc(Pageable pageable);

    default Room createRoom(User inviter) {
        return save(new Room(inviter));
    }

    default Room updateRoom(Room room, User accepting) {
        room.setAccepting(accepting);
        return save(room);
    }

    Room findGameRoomByInviter(User inviter);
}