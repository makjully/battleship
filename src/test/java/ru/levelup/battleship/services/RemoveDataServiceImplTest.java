package ru.levelup.battleship.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.impl.RemoveDataServiceImpl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoveDataServiceImpl.class)
public class RemoveDataServiceImplTest {

    @Autowired
    private RemoveDataServiceImpl removeDataService;

    @MockBean
    private RoomService roomService;

    @MockBean
    private UserService userService;

    @MockBean
    private ShipService shipService;

    @MockBean
    private GameService gameService;

    private Room room;
    private User inviter;
    private User accepting;

    @Before
    public void init() {
        inviter = new User("user001", "123");
        accepting = new User("user002", "123");
        room = new Room(inviter);
    }

    @Test
    public void removeDataAfterGameStartedTest() {
        Game game = new Game();
        inviter.setPlayerFieldArranged(true);
        accepting.setPlayerFieldArranged(true);
        room.setAccepting(accepting);
        room.setGame(game);

        when(gameService.findGameById(room.getGame().getId())).thenReturn(game);
        when(userService.findByLogin(room.getInviter().getLogin())).thenReturn(inviter);
        when(shipService.countShipsByPlayer(inviter)).thenReturn(5L);
        when(userService.findByLogin(room.getAccepting().getLogin())).thenReturn(accepting);
        when(shipService.countShipsByPlayer(accepting)).thenReturn(9L);

        removeDataService.removeDataAfterExit(room);

        verify(roomService, times(1)).updateRoom(room);
        verify(gameService, times(1)).deleteUnfinishedGame(game);
        verify(shipService, times(1)).deleteAll(inviter);
        verify(shipService, times(1)).deleteAll(accepting);
        verify(userService, times(1)).update(inviter);
        verify(userService, times(1)).update(accepting);
        verify(roomService, times(1)).deleteRoom(room);
    }

    @Test
    public void removeDataGameNotStartedTest() {
        room.setAccepting(accepting);

        when(userService.findByLogin(room.getInviter().getLogin())).thenReturn(inviter);
        when(shipService.countShipsByPlayer(inviter)).thenReturn(10L);
        when(userService.findByLogin(room.getAccepting().getLogin())).thenReturn(accepting);
        when(shipService.countShipsByPlayer(accepting)).thenReturn(0L);

        removeDataService.removeDataAfterExit(room);

        verify(roomService, times(0)).updateRoom(room);
        verify(shipService, times(1)).deleteAll(inviter);
        verify(shipService, times(0)).deleteAll(accepting);
        verify(roomService, times(1)).deleteRoom(room);
    }

    @Test
    public void removeDataOnlyInviterTest() {
        when(userService.findByLogin(room.getInviter().getLogin())).thenReturn(inviter);
        when(shipService.countShipsByPlayer(inviter)).thenReturn(10L);

        removeDataService.removeDataAfterExit(room);

        verify(roomService, times(0)).updateRoom(room);
        verify(shipService, times(1)).deleteAll(inviter);
        verify(roomService, times(1)).deleteRoom(room);
        verify(userService, times(0)).findByLogin(accepting.getLogin());
    }

    @Test
    public void removeDataGameEndedTest() {
        Game game = new Game();
        game.setCompleted(true);
        room.setAccepting(accepting);
        room.setGame(game);

        when(userService.findByLogin(room.getInviter().getLogin())).thenReturn(inviter);
        when(shipService.countShipsByPlayer(inviter)).thenReturn(4L);
        when(userService.findByLogin(room.getAccepting().getLogin())).thenReturn(accepting);
        when(shipService.countShipsByPlayer(accepting)).thenReturn(0L);

        removeDataService.removeDataAfterExit(room);

        verify(roomService, times(0)).updateRoom(room);
        verify(gameService, times(0)).deleteUnfinishedGame(game);
        verify(shipService, times(1)).deleteAll(inviter);
        verify(shipService, times(0)).deleteAll(accepting);
        verify(userService, times(1)).update(inviter);
        verify(userService, times(1)).update(accepting);
        verify(roomService, times(1)).deleteRoom(room);
        verify(gameService, times(0)).findGameById(room.getGame().getId());
    }
}