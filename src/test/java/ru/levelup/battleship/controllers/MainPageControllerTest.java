package ru.levelup.battleship.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.levelup.battleship.TestMvcConfiguration;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.UserService;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMvcConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MainPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoomService roomService;

    private User user;

    @Before
    public void init() {
        user = new User("user001", "123");
    }

    @Test
    public void showGamePageTest() throws Exception {
        Long roomId = 1L;
        Room room = new Room(user);
        when(userService.findByLogin("user001")).thenReturn(user);
        when(roomService.findById(roomId)).thenReturn(Optional.of(room));

        mvc.perform(get("/app/main/1")
                        .with(user("user001").roles("user"))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("room", room));

        verify(userService, times(1)).findByLogin("user001");
        verify(roomService, times(1)).findById(roomId);
    }

    @Test
    public void exitGameTest() throws Exception {
        mvc.perform(get("/app/game/exit")
                        .with(user("user001").roles("user"))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/rooms"));
    }

    @Test
    public void createRoomTest() throws Exception {
        when(userService.findByLogin("user001")).thenReturn(user);
        Room room = new Room(user);
        room.setId(1L);
        when(roomService.createRoom(user)).thenReturn(room);

        mvc.perform(post("/app/main/create")
                        .with(user("user001").roles("user"))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/app/main/{room_id}", room.getId()));

        verify(userService, times(1)).findByLogin("user001");
        verify(roomService, times(1)).createRoom(user);
    }

    @Test
    public void joinRoomTest() throws Exception {
        when(userService.findByLogin("user001")).thenReturn(user);
        Room room = new Room(user);
        Long roomId = 1L;
        room.setId(roomId);
        when(roomService.findById(roomId)).thenReturn(Optional.of(room));

        mvc.perform(post("/app/main/join")
                        .with(user("user001").roles("user"))
                        .with(csrf())
                        .param("id", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/app/main/{room_id}", room.getId()));

        verify(userService, times(1)).findByLogin("user001");
        verify(roomService, times(1)).findById(roomId);
        verify(roomService, times(1)).updateRoomWhenAccept(room, user);
    }

    @Test
    public void joinRoomUnsuccessfullyTest() throws Exception {
        when(userService.findByLogin("user001")).thenReturn(user);
        Room room = new Room(user);
        Long roomId = 1L;
        room.setId(roomId);
        room.setAccepting(new User("user002", "123"));
        when(roomService.findById(roomId)).thenReturn(Optional.of(room));

        mvc.perform(post("/app/main/join")
                        .with(user("user001").roles("user"))
                        .with(csrf())
                        .param("id", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/rooms"));

        verify(userService, times(1)).findByLogin("user001");
        verify(roomService, times(1)).findById(roomId);
        verify(roomService, times(0)).updateRoomWhenAccept(room, user);
    }
}