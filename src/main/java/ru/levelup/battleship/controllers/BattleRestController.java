package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;
import ru.levelup.battleship.services.BattleService;
import ru.levelup.battleship.services.GameService;
import ru.levelup.battleship.services.UserService;

import javax.websocket.server.PathParam;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class BattleRestController {

    private BattleService battleService;
    private GameService gameService;
    private UserService userService;

    @GetMapping("api/{game_id}/hit")
    public ResponseEntity<Result> hitOpponent(@PathParam("game_id") Long gameId,
                                              @RequestParam("x") int x,
                                              @RequestParam("y") int y,
                                              Authentication authentication) {
        Game game = gameService.findGameById(gameId);
        User user = userService.findByLogin(authentication.getName());
        Result result = battleService.hit(user, x, y); //TODO: change user to opponent

        if (Objects.equals(result, Result.WIN))
            gameService.endGame(game, true, user);

        return result == null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(result, HttpStatus.OK);
    }
}