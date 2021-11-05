package ru.levelup.battleship.process;

import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipsArrange {

    private final int[][] tempBoard;
    private final Random random;
    private final List<Ship> ships;

    public ShipsArrange() {
        this.tempBoard = new int[10][10];
        this.random = new Random();
        this.ships = new ArrayList<>();
    }

    public List<Ship> getArrangedShips() {
        arrange();

        return ships.size() < 10 ? getArrangedShips() : ships;
    }

    private void arrange() {
        int x, y, kx, ky;
        boolean success;
        int boardSize = tempBoard.length;

        for (int n = 3; n >= 0; n--) {
            for (int m = 0; m <= 3 - n; m++) {
                do {
                    x = random.nextInt(boardSize);
                    y = random.nextInt(boardSize);
                    kx = random.nextInt(2);
                    if (kx == 0)
                        ky = 1;
                    else
                        ky = 0;
                    success = true;
                    for (int j = 0; j <= n; j++) {
                        if (!(isPossibleToArrange(x + kx * j, y + ky * j))) {
                            success = false;
                            break;
                        }
                    }
                    if (success) {
                        List<Cell> cells = new ArrayList<>();
                        for (int k = 0; k <= n; k++) {
                            int coordinateX = x + kx * k;
                            int coordinateY = y + ky * k;
                            tempBoard[coordinateX][coordinateY] = 1;
                            cells.add(new Cell(coordinateX, coordinateY));
                        }
                        ships.add(new Ship(cells));
                    }
                }
                while (!(success));
            }
        }
    }

    private boolean isPossibleToArrange(int x, int y) {
        int dx, dy;

        if ((x >= 0) & (x < 10) & (y >= 0) & (y < 10) && ((tempBoard[x][y] == 0) || (tempBoard[x][y] == 2))) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    dx = x + i;
                    dy = y + j;
                    if ((dx >= 0) & (dx < 10) & (dy >= 0) & (dy < 10) && (tempBoard[dx][dy] == 1))
                        return false;
                }
            }
            return true;
        } else
            return false;
    }
}