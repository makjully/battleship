package ru.levelup.battleship.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.levelup.battleship.TestMvcConfiguration;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.RoomService;
import ru.levelup.battleship.services.UserService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMvcConfiguration.class)
public class RoomsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private UserService userService;

    @Mock
    private Page<Room> rooms;

    @Test
    public void redirectTest() throws Exception {
        mvc.perform(get("/")
                        .with(user("user001").roles("user"))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("app/rooms"));

        mvc.perform(get("/app")
                        .with(user("user001").roles("user"))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("app/rooms"));
    }

    @Test
    public void findRoomsFirstPageTest() throws Exception {
        User user = new User("user001", "123");
        when(userService.findByLogin("user001")).thenReturn(user);
        when(roomService.findActualGameRooms(PageRequest.of(0, RoomsController.PAGE_RESULTS)))
                .thenReturn(rooms);

        mvc.perform(get("/app/rooms")
                        .with(user("user001").roles("user"))
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("page", 1))
                .andExpect(model().attribute("rooms", rooms));

        verify(userService, times(1)).findByLogin("user001");
        verify(roomService, times(1)).findActualGameRooms(
                PageRequest.of(0, RoomsController.PAGE_RESULTS));
    }
}