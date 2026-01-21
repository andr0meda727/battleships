package battleships.models;

import battleships.enums.AttackResult;
import battleships.enums.Orientation;
import battleships.factories.ShipFactory;
import battleships.config.GameConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {
    private final int boardSize = GameConfig.getInstance().getBoardSize();
//    private final List<Integer> shipLengths = Arrays.asList(2, 3, 3, 4, 5);

    public Cell[][] board;
    private int shipsSunk;
    private final List<Ship> ships;
    private final Random random;

    public Board() {
        this.board = new Cell[boardSize][boardSize];
        this.ships = new ArrayList<>();
        this.random = new Random();
        initializeBoard();
        placeShips();
    }

    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    private void placeShips() {
        ships.clear();
        shipsSunk = 0;

        Ship[] fleet = ShipFactory.createFleet();

        for (Ship ship : fleet) {
            ships.add(ship);
            int length = ship.getLength();

            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < 1000) {
                int row = random.nextInt(boardSize);
                int col = random.nextInt(boardSize);
                Orientation orientation = random.nextBoolean() ?
                        Orientation.HORIZONTAL : Orientation.VERTICAL;

                if (canPlaceShip(row, col, length, orientation)) {
                    placeShipOnBoard(ship, row, col, length, orientation);
                    placed = true;
                }
                attempts++;
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            if (col + length > boardSize) return false;

            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + length; c++) {
                    if (r >= 0 && r < boardSize && c >= 0 && c < boardSize) {
                        if (board[r][c].hasShip()) return false;
                    }
                }
            }
        } else {
            if (row + length > boardSize) return false;

            for (int r = row - 1; r <= row + length; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    if (r >= 0 && r < boardSize && c >= 0 && c < boardSize) {
                        if (board[r][c].hasShip()) return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeShipOnBoard(Ship ship, int row, int col, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            for (int i = 0; i < length; i++) {
                board[row][col + i].setShip(ship);
            }
        } else {
            for (int i = 0; i < length; i++) {
                board[row + i][col].setShip(ship);
            }
        }
    }

    public AttackResult receiveAttack(int row, int col) {
        Cell cell = board[row][col];

        if (cell.wasShot()) {
            return AttackResult.ALREADY_SHOT;
        }

        cell.shoot();

        if (cell.hasShip()) {
            Ship ship = cell.getShip();
            ship.hit(); // State Pattern - zmienia stan

            if (ship.isSunk()) {
                shipsSunk++;
                return AttackResult.SUNK;
            }
            return AttackResult.HIT;
        }

        return AttackResult.MISS;
    }

    public boolean isGameOver() {
        return shipsSunk == ships.size();
    }

    public List<Integer> getRemainingShipsLengths() {
        List<Integer> remaining = new ArrayList<>();
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                remaining.add(ship.getLength());
            }
        }
        return remaining;
    }

    public void resetBoard() {
        initializeBoard();
        placeShips();
    }

    public int getBoardSize() { return boardSize; }
    public List<Ship> getShips() { return new ArrayList<>(ships); }
}