package ru.levelup.battleship.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;

import java.util.Optional;

public interface RoomService {

    Page<Room> findActualGameRooms(Pageable pageable);

    Room createRoom(User inviter);

    Room updateRoomWhenAccept(Room room, User accepting);

    void deleteGameRoom(Room room);

    Optional<Room> findById(Long id);

    Room updateRoom(Room room);

    Room findRoomByUser(User user);
}