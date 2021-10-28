package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.services.ShipsArrangeService;
import ru.levelup.battleship.services.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ShipsArrangeRestController {

    private ShipsArrangeService shipsArrangeService;
    private UserService userService;

    @GetMapping(value = "api/arrange/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cell>> getCoordinates(@PathVariable String login) {
        User user = userService.findByLogin(login);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<Cell> cells = shipsArrangeService.arrangeShips(user);
        userService.updateWhenBoardPrepared(user);

        return new ResponseEntity<>(cells, HttpStatus.OK);
    }
}