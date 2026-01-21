package battleships.strategies;

import battleships.enums.AttackResult;
import battleships.enums.Orientation;
import battleships.models.AttackOutcome;
import battleships.models.Board;
import battleships.models.Coordinate;
import battleships.models.Ship;

import java.util.*;


public class ProbabilityAttackStrategy implements AttackStrategy {
    private static final int ADJACENT_HIT_BONUS = 1000;

    private final Random random;
    private final List<Coordinate> currentHits;
    private final Set<Coordinate> excludedCoordinates;
    private final Set<Coordinate> processedSunkCells;
    private Orientation detectedOrientation;

    public ProbabilityAttackStrategy() {
        this.random = new Random();
        this.currentHits = new ArrayList<>();
        this.excludedCoordinates = new HashSet<>();
        this.processedSunkCells = new HashSet<>();
        this.detectedOrientation = Orientation.UNKNOWN;
    }

    @Override
    public AttackOutcome executeAttack(Board playerBoard) {
        updateExcludedCells(playerBoard);

        int[][] probabilities = calculateProbabilities(playerBoard);
        Coordinate target = selectBestTarget(playerBoard, probabilities);
        AttackResult result = playerBoard.receiveAttack(target.row(), target.column());

        handleAttackResult(target, result, playerBoard);

        return new AttackOutcome(target.row(), target.column(), result);
    }

    public int[][] calculateProbabilities(Board playerBoard) {
        updateExcludedCells(playerBoard);

        int boardSize = playerBoard.getBoardSize();
        int[][] probabilities = new int[boardSize][boardSize];

        calculateBaseProbabilities(playerBoard, probabilities);
        applyAdjacentHitBonus(playerBoard, probabilities);

        return probabilities;
    }

    private void calculateBaseProbabilities(Board playerBoard, int[][] probabilities) {
        int boardSize = playerBoard.getBoardSize();
        List<Integer> remainingShips = playerBoard.getRemainingShipsLengths();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (isValidTarget(playerBoard, row, col)) {
                    for (int shipLength : remainingShips) {
                        probabilities[row][col] += countPossiblePlacements(playerBoard, row, col, shipLength);
                    }
                }
            }
        }
    }

    private int countPossiblePlacements(Board playerBoard, int row, int col, int shipLength) {
        int count = 0;

        // Poziome umieszczenia
        for (int startCol = Math.max(0, col - shipLength + 1); startCol <= col; startCol++) {
            if (canPlaceHorizontally(playerBoard, row, startCol, shipLength)) {
                count++;
            }
        }

        // Pionowe umieszczenia
        for (int startRow = Math.max(0, row - shipLength + 1); startRow <= row; startRow++) {
            if (canPlaceVertically(playerBoard, startRow, col, shipLength)) {
                count++;
            }
        }

        return count;
    }

    private boolean canPlaceHorizontally(Board playerBoard, int row, int startCol, int length) {
        int boardSize = playerBoard.getBoardSize();
        if (startCol + length > boardSize) return false;

        for (int col = startCol; col < startCol + length; col++) {
            if (!isValidTarget(playerBoard, row, col)) {
                return false;
            }
        }
        return true;
    }

    private boolean canPlaceVertically(Board playerBoard, int startRow, int col, int length) {
        int boardSize = playerBoard.getBoardSize();
        if (startRow + length > boardSize) return false;

        for (int row = startRow; row < startRow + length; row++) {
            if (!isValidTarget(playerBoard, row, col)) {
                return false;
            }
        }
        return true;
    }

    private void applyAdjacentHitBonus(Board playerBoard, int[][] probabilities) {
        detectShipOrientation();

        for (Coordinate hit : currentHits) {
            addBonusToAdjacentCells(playerBoard, hit, probabilities);
        }
    }

    private void detectShipOrientation() {
        if (currentHits.size() >= 2) {
            Coordinate first = currentHits.get(0);
            Coordinate second = currentHits.get(1);

            if (first.column() == second.column()) {
                detectedOrientation = Orientation.VERTICAL;
            } else if (first.row() == second.row()) {
                detectedOrientation = Orientation.HORIZONTAL;
            }
        } else {
            detectedOrientation = Orientation.UNKNOWN;
        }
    }

    private void addBonusToAdjacentCells(Board playerBoard, Coordinate hit, int[][] probabilities) {
        int[][] directions = getDirectionsForOrientation();
        int boardSize = playerBoard.getBoardSize();

        for (int[] dir : directions) {
            int newRow = hit.row() + dir[0];
            int newCol = hit.column() + dir[1];

            if (newRow >= 0 && newRow < boardSize && newCol >= 0 && newCol < boardSize) {
                if (isValidTarget(playerBoard, newRow, newCol)) {
                    probabilities[newRow][newCol] += ADJACENT_HIT_BONUS;
                }
            }
        }
    }

    private int[][] getDirectionsForOrientation() {
        return switch (detectedOrientation) {
            case HORIZONTAL -> new int[][]{{0, -1}, {0, 1}};
            case VERTICAL -> new int[][]{{-1, 0}, {1, 0}};
            case UNKNOWN -> new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        };
    }

    private Coordinate selectBestTarget(Board playerBoard, int[][] probabilities) {
        List<Coordinate> candidates = new ArrayList<>();
        int maxProb = -1;
        int boardSize = playerBoard.getBoardSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (!isValidTarget(playerBoard, row, col)) continue;

                if (probabilities[row][col] > maxProb) {
                    maxProb = probabilities[row][col];
                    candidates.clear();
                    candidates.add(new Coordinate(row, col));
                } else if (probabilities[row][col] == maxProb) {
                    candidates.add(new Coordinate(row, col));
                }
            }
        }

        return candidates.get(random.nextInt(candidates.size()));
    }

    private boolean isValidTarget(Board playerBoard, int row, int col) {
        return !playerBoard.board[row][col].wasShot() &&
                !excludedCoordinates.contains(new Coordinate(row, col));
    }

    private void handleAttackResult(Coordinate target, AttackResult result, Board playerBoard) {
        switch (result) {
            case HIT -> currentHits.add(target);
            case SUNK -> {
                currentHits.add(target);
                excludeAroundSunkShip(playerBoard, target);
                currentHits.clear();
                detectedOrientation = Orientation.UNKNOWN;
            }
        }
    }

    private void updateExcludedCells(Board playerBoard) {
        int boardSize = playerBoard.getBoardSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Coordinate coord = new Coordinate(row, col);
                if (isSunk(playerBoard, row, col) && !processedSunkCells.contains(coord)) {
                    excludeAroundSunkShip(playerBoard, coord);
                }
            }
        }
    }

    private void excludeAroundSunkShip(Board playerBoard, Coordinate coord) {
        Ship ship = playerBoard.board[coord.row()][coord.column()].getShip();
        if (ship == null || !ship.isSunk()) return;

        int boardSize = playerBoard.getBoardSize();
        List<Coordinate> shipCells = new ArrayList<>();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (playerBoard.board[row][col].getShip() == ship) {
                    Coordinate shipCell = new Coordinate(row, col);
                    shipCells.add(shipCell);
                    processedSunkCells.add(shipCell);
                }
            }
        }

        for (Coordinate cell : shipCells) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int r = cell.row() + dr;
                    int c = cell.column() + dc;
                    if (Coordinate.isValid(r, c)) {
                        excludedCoordinates.add(new Coordinate(r, c));
                    }
                }
            }
        }
    }

    private boolean isSunk(Board playerBoard, int row, int col) {
        return playerBoard.board[row][col].hasShip() &&
                playerBoard.board[row][col].getShip().isSunk();
    }

    @Override
    public void reset() {
        currentHits.clear();
        excludedCoordinates.clear();
        processedSunkCells.clear();
        detectedOrientation = Orientation.UNKNOWN;
    }
}