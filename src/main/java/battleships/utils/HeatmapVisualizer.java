package battleships.utils;

import battleships.managers.GameStateManager;
import battleships.models.bots.BotPlayer;
import battleships.strategies.ProbabilityAttackStrategy;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HeatmapVisualizer {

    public void updateHeatmap(GridPane playerGrid, GameStateManager gameManager) {
        if (!gameManager.isGameStarted() || gameManager.isGameEnded()) return;

        BotPlayer botPlayer = gameManager.getAiPlayer();
        if (!(botPlayer.getStrategy() instanceof ProbabilityAttackStrategy strategy)) return;

        int[][] probabilities = strategy.calculateProbabilities(gameManager.getPlayer().getBoard());
        int maxProb = 0;
        for (int[] row : probabilities) {
            for (int prob : row) if (prob > maxProb) maxProb = prob;
        }

        if (maxProb == 0) return;

        int finalMaxProb = maxProb;

        playerGrid.getChildren().forEach(node -> {
            if (node instanceof StackPane pane) {
                Rectangle rect = (Rectangle) pane.getChildren().get(0);
                Label mark = (Label) pane.getChildren().get(1);

                Integer row = GridPane.getRowIndex(pane);
                Integer col = GridPane.getColumnIndex(pane);

                if (row != null && col != null) {
                    int prob = probabilities[row][col];
                    boolean hasShip = gameManager.getPlayer().getBoard().board[row][col].hasShip();

                    if (prob == 0) {
                        // Reset do stanu "bez heatmapy"
                        if (mark.getText().isEmpty()) { // Tylko jeśli nie ma X/kropki
                            rect.setFill(UIUtils.COLOR_WATER);
                        }
                        return;
                    }

                    double intensity = (double) prob / finalMaxProb;

                    // Sprawdzamy czy pole nie jest już trafione (brak X lub kropki)
                    if (mark.getText().isEmpty()) {
                        // Kolorujemy tylko tło prostokąta
                        Color heatColor = Color.color(1.0, 0.9 - (intensity * 0.75), 0.1, 0.6);
                        rect.setFill(heatColor);

                        // Jeśli jest tam statek, upewniamy się że stroke jest zielony i gruby
                        if (hasShip) {
                            rect.setStroke(Color.web("#2ecc71"));
                            rect.setStrokeWidth(3.0);
                        }
                    }
                }
            }
        });
    }


    private boolean isAttackable(Rectangle rect) {
        Color c = (Color) rect.getFill();
        // Sprawdzamy, czy to nie jest kolor trafienia/pudła (z UIUtils)
        return !c.toString().equals(UIUtils.COLOR_HIT.toString()) &&
                !c.toString().equals(UIUtils.COLOR_MISS.toString()) &&
                !c.toString().equals(UIUtils.COLOR_SUNK.toString());
    }

    private void resetToBaseline(Rectangle rect, boolean hasShip) {
        if (isAttackable(rect)) {
            if (hasShip) {
                // Subtelny zielony, gdy heatmapa nie działa na tym polu
                rect.setFill(Color.web("#2ecc71", 0.5));
                rect.setStroke(Color.web("#2ecc71"));
                rect.setStrokeWidth(1.5);
            } else {
                // Standardowa woda
                rect.setFill(Color.web("#2c3e50"));
                rect.setStroke(Color.web("#16213e"));
                rect.setStrokeWidth(0.5);
            }
        }
        rect.setCursor(javafx.scene.Cursor.DEFAULT);
        Tooltip.uninstall(rect, null);
    }

    public void clearHeatmap(GridPane grid, GameStateManager gameManager) {
        grid.getChildren().forEach(node -> {
            if (node instanceof Rectangle rect) {
                Integer row = GridPane.getRowIndex(rect);
                Integer col = GridPane.getColumnIndex(rect);
                if (row != null && col != null) {
                    boolean hasShip = gameManager.getPlayer().getBoard().board[row][col].hasShip();
                    resetToBaseline(rect, hasShip);
                }
            }
        });
    }
}