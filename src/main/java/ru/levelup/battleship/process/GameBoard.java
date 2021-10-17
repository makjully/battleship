package ru.levelup.battleship.process;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.levelup.battleship.model.Cell;
import ru.levelup.battleship.model.Ship;
import ru.levelup.battleship.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class GameBoard {
    @Getter
    private final User player;

    @Getter
    private final List<Ship> ships;

    private final int[][] tempBoard;

    public GameBoard(User player) {
        this.player = player;
        this.tempBoard = new int[10][10];
        this.ships = new ArrayList<>();
    }

    public void arrangeAllShips() {
        List<Integer> shipsDecks = new ArrayList<>(Arrays.asList(4, 3, 3, 2, 2, 2, 1, 1, 1, 1));
        shipsDecks.forEach(element -> ships.add(buildShip(element)));
    }

    private Ship buildShip(int shipSize) {
        int orientation = generateShipOrientation();

        int[][] shipCoordinates = generateShipsCoordinates(shipSize, orientation);

        while (!arrangeShip(shipCoordinates, shipSize, orientation)) {
            buildShip(shipSize);
        }

        ArrayList<Cell> shipCells = new ArrayList<>();
        for (int[] xy : shipCoordinates) {
            shipCells.add(new Cell(xy[0], xy[1]));
        }

        Ship ship = new Ship(player, shipCells);
        ship.setPlayer(player);

        return ship;
    }

    private int[][] generateShipsCoordinates(int shipSize, int orientation) {
        int[] startShip = generateStartShipCoordinate(shipSize, orientation);

        int startX = startShip[0];
        int startY = startShip[1];

        int[][] shipCoordinates = new int[shipSize][2];
        for (int[] coordinatePair : shipCoordinates) {
            coordinatePair[0] = startX;
            coordinatePair[1] = startY;
            if (orientation == 0)
                startY++;
            else
                startX++;
        }

        return shipCoordinates;
    }

    private int generateShipOrientation() {
        Random random = new Random();
        return random.nextInt(2) - 1; // 0 = horizontal, 1 = vertical
    }

    private int[] generateStartShipCoordinate(int shipSize, int orientation) {
        Random random = new Random();
        int[] xy = new int[2];

        if (orientation == 0) {
            xy[0] = random.nextInt(9);
            xy[1] = random.nextInt(10 - shipSize);
        } else {
            xy[0] = random.nextInt(10 - shipSize);
            xy[1] = random.nextInt(9);
        }

        return xy;
    }

    private boolean arrangeShip(int[][] shipCoordinates, int shipSize, int orientation) {
        if (!arrangementPossible(shipCoordinates, shipSize, orientation))
            return false;

        arrangeShipOnTempBoard(shipCoordinates, shipSize, orientation);

        return true;
    }

    private void arrangeShipOnTempBoard(int[][] shipCoordinates, int shipSize, int orientation) {
        for (int[] shipCoordinate : shipCoordinates)
            tempBoard[shipCoordinate[0]][shipCoordinate[1]] = 1;

        List<Integer[]> shipAureole = getShipAureole(shipCoordinates, shipSize, orientation);

        for (Integer[] shipAureoleCoordinate : shipAureole)
            tempBoard[shipAureoleCoordinate[0]][shipAureoleCoordinate[1]] = 0;
    }

    private boolean arrangementPossible(int[][] shipCoordinates, int shipSize, int orientation) {
        for (int[] shipCoordinate : shipCoordinates) {
            if (tempBoard[shipCoordinate[0]][shipCoordinate[1]] == 1)
                return false;
        }

        List<Integer[]> shipAureole = getShipAureole(shipCoordinates, shipSize, orientation);

        for (Integer[] shipAureoleCoordinate : shipAureole) {
            if (tempBoard[shipAureoleCoordinate[0]][shipAureoleCoordinate[1]] == 1)
                return false;
        }

        return true;
    }

    private List<Integer[]> getShipAureole(int[][] shipCoordinates, int shipSize, int orientation) {
        List<Integer[]> shipAureole = new ArrayList<>();

        if (orientation == 1) {

            if (shipCoordinates[0][1] + 1 <= 9) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0], shipCoordinate[1] + 1});

                if (shipCoordinates[0][0] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1] + 1});

                if (shipCoordinates[shipSize - 1][0] + 1 <= 9) {
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1,
                            shipCoordinates[shipSize - 1][1] + 1});
                }
            }

            if (shipCoordinates[0][1] - 1 >= 0) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0], shipCoordinate[1] - 1});

                if (shipCoordinates[0][0] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1] - 1});

                if (shipCoordinates[shipSize - 1][0] + 1 <= 9)
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1,
                            shipCoordinates[shipSize - 1][1] - 1});
            }

            if (shipCoordinates[0][0] - 1 >= 0)
                shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1]});

            if (shipCoordinates[shipSize - 1][0] + 1 <= 9)
                shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1, shipCoordinates[shipSize - 1][1]});

        } else {

            if (shipCoordinates[0][0] - 1 >= 0) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0] - 1, shipCoordinate[1]});

                if (shipCoordinates[shipSize - 1][1] + 1 <= 9)
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] - 1,
                            shipCoordinates[shipSize - 1][1] + 1});

                if (shipCoordinates[0][1] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] - 1, shipCoordinates[0][1] - 1});
            }

            if (shipCoordinates[0][0] + 1 <= 9) {
                for (int[] shipCoordinate : shipCoordinates)
                    shipAureole.add(new Integer[]{shipCoordinate[0] + 1, shipCoordinate[1]});

                if (shipCoordinates[0][1] - 1 >= 0)
                    shipAureole.add(new Integer[]{shipCoordinates[0][0] + 1, shipCoordinates[0][1] - 1});

                if (shipCoordinates[shipSize - 1][1] + 1 <= 9)
                    shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0] + 1,
                            shipCoordinates[shipSize - 1][1] + 1});
            }

            if (shipCoordinates[0][1] - 1 >= 0)
                shipAureole.add(new Integer[]{shipCoordinates[0][0], shipCoordinates[0][1] - 1});

            if (shipCoordinates[shipSize - 1][1] + 1 <= 9)
                shipAureole.add(new Integer[]{shipCoordinates[shipSize - 1][0], shipCoordinates[shipSize - 1][1] + 1});
        }

        return shipAureole;
    }
}