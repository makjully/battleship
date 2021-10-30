package ru.levelup.battleship.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.levelup.battleship.services.GameRoomService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SchedulerService {

    private static final String CRON = "0 0/15 * * * *";

    private final GameRoomService service;

    @Scheduled(cron = CRON)
    public void deleteExpiredRooms() {
        LocalDateTime expired = LocalDateTime.now().minusMinutes(15);
        service.deleteExpiredRooms(expired);
    }
}