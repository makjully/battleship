package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.RoomRepository;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.RoomService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    @Override
    public Page<Room> findActualGameRooms(Pageable pageable) {
        return roomRepository.findRoomsByAcceptingIsNullOrderByTimestampDesc(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Room createRoom(User inviter) {
        return roomRepository.createRoom(inviter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Room updateRoomWhenAccept(Room room, User accepting) {
        return roomRepository.updateRoom(room, accepting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Room room) {
        roomRepository.delete(room);
    }

    @Override
    @Transactional
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room findRoomByUser(User user) {
        return roomRepository.findRoomByUser(user);
    }
}