package battleships.utils;

import battleships.managers.GameStateManager;
import battleships.models.bots.BotPlayer;
import battleships.strategies.ProbabilityAttackStrategy;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HeatmapVisualizer {

    public void updateHeatmap(GridPane playerGrid, GameStateManager gameManager) {
        if (!gameManager.isGameStarted() || gameManager.isGameEnded()) {
            return;
        }

        BotPlayer botPlayer = gameManager.getAiPlayer();
        if (!(botPlayer.getStrategy() instanceof ProbabilityAttackStrategy)) {
            return;
        }

        ProbabilityAttackStrategy strategy = (ProbabilityAttackStrategy) botPlayer.getStrategy();
        int[][] probabilities = strategy.calculateProbabilities(gameManager.getPlayer().getBoard());

        int maxProb = 0;
        for (int[] row : probabilities) {
            for (int prob : row) {
                if (prob > maxProb) maxProb = prob;
            }
        }

        if (maxProb == 0) return;

        int finalMaxProb = maxProb;
        playerGrid.getChildren().forEach(node -> {
            if (node instanceof Rectangle rect) {
                Integer row = GridPane.getRowIndex(rect);
                Integer col = GridPane.getColumnIndex(rect);

                if (row != null && col != null) {
                    int prob = probabilities[row][col];
                    double intensity = (double) prob / finalMaxProb;

                    Color currentColor = (Color) rect.getFill();
                    if (!currentColor.equals(Color.RED) &&
                            !currentColor.equals(Color.BLUE) &&
                            !currentColor.equals(Color.DARKRED)) {

                        Color heatColor = Color.color(1.0, 1.0 - intensity * 0.5, 0.0, 0.3);
                        rect.setFill(heatColor);
                        Tooltip.install(rect, new Tooltip("Prawdopodobieństwo: " + prob));
                    }
                }
            }
        });
    }

    public void clearHeatmap(GridPane grid) {
        grid.getChildren().forEach(node -> {
            if (node instanceof Rectangle rect) {
                Color currentColor = (Color) rect.getFill();

                if (!currentColor.equals(Color.RED) &&
                        !currentColor.equals(Color.BLUE) &&
                        !currentColor.equals(Color.DARKRED) &&
                        !currentColor.equals(Color.HOTPINK)) {
                    rect.setFill(Color.LIGHTGRAY);
                }
                Tooltip.uninstall(rect, null);
            }
        });
    }
}