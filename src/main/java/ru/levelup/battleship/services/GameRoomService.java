package ru.levelup.battleship.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.levelup.battleship.model.GameRoom;
import ru.levelup.battleship.model.User;

import java.time.LocalDateTime;

public interface GameRoomService {

    void deleteExpiredRooms(LocalDateTime time);

    Page<GameRoom> findActualGameRooms(Pageable pageable);

    GameRoom createRoom(User inviter);

    GameRoom updateRoomWhenAccept(GameRoom room, User accepting);

    GameRoom findGameRoomByInviter(User inviter);

    void deleteGameRoomAfterBattle(GameRoom room);
}