package battleships.models;

import battleships.interfaces.AiBot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

enum Mode {
    HUNT,
    TARGET;
}

enum Orientation {
    HORIZONTAL,
    VERTICAL,
    UNKNOWN;
}

public class AiMedium implements AiBot {
    ArrayList<Integer> currentHits = new ArrayList<>();
    ArrayList<Integer> potentialTargets = new ArrayList<>();
    Set<Integer> impossibleCoordinates = new HashSet<>();
    Mode mode;
    Orientation orientation;
//    Board board = new Board();

    public AiMedium() {
        this.mode = Mode.HUNT;
        this.orientation = Orientation.UNKNOWN;
    }

    @Override
    public void makeMove(int[][] playerBoard) {
        List<Integer> cords;
//        checkIfOrientationKnown();

        if (mode == Mode.HUNT) {
            cords = getCoordinatesHuntMode();
        } else if (mode == Mode.TARGET) {
            cords = getCoordinatesTargetMode();
        } else {
            System.out.println("Unknown mode");
        }

        // playerBoard.receiveAttack(crds)
    }

    private List<Integer> getCoordinatesHuntMode() {
        return generateRandomValidCoordinates();
    }

    private List<Integer> getCoordinatesTargetMode() {
        return getPotentialCoordinates();
    }

    private List<Integer> getPotentialCoordinates() {
        List<Integer> coordinates = new ArrayList<>();

        int row = (int) (potentialTargets.get(0) / 10);
        int column = (int) (potentialTargets.get(0) % 10);
        potentialTargets.remove(0);

        coordinates.add(row);
        coordinates.add(column);

        return coordinates;
    }

    private void updatePotentialTargets() {
        if (currentHits.size() == 2) {
            detectOrientation();
        }

        int recentlyHitTargetCoordinates = currentHits.get(currentHits.size() - 1);

        if (this.orientation == Orientation.HORIZONTAL) {
            addPotentialHorizontalTargets(recentlyHitTargetCoordinates);
        } else if (this.orientation == Orientation.VERTICAL) {
            addPotentialVerticalTargets(recentlyHitTargetCoordinates);
        } else if (this.orientation == Orientation.UNKNOWN) {
            addPotentialHorizontalTargets(recentlyHitTargetCoordinates);
            addPotentialVerticalTargets(recentlyHitTargetCoordinates);
        }
    }

    private void addPotentialVerticalTargets(int recentlyHitTargetCoordinates) {
        switch((int) (recentlyHitTargetCoordinates / 10)) {
            case 0:
                potentialTargets.add(recentlyHitTargetCoordinates + 10);
                break;
            case 9:
                potentialTargets.add(recentlyHitTargetCoordinates - 10);
                break;
            default:
                potentialTargets.add(recentlyHitTargetCoordinates - 10);
                potentialTargets.add(recentlyHitTargetCoordinates + 10);
        }
    }

    private void addPotentialHorizontalTargets(int recentlyHitTargetCoordinates) {
        switch(recentlyHitTargetCoordinates % 10) {
            case 0:
                potentialTargets.add(recentlyHitTargetCoordinates + 1);
                break;
            case 9:
                potentialTargets.add(recentlyHitTargetCoordinates - 1);
                break;
            default:
                potentialTargets.add(recentlyHitTargetCoordinates - 1);
                potentialTargets.add(recentlyHitTargetCoordinates + 1);
        }
    }

    private void detectOrientation() {
        if (Math.abs(currentHits.get(0) - currentHits.get(1)) == 10) {
            this.orientation = Orientation.VERTICAL;
        } else if (Math.abs(currentHits.get(0) - currentHits.get(1)) == 1) {
            this.orientation = Orientation.HORIZONTAL;
        } else {
            System.out.println("Shouldnt happen");
        }
    }

    private List<Integer> generateRandomValidCoordinates() {
        List<Integer> coordinates = new ArrayList<>();

        int row = (int) (Math.random() * 10);
        int column = (int) (Math.random() * 10);

        while (currentHits.contains(row * 10 + column) || impossibleCoordinates.contains(row * 10 + column)) {
            row = (int) (Math.random() * 10);
            column = (int) (Math.random() * 10);
        }
        currentHits.add(row * 10 + column);

        coordinates.add(row);
        coordinates.add(column);

        return coordinates;
    }
}
