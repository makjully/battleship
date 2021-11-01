package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.levelup.battleship.dao.GameRoomRepository;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.GameRoomService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private GameRoomRepository repository;

    @Override
    public void deleteExpiredRooms(LocalDateTime time) {
        repository.deleteGameRoomsByAcceptingIsNullAndTimestampIsLessThanEqual(time);
    }

    @Override
    public Page<GameRoom> findActualGameRooms(Pageable pageable) {
        return repository.findGameRoomsByAcceptingIsNull(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameRoom createRoom(User inviter) {
        return repository.createRoom(inviter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameRoom updateRoomWhenAccept(GameRoom room, User accepting) {
        return repository.updateRoom(room, accepting);
    }
}