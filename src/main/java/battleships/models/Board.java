package battleships.models;

import battleships.enums.Orientation;
import battleships.enums.attackResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 10;
    final List<Integer> shipLengths = new ArrayList<>(Arrays.asList(2, 3, 3, 4, 5));
    private int shipsSunk;
    public Cell[][] board;

    public void resetSunk(){
        shipsSunk = 0;
    }
    public Board() {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Cell();
            }
        }
        placeShips();
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    private void placeShips() {
        for (int length : shipLengths) {
            boolean placed = false;
            while (!placed) {
                int row = (int) (Math.random() * BOARD_SIZE);
                int col = (int) (Math.random() * BOARD_SIZE);
                Orientation orientation = ((int) ((Math.random() * 10) % 2)) == 0 ? Orientation.VERTICAL : Orientation.HORIZONTAL;

                if (canPlaceShip(row, col, length, orientation)) {
                    Ship ship = new Ship(length);
                    placeShipOnBoard(ship, row, col, length, orientation);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int length, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            if (col + length > BOARD_SIZE) return false;

            for (int i = row - 1; i <= row + 1; i++) {
                if (i < 0 || i >= BOARD_SIZE) continue;
                for (int j = col - 1; j <= col + length; j++) {
                    if (j < 0 || j >= BOARD_SIZE) continue;
                    if (board[i][j].hasShip()) return false;
                }
            }
        } else {
            if (row + length > BOARD_SIZE) return false;

            for (int i = row - 1; i <= row + length; i++) {
                if (i < 0 || i >= BOARD_SIZE) continue;
                for (int j = col - 1; j <= col + 1; j++) {
                    if (j < 0 || j >= BOARD_SIZE) continue;
                    if (board[i][j].hasShip()) return false;
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

    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell();
            }
        }
        placeShips();
    }

    public attackResult receiveAttack(int row, int col) {
        Cell cell = board[row][col];

        if (cell.wasShot()) {
            return attackResult.ALREADY_SHOT;
        }

        cell.shoot();

        if (cell.hasShip()) {
            Ship ship = cell.getShip();
            ship.hit();

            if (ship.isSunk()) {
                shipsSunk++;
                if (isGameOver()){
                    GameState.endGame();
                }
                return attackResult.SUNK;
            }
            return attackResult.HIT;
        }

        return attackResult.MISS;
    }

    public boolean isGameOver() {
        return shipsSunk == shipLengths.size();
    }

    public List<Integer> getRemainingShipsLengths() {
        List<Integer> remainingShips = new ArrayList<>();
        List<Ship> processedShips = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                Cell cell = board[row][column];

                if (cell.hasShip()) {
                    Ship ship = cell.getShip();

                    if (!processedShips.contains(ship) && !ship.isSunk()) {
                        remainingShips.add(ship.getLength());
                        processedShips.add(ship);
                    }
                }
            }
        }

        return remainingShips;
    }
}