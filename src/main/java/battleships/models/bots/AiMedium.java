package battleships.models.bots;

import battleships.enums.Mode;
import battleships.enums.Orientation;
import battleships.enums.attackResult;
import battleships.interfaces.AiBot;
import battleships.models.Board;

import java.util.*;
import java.util.stream.Collectors;

public class AiMedium implements AiBot {
    List<List<Integer>> currentHits = new ArrayList<>(); // Traces the coordinates of the last hits to detect orientation
    List<List<Integer>> potentialTargets = new ArrayList<>();
    Set<Integer> impossibleCoordinates = new HashSet<>(); // Exclude adjacent coordinates after the ship was sunk
    Mode mode;
    Orientation orientation;

    private AiMedium() {
        this.mode = Mode.HUNT;
        this.orientation = Orientation.UNKNOWN;
    }

    @Override
    public void makeMove(Board playerBoard) {
        switch (mode) {
            case HUNT:
                attackHuntMode(playerBoard);
                break;
            case TARGET:
                attackTargetMode(playerBoard);
                break;
            default:
                System.out.println("Mode unknown");
        }
    }

    private void attackHuntMode(Board playerBoard) {
        attackRandomValidCoordinates(playerBoard);
    }

    private void attackTargetMode(Board playerBoard) {
        attackPotentialCoordinates(playerBoard);
    }

    private void addCoordinatesToImpossibleCoordinatesList() {
        for (List<Integer> shipCoordinates : currentHits) {
            int row = shipCoordinates.get(0);
            int column = shipCoordinates.get(1);

            Set<Integer> adjacentCoordinates = new HashSet<>();

            switch (column) {
                case 0 -> {
                    if (row == 0) {
                        adjacentCoordinates.add((row + 1) * 10);
                        adjacentCoordinates.add((row + 1) * 10 + 1);
                    } else if (row == 9) {
                        adjacentCoordinates.add((row - 1) * 10);
                        adjacentCoordinates.add((row - 1) * 10 + 1);
                    } else {
                        adjacentCoordinates.add((row + 1) * 10);
                        adjacentCoordinates.add((row + 1) * 10 + 1);
                        adjacentCoordinates.add((row - 1) * 10);
                        adjacentCoordinates.add((row - 1) * 10 + 1);
                    }
                    adjacentCoordinates.add(row * 10 + 1);
                }
                case 9 -> {
                    if (row == 0) {
                        adjacentCoordinates.add((row + 1) * 10 + column);
                        adjacentCoordinates.add((row + 1) * 10 + column - 1);
                    } else if (row == 9) {
                        adjacentCoordinates.add((row - 1) * 10 + column);
                        adjacentCoordinates.add((row - 1) * 10 + column - 1);
                    } else {
                        adjacentCoordinates.add((row + 1) * 10 + column);
                        adjacentCoordinates.add((row + 1) * 10 + column - 1);
                        adjacentCoordinates.add((row - 1) * 10 + column);
                        adjacentCoordinates.add((row - 1) * 10 + column - 1);
                    }
                    adjacentCoordinates.add(row * 10 + column - 1);
                }
                default -> { // Coordinate is not on the edges
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx != 0 || dy != 0) {
                                addIfValid(adjacentCoordinates, row + dy, column + dx);
                            }
                        }
                    }
                }
            }

            impossibleCoordinates.addAll(adjacentCoordinates);
        }
    }

    private void addIfValid(Set<Integer> set, int row, int column) {
        if (row >= 0 && row < 10 && column >= 0 && column < 10) {
            set.add(row * 10 + column);
        }
    }

    private void attackPotentialCoordinates(Board playerBoard) {
        List<Integer> coordinates = new ArrayList<>();
        attackResult shootingResult;

        int row, column;
        do {
            row = potentialTargets.get(0).get(0);
            column = potentialTargets.get(0).get(1);
            potentialTargets.remove(0);
        } while (impossibleCoordinates.contains(row * 10 + column));

        shootingResult = playerBoard.receiveAttack(row, column);

        while (shootingResult == attackResult.ALREADY_SHOT) {
            do {
                row = potentialTargets.get(0).get(0);
                column = potentialTargets.get(0).get(1);
                potentialTargets.remove(0);
            } while (impossibleCoordinates.contains(row * 10 + column));
            shootingResult = playerBoard.receiveAttack(row, column);
        }

        coordinates.add(row);
        coordinates.add(column);

        if (shootingResult == attackResult.HIT) {
            currentHits.add(coordinates);
            updatePotentialTargets();
        } else if (shootingResult == attackResult.SUNK) {
            mode = Mode.HUNT;
            orientation = Orientation.UNKNOWN;
            addCoordinatesToImpossibleCoordinatesList();
            currentHits.clear();
            potentialTargets.clear();
        }
    }

    private void updatePotentialTargets() {
        List<Integer> recentlyHitTargetCoordinates = currentHits.get(currentHits.size() - 1);
        detectAndChangeOrientation();

        if (this.orientation == Orientation.HORIZONTAL) {
            addPotentialHorizontalTargets(recentlyHitTargetCoordinates);
        } else if (this.orientation == Orientation.VERTICAL) {
            addPotentialVerticalTargets(recentlyHitTargetCoordinates);
        } else if (this.orientation == Orientation.UNKNOWN) {
            addPotentialHorizontalTargets(recentlyHitTargetCoordinates);
            addPotentialVerticalTargets(recentlyHitTargetCoordinates);
        }
    }

    private void addPotentialVerticalTargets(List<Integer> recentlyHitTargetCoordinates) {
        int row = recentlyHitTargetCoordinates.get(0);
        int column = recentlyHitTargetCoordinates.get(1);

        switch(row) {
            case 0:
                potentialTargets.add(Arrays.asList(row + 1, column));
                break;
            case 9:
                potentialTargets.add(Arrays.asList(row - 1, column));
                break;
            default:
                potentialTargets.add(Arrays.asList(row + 1, column));
                potentialTargets.add(Arrays.asList(row - 1, column));
        }
    }

    private void addPotentialHorizontalTargets(List<Integer> recentlyHitTargetCoordinates) {
        int row = recentlyHitTargetCoordinates.get(0);
        int column = recentlyHitTargetCoordinates.get(1);

        switch(column) {
            case 0:
                potentialTargets.add(Arrays.asList(row, column + 1));
                break;
            case 9:
                potentialTargets.add(Arrays.asList(row, column - 1));
                break;
            default:
                potentialTargets.add(Arrays.asList(row, column + 1));
                potentialTargets.add(Arrays.asList(row, column - 1));
        }
    }

    private void detectAndChangeOrientation() {
        if (currentHits.size() == 2) {
            List<Integer> firstHitCoordinates = currentHits.get(0);
            int firstHitRow = firstHitCoordinates.get(0);
            int firstHitColumn = firstHitCoordinates.get(1);

            List<Integer> secondHitCoordinates = currentHits.get(1);
            int secondHitRow = secondHitCoordinates.get(0);
            int secondHitColumn = secondHitCoordinates.get(1);

            if (firstHitColumn == secondHitColumn) {
                this.orientation = Orientation.VERTICAL;
            } else if (firstHitRow == secondHitRow) {
                this.orientation = Orientation.HORIZONTAL;
            } else {
                System.out.println("Shouldnt happen");
            }

            removeImpossibleCoordinatesBasedOnOrientation();
        }
    }

    private void removeImpossibleCoordinatesBasedOnOrientation() {
        List<Integer> firstHit = currentHits.get(0);
        int hitRow = firstHit.get(0);
        int hitColumn = firstHit.get(1);

        potentialTargets = potentialTargets.stream()
                .filter(target -> {
                    int targetRow = target.get(0);
                    int targetColumn = target.get(1);

                    if (orientation == Orientation.VERTICAL) {
                        return targetColumn == hitColumn;
                    } else if (orientation == Orientation.HORIZONTAL) {
                        return targetRow == hitRow;
                    }
                    return false;
                })
                .filter(target -> !impossibleCoordinates.contains(target.get(0) * 10 + target.get(1)))
                .collect(Collectors.toList());
    }

    private void attackRandomValidCoordinates(Board playerBoard) {
        List<Integer> coordinates = new ArrayList<>();
        attackResult shootingResult;

        int row, column;
        do {
            row = (int) (Math.random() * 10);
            column = (int) (Math.random() * 10);
        } while (impossibleCoordinates.contains(row * 10 + column));

        shootingResult = playerBoard.receiveAttack(row, column);

        while (shootingResult == attackResult.ALREADY_SHOT) {
            do {
                row = (int) (Math.random() * 10);
                column = (int) (Math.random() * 10);
            } while (impossibleCoordinates.contains(row * 10 + column));
            shootingResult = playerBoard.receiveAttack(row, column);
        }

        coordinates.add(row);
        coordinates.add(column);

        if (shootingResult == attackResult.HIT) {
            mode = Mode.TARGET;
            currentHits.add(coordinates);
            updatePotentialTargets();
        } else if (shootingResult == attackResult.SUNK) {
            mode = Mode.HUNT;
            orientation = Orientation.UNKNOWN;
            addCoordinatesToImpossibleCoordinatesList();
            currentHits.clear();
            potentialTargets.clear();
        }
    }
}
