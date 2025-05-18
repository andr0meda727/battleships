package battleships.models.bots;

import battleships.interfaces.AiBot;
import battleships.models.Board;

import java.util.List;

public class AiHard implements AiBot {
    private static final int ADJACENT_HIT_BONUS = 1000;

    @Override
    public void makeMove(Board playerBoard) {
        int boardSize = playerBoard.getBoardSize();
        int[][] probabilities = new int[boardSize][boardSize];

        calculateBaseProbabilities(playerBoard, probabilities);
        addAdjacentHitBonus(playerBoard, probabilities);

        List<Integer> coordinates = findMaxProbabilityCell(playerBoard, probabilities);
        playerBoard.receiveAttack(coordinates.get(0), coordinates.get(1));
    }

    private void calculateBaseProbabilities(Board playerBoard, int[][] probabilities) {
        // Calculates probability for each cell
    }

    private void addAdjacentHitBonus(Board playerBoard, int[][] probabilities) {
        // Works like TARGET mode, it adds "bonus" to probability for cells that are adjacent to last successful hit
    }

    private List<Integer> findMaxProbabilityCell(Board playerBoard, int[][] probabilities) {
        // It returns coordinates of the cell that have the max probability based on the probabilities array
    }
}
