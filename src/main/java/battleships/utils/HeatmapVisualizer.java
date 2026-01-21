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
            if (node instanceof Rectangle rect) {
                Integer row = GridPane.getRowIndex(rect);
                Integer col = GridPane.getColumnIndex(rect);

                if (row != null && col != null) {
                    int prob = probabilities[row][col];
                    boolean hasShip = gameManager.getPlayer().getBoard().board[row][col].hasShip();

                    if (prob == 0) {
                        resetToBaseline(rect, hasShip);
                        return;
                    }

                    double intensity = (double) prob / finalMaxProb;

                    if (isAttackable(rect)) {
                        // 1. BAZOWY KOLOR HEATMAPY (od niebieskiego do czerwono-pomarańczowego)
                        // Zwiększyłem lekko bazową przezroczystość (0.7), żeby kolory były żywsze
                        Color heatColor = Color.color(1.0, 0.9 - (intensity * 0.75), 0.1, 0.7);

                        if (hasShip) {
                            // === JEŚLI JEST STATEK ===

                            // A) Wypełnienie: Używamy koloru heatmapy, ale nakładamy na niego
                            // BARDZO subtelny zielony filtr (opacity 0.15).
                            // Dzięki temu "gorące" pola nadal są czerwone, ale mają leciutki zielony odcień.
                            Color subtleGreenTint = Color.web("#00ff00", 0.15);
                            rect.setFill(heatColor.interpolate(subtleGreenTint, 0.3));

                            // B) Obramowanie: To jest główny wskaźnik. Neonowa zieleń.
                            rect.setStroke(Color.web("#39ff14")); // Neon Green
                            rect.setStrokeWidth(2.0); // Grubsza ramka dla statków

                            // Opcjonalnie: zmiana kursora, by wskazać, że tu jest coś ważnego
                            rect.setCursor(javafx.scene.Cursor.CROSSHAIR);
                        } else {
                            // === ZWYKŁE POLE ===
                            rect.setFill(heatColor);
                            rect.setStroke(Color.web("#16213e")); // Standardowa ciemna ramka
                            rect.setStrokeWidth(0.5); // Cienka ramka
                            rect.setCursor(javafx.scene.Cursor.DEFAULT);
                        }
                        Tooltip.install(rect, new Tooltip("Prawdopodobieństwo ataku: " + prob));
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