package battleships.models.bots;

import battleships.enums.Orientation;
import battleships.enums.attackResult;
import battleships.interfaces.AiBot;
import battleships.models.AttackOutcome;
import battleships.models.Board;
import battleships.models.Cell;
import battleships.models.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AiHard implements AiBot {
    private static final int ADJACENT_HIT_BONUS = 1000;
    private boolean[][] excluded;
    private boolean[][] processedSunkCells;
    private final List<int[]> currentHits = new ArrayList<>();
    private final Board board;
    Orientation orientation;

    public AiHard() {
        this.board = new Board();
        this.orientation = Orientation.UNKNOWN;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public AttackOutcome makeMove(Board playerBoard) {
        int boardSize = playerBoard.getBoardSize();
        initializeArrays(boardSize);
        updateExcludedCells(playerBoard, boardSize);

        int[][] probabilities = new int[boardSize][boardSize];

        calculateBaseProbabilities(playerBoard, probabilities);
        addAdjacentHitBonus(playerBoard, probabilities);

        int[] coordinates = findMaxProbabilityCell(playerBoard, probabilities);
        attackResult result = playerBoard.receiveAttack(coordinates[0], coordinates[1]);

        if (result == attackResult.HIT) {
            currentHits.add(new int[]{coordinates[0], coordinates[1]});
        } else if (result == attackResult.SUNK) {
            currentHits.clear();
        }

        return new AttackOutcome(coordinates[0], coordinates[1], result);
    }

    private void initializeArrays(int boardSize) {
        if (excluded == null || excluded.length != boardSize) {
            excluded = new boolean[boardSize][boardSize];
            processedSunkCells = new boolean[boardSize][boardSize];
        }
    }

    private void updateExcludedCells(Board playerBoard, int boardSize) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (isSunk(playerBoard, row, col) && !processedSunkCells[row][col]) {
                    processNewSunkShip(playerBoard, row, col);
                }
            }
        }
    }

    private void processNewSunkShip(Board playerBoard, int row, int column) {
        Ship sunkShip = playerBoard.board[row][column].getShip();
        int boardSize = playerBoard.getBoardSize();
        if (sunkShip == null || !sunkShip.isSunk()) return;

        List<int[]> shipCells = new ArrayList<>();
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                Cell cell = playerBoard.board[r][c];
                if (cell.getShip() == sunkShip) {
                    shipCells.add(new int[]{r, c});
                    processedSunkCells[r][c] = true;
                }
            }
        }

        for (int[] cell : shipCells) {
            markAdjacentAsExcluded(playerBoard, cell[0], cell[1]);
        }
    }

    private void markAdjacentAsExcluded(Board playerBoard, int row, int column) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int adjacentRow = row + i;
                int adjacentColumn = column + j;
                if (adjacentRow >= 0 && adjacentRow < playerBoard.getBoardSize()
                && adjacentColumn >= 0 && adjacentColumn < playerBoard.getBoardSize()) {
                    excluded[adjacentRow][adjacentColumn] = true;
                }
            }
        }
    }

    private void calculateBaseProbabilities(Board playerBoard, int[][] probabilities) {
        int boardSize = playerBoard.getBoardSize();
        List<Integer> remainingShips = playerBoard.getRemainingShipsLengths();

        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (isAlreadyAttacked(playerBoard, row, column)) continue;

                for (int shipLength : remainingShips) {
                    checkHorizontalPlacements(playerBoard, row, column, shipLength, probabilities);
                    checkVerticalPlacements(playerBoard, row, column, shipLength, probabilities);
                }
            }
        }
    }

    private boolean isAlreadyAttacked(Board playerBoard, int row, int column) {
        return playerBoard.board[row][column].wasShot() || excluded[row][column];
    }

    private void checkHorizontalPlacements(Board playerBoard, int row, int column, int shipLength, int[][] probabilities) {
        int startColumn = Math.max(0, column - shipLength + 1);
        int endColumn = column;

        for (int i = startColumn; i <= endColumn; i++) {
            if (i + shipLength - 1 >= playerBoard.getBoardSize()) continue;
            if (isValidHorizontalPlacement(playerBoard, row, i, shipLength)) {
                probabilities[row][column]++;
            }
        }
    }

    private boolean isValidHorizontalPlacement(Board playerBoard, int row, int startColumn, int shipLength) {
        for (int i = startColumn; i < startColumn + shipLength; i++) {
            if (i >= playerBoard.getBoardSize()) return false;

            if (isAlreadyAttacked(playerBoard, row, i)) {
                return false;
            }
        }
        return true;
    }

    private void checkVerticalPlacements(Board playerBoard, int row, int column, int shipLength, int[][] probabilities) {
        int startRow = Math.max(0, row - shipLength + 1);
        int endRow = row;

        for (int i = startRow; i <= endRow; i++) {
            if (i + shipLength - 1 >= playerBoard.getBoardSize()) continue;
            if (isValidVerticalPlacement(playerBoard, i, column, shipLength)) {
                probabilities[row][column]++;
            }
        }
    }

    private boolean isValidVerticalPlacement(Board playerBoard, int startRow, int column, int shipLength) {
        for (int i = startRow; i < startRow + shipLength; i++) {
            if (i >= playerBoard.getBoardSize()) return false;

            if (isAlreadyAttacked(playerBoard, i, column)) {
                return false;
            }
        }
        return true;
    }

    private void addAdjacentHitBonus(Board playerBoard, int[][] probabilities) {
        for (int[] hit : currentHits) {
            addBonusToAdjacentCells(playerBoard, hit[0], hit[1], probabilities);
        }
    }

    private void detectShipOrientation() {
        if (currentHits.size() >= 2) {
            int[] firstHitCoordinates = currentHits.get(0);
            int firstHitRow = firstHitCoordinates[0];
            int firstHitColumn = firstHitCoordinates[1];

            int[] secondHitCoordinates = currentHits.get(1);
            int secondHitRow = secondHitCoordinates[0];
            int secondHitColumn = secondHitCoordinates[1];

            if (firstHitColumn == secondHitColumn) {
                this.orientation = Orientation.VERTICAL;
            } else if (firstHitRow == secondHitRow) {
                this.orientation = Orientation.HORIZONTAL;
            } else {
                System.out.println("Shouldnt happen");
            }
        } else {
            this.orientation = Orientation.UNKNOWN;
        }
    }

    private void addBonusToAdjacentCells(Board playerBoard, int row, int column, int[][] probabilities) {
        detectShipOrientation();
        int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        switch (this.orientation) {
            case HORIZONTAL:
                directions = new int[][]{{0, -1}, {0, 1}};
                break;
            case VERTICAL:
                directions = new int[][]{{1, 0}, {-1, 0}};
                break;
            case UNKNOWN:
                break;
            default:
                System.out.println("Error in detecting orientation");
        }

        int boardSize = playerBoard.getBoardSize();

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            if (newRow >= 0 && newRow < boardSize && newColumn >= 0 && newColumn < boardSize) {
                if (!isAlreadyAttacked(playerBoard, newRow, newColumn)) {
                    probabilities[newRow][newColumn] += ADJACENT_HIT_BONUS;
                }
            }
        }
    }

    private int[] findMaxProbabilityCell(Board playerBoard, int[][] probabilities) {
        List<int[]> candidates = new ArrayList<>();
        int boardSize = playerBoard.getBoardSize();
        int maxProb = -1;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (isAlreadyAttacked(playerBoard, row, col)) continue;

                if (probabilities[row][col] > maxProb) {
                    maxProb = probabilities[row][col];
                    candidates.clear();
                    candidates.add(new int[]{row, col});
                } else if (probabilities[row][col] == maxProb) {
                    candidates.add(new int[]{row, col});
                }
            }
        }

        return candidates.get(new Random().nextInt(candidates.size()));
    }


   private boolean isSunk(Board playerBoard, int row, int column) {
        return playerBoard.board[row][column].hasShip() &&
                playerBoard.board[row][column].getShip().isSunk();
   }
}

