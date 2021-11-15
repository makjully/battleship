package ru.levelup.battleship.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;

import java.time.LocalDateTime;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findRoomsByAcceptingIsNullOrderByTimestampDesc(Pageable pageable);

    default Room createRoom(User inviter) {
        return save(new Room(inviter));
    }

    default Room updateRoom(Room room, User accepting) {
        room.setAccepting(accepting);
        return save(room);
    }

    @Query("from Room r where r.inviter = :user or r.accepting = :user")
    Room findRoomByUser(@Param("user") User user);
}