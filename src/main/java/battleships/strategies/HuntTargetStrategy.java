package battleships.strategies;

import battleships.enums.AttackResult;
import battleships.enums.Mode;
import battleships.enums.Orientation;
import battleships.models.AttackOutcome;
import battleships.models.Board;
import battleships.models.Coordinate;

import java.util.*;

public class HuntTargetStrategy implements AttackStrategy {
    private final Random random;
    private Mode mode;
    private Orientation orientation;
    private final List<Coordinate> currentHits;
    private final List<Coordinate> potentialTargets;
    private final Set<Coordinate> excludedCoordinates;

    public HuntTargetStrategy() {
        this.random = new Random();
        this.mode = Mode.HUNT;
        this.orientation = Orientation.UNKNOWN;
        this.currentHits = new ArrayList<>();
        this.potentialTargets = new ArrayList<>();
        this.excludedCoordinates = new HashSet<>();
    }

    @Override
    public AttackOutcome executeAttack(Board playerBoard) {
        return mode == Mode.HUNT ? executeHuntMode(playerBoard) : executeTargetMode(playerBoard);
    }

    private AttackOutcome executeHuntMode(Board playerBoard) {
        Coordinate target = getRandomValidCoordinate(playerBoard);
        AttackResult result = playerBoard.receiveAttack(target.row(), target.column());

        return handleAttackResult(target, result, playerBoard);
    }

    private AttackOutcome executeTargetMode(Board playerBoard) {
        Coordinate target = getNextPotentialTarget(playerBoard);
        AttackResult result = playerBoard.receiveAttack(target.row(), target.column());

        return handleAttackResult(target, result, playerBoard);
    }

    private AttackOutcome handleAttackResult(Coordinate target, AttackResult result, Board playerBoard) {
        switch (result) {
            case HIT -> handleHit(target);
            case SUNK -> handleSunk(playerBoard, target);
            case MISS -> handleMiss();
        }

        return new AttackOutcome(target.row(), target.column(), result);
    }

    private void handleHit(Coordinate target) {
        mode = Mode.TARGET;
        currentHits.add(target);
        updatePotentialTargets(target);
    }

    private void handleSunk(Board playerBoard, Coordinate target) {
        currentHits.add(target);
        excludeAdjacentCoordinates(currentHits);
        reset();
    }

    private void handleMiss() {
        // W trybie TARGET kontynuujemy z pozostałymi celami
        if (mode == Mode.TARGET && potentialTargets.isEmpty()) {
            mode = Mode.HUNT;
            orientation = Orientation.UNKNOWN;
            currentHits.clear();
        }
    }

    private Coordinate getRandomValidCoordinate(Board playerBoard) {
        int boardSize = playerBoard.getBoardSize();
        Coordinate coordinate;

        do {
            int row = random.nextInt(boardSize);
            int column = random.nextInt(boardSize);
            coordinate = new Coordinate(row, column);
        } while (excludedCoordinates.contains(coordinate) ||
                playerBoard.board[coordinate.row()][coordinate.column()].wasShot());

        return coordinate;
    }

    private Coordinate getNextPotentialTarget(Board playerBoard) {
        Coordinate target;

        do {
            if (potentialTargets.isEmpty()) {
                return getRandomValidCoordinate(playerBoard);
            }
            target = potentialTargets.remove(0);
        } while (excludedCoordinates.contains(target) ||
                playerBoard.board[target.row()][target.column()].wasShot());

        return target;
    }

    private void updatePotentialTargets(Coordinate lastHit) {
        detectOrientation();

        if (orientation == Orientation.UNKNOWN) {
            addAdjacentTargets(lastHit);
        } else if (orientation == Orientation.HORIZONTAL) {
            addHorizontalTargets(lastHit);
        } else {
            addVerticalTargets(lastHit);
        }

        filterPotentialTargets();
    }

    private void detectOrientation() {
        if (currentHits.size() >= 2) {
            Coordinate first = currentHits.get(0);
            Coordinate second = currentHits.get(1);

            if (first.column() == second.column()) {
                orientation = Orientation.VERTICAL;
            } else if (first.row() == second.row()) {
                orientation = Orientation.HORIZONTAL;
            }
        }
    }

    private void addAdjacentTargets(Coordinate coord) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int r = coord.row() + dir[0];
            int c = coord.column() + dir[1];

            if (Coordinate.isValid(r, c)) {
                potentialTargets.add(new Coordinate(r, c));
            }
        }
    }

    private void addHorizontalTargets(Coordinate coord) {
        potentialTargets.add(new Coordinate(coord.row(), coord.column() - 1));
        potentialTargets.add(new Coordinate(coord.row(), coord.column() + 1));
    }

    private void addVerticalTargets(Coordinate coord) {
        potentialTargets.add(new Coordinate(coord.row() - 1, coord.column()));
        potentialTargets.add(new Coordinate(coord.row() + 1, coord.column()));
    }

    private void filterPotentialTargets() {
        if (orientation == Orientation.UNKNOWN) return;

        Coordinate reference = currentHits.get(0);
        potentialTargets.removeIf(target -> {
            if (orientation == Orientation.HORIZONTAL) {
                return target.row() != reference.row();
            } else {
                return target.column() != reference.column();
            }
        });
    }

    private void excludeAdjacentCoordinates(List<Coordinate> shipCoordinates) {
        for (Coordinate coord : shipCoordinates) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int r = coord.row() + dr;
                    int c = coord.column() + dc;

                    if (Coordinate.isValid(r, c)) {
                        excludedCoordinates.add(new Coordinate(r, c));
                    }
                }
            }
        }
    }

    @Override
    public void reset() {
        mode = Mode.HUNT;
        orientation = Orientation.UNKNOWN;
        currentHits.clear();
        potentialTargets.clear();
        excludedCoordinates.clear();
    }
}