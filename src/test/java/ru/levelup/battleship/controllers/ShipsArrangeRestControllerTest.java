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
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.CellService;
import ru.levelup.battleship.services.ShipsArrangeService;
import ru.levelup.battleship.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestMvcConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ShipsArrangeRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShipsArrangeService shipsArrangeService;

    @MockBean
    private UserService userService;

    @MockBean
    private CellService cellService;

    private List<Cell> cells;

    @Before
    public void init() {
        cells = new ArrayList<>(List.of(
                new Cell(5, 5),
                new Cell(5, 6),
                new Cell(5, 7)
        ));
    }

    @Test
    public void arrangeGetCoordinatesTest() throws Exception {
        User user = new User("user001", "123");
        when(userService.findByLogin("user001")).thenReturn(user);
        when(shipsArrangeService.arrangeShips(user)).thenReturn(cells);

        mvc.perform(get("/api/arrange/user001")
                        .with(user("user001").roles("user"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].coordinateX", is(5)))
                .andExpect(jsonPath("$[0].coordinateY", is(5)))
                .andExpect(jsonPath("$[1].coordinateX", is(5)))
                .andExpect(jsonPath("$[1].coordinateY", is(6)))
                .andExpect(jsonPath("$[2].coordinateX", is(5)))
                .andExpect(jsonPath("$[2].coordinateY", is(7)));

        verify(userService, times(1)).findByLogin("user001");
        verify(shipsArrangeService, times(1)).arrangeShips(user);
    }

    @Test
    public void arrangeNoContentTest() throws Exception {
        when(userService.findByLogin("user003")).thenReturn(null);

        mvc.perform(get("/api/arrange/user003")
                        .with(user("user003").roles("user"))
                )
                .andExpect(status().isNoContent());

        verify(userService, times(1)).findByLogin("user003");
    }

    @Test
    public void loadBoardTest() throws Exception {
        User user = new User("user002", "123");
        cells.remove(0);
        when(userService.findByLogin("user002")).thenReturn(user);
        when(cellService.findAll(user)).thenReturn(cells);

        mvc.perform(get("/api/loadBoard/user002")
                        .with(user("user002").roles("user"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].coordinateX", is(5)))
                .andExpect(jsonPath("$[0].coordinateY", is(6)))
                .andExpect(jsonPath("$[1].coordinateX", is(5)))
                .andExpect(jsonPath("$[1].coordinateY", is(7)));

        verify(userService, times(1)).findByLogin("user002");
        verify(cellService, times(1)).findAll(user);
    }

    @Test
    public void loadBoardNoContentTest() throws Exception {
        when(userService.findByLogin("user004")).thenReturn(null);

        mvc.perform(get("/api/loadBoard/user004")
                        .with(user("user004").roles("user"))
                )
                .andExpect(status().isNoContent());

        verify(userService, times(1)).findByLogin("user004");
    }
}