package battleships.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private enum Direction {
        VERTICAL,
        HORIZONTAL
    }

    private static final int BOARD_SIZE = 10;
    private Cell[][] board;
    final List<Integer> shipLengths = new ArrayList<>(Arrays.asList(2, 3, 3, 4, 5));

    public Board() {
        board = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new Cell();
            }
        }
        placeShips();
    }

    private void placeShips() {
        for (int length : shipLengths) {
            boolean placed = false;
            while(!placed) {
                int row = (int) (Math.random() * BOARD_SIZE);
                int col = (int) (Math.random() * BOARD_SIZE);
                Direction direction = ((int) ((Math.random() * 10) % 2)) == 0 ? Direction.VERTICAL : Direction.HORIZONTAL;

                if(canPlaceShip(row, col, length, direction)) {
                    Ship ship = new Ship(length);
                    placeShipOnBoard(ship, row, col, length, direction);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int length, Direction direction) {
        if (direction == Direction.HORIZONTAL) {
            if (col + length > BOARD_SIZE) return false;

            for (int i = row - 1; i <= row + 1; i++) {
                if (i < 0 || i >= BOARD_SIZE) continue;
                for (int j = col - 1; j <= col + length; j++) {
                    if (j < 0 || j >= BOARD_SIZE) continue;
                    if (board[i][j].hasShip()) {
                        System.out.println("The ship is already here");
                        return false;
                    }
                }
            }
        }
        else {
            if (row + length > BOARD_SIZE) return false;

            for (int i = row - 1; i <= row + length; i++) {
                if (i < 0 || i >= BOARD_SIZE) continue;
                for (int j = col - 1; j <= col + 1; j++) {
                    if (j < 0 || j >= BOARD_SIZE) continue;
                    if (board[i][j].hasShip()) {
                        System.out.println("The ship is already here");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeShipOnBoard(Ship ship, int row, int col, int length, Direction direction) {
        if (direction == Direction.HORIZONTAL) {
            for (int i = 0; i < length; i++) {
                board[row][col + i].setShip(ship);
            }
        }
        else {
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
}