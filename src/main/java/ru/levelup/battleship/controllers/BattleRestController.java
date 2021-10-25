package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;
import ru.levelup.battleship.services.BattleService;
import ru.levelup.battleship.services.GameService;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class BattleRestController {

    private BattleService battleService;
    private GameService gameService;

    @GetMapping("api/game/hit")
    public ResponseEntity<Result> hitOpponent(@RequestParam("game") Game game,
                                              @RequestParam("user") User user,
                                              @RequestParam("opponent") User opponent,
                                              @RequestParam("x") int x,
                                              @RequestParam("y") int y) {
        Result result = battleService.hit(opponent, x, y);

        if (Objects.equals(result, Result.WIN))
            gameService.endGame(game, true, user);

        return result == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(result, HttpStatus.OK);
    }
}