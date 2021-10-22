package ru.levelup.battleship.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.levelup.battleship.model.Game;
import ru.levelup.battleship.model.User;
import ru.levelup.battleship.process.Result;
import ru.levelup.battleship.services.BattleService;
import ru.levelup.battleship.services.GameService;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class BattleController {

    private BattleService battleService;
    private GameService gameService;

    @GetMapping("app/game/hit")
    public String hitOpponent(Game game, User user, User opponent, @RequestParam("x") int x, @RequestParam("y") int y) {
        String result = battleService.hit(opponent, x, y);

        if (Objects.equals(result, Result.WIN.name()))
            gameService.endGame(game, true, user);

        return null;
    }
}
