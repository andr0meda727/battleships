package battleships.models.bots;

import battleships.enums.Mode;
import battleships.enums.Orientation;
import battleships.enums.attackResult;
import battleships.interfaces.AiBot;
import battleships.models.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AiMedium implements AiBot {
    ArrayList<List<Integer>> currentHits = new ArrayList<>(); // Traces the coordinates of the last hits to detect orientation
    ArrayList<Integer> potentialTargets = new ArrayList<>();
    Set<Integer> impossibleCoordinates = new HashSet<>();
    Mode mode;
    Orientation orientation;

    public AiMedium() {
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

    private void attackPotentialCoordinates(Board playerBoard) {
        List<Integer> coordinates = new ArrayList<>();

        int row = (int) (potentialTargets.get(0) / 10);
        int column = (int) (potentialTargets.get(0) % 10);
        potentialTargets.remove(0);

        coordinates.add(row);
        coordinates.add(column);
    }

    private void updatePotentialTargets() {
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

    private void detectAndChangeOrientation() {
        int firstHitCoords = currentHits.get(0).get(0) * 10 + currentHits.get(0).get(1);
        int secondHitCoords = currentHits.get(1).get(0) * 10 + currentHits.get(1).get(1);

        if (Math.abs(firstHitCoords - secondHitCoords) == 10) {
            this.orientation = Orientation.VERTICAL;
        } else if (Math.abs(firstHitCoords - secondHitCoords) == 1) {
            this.orientation = Orientation.HORIZONTAL;
        } else {
            System.out.println("Shouldnt happen");
        }
    }

    private void attackRandomValidCoordinates(Board playerBoard) {
        List<Integer> coordinates = new ArrayList<>();

        int row = (int) (Math.random() * 10);
        int column = (int) (Math.random() * 10);

        attackResult shootingResult  = playerBoard.receiveAttack(row, column);
        while (shootingResult == attackResult.ALREADY_SHOT) {
            row = (int) (Math.random() * 10);
            column = (int) (Math.random() * 10);
            shootingResult = playerBoard.receiveAttack(row, column);
        }

        coordinates.add(row);
        coordinates.add(column);

        if (shootingResult == attackResult.HIT) {
            currentHits.add(coordinates);
        } else if (shootingResult == attackResult.SUNK) {
            mode = Mode.HUNT;
            orientation = Orientation.UNKNOWN;
            currentHits.clear();
        }

        if (currentHits.size() == 2) {
            detectAndChangeOrientation();
        }
    }
}
