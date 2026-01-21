package battleships.controllers;

import battleships.builders.GameSetup;
import battleships.builders.GameSetupBuilder;
import battleships.commands.AttackCommand;
import battleships.enums.AttackResult;
import battleships.managers.GameStateManager;
import battleships.models.AttackOutcome;
import battleships.models.Board;
import battleships.models.Coordinate;
import battleships.observers.StatisticsObserver;
import battleships.utils.HeatmapVisualizer;
import battleships.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class MainMenuController {
    private static final int SIZE = 10;
    private static final int CELL_SIZE = 40;

    @FXML private GridPane playerGrid;
    @FXML private GridPane enemyGrid;
    @FXML private VBox difficultyBox;
    @FXML private VBox placementBox;
    @FXML private VBox statsBox;
    @FXML private DifficultyController difficultyBoxController;
    @FXML private Button startButton;
    @FXML private CheckBox showHeatmapCheckbox;
    @FXML private Label statsLabel;
    private ButtonController buttonController;

    private GameStateManager gameManager;
    private HeatmapVisualizer heatmapVisualizer;
    private StatisticsObserver statisticsObserver;

    @FXML
    public void initialize() throws IOException {
        // Builder Pattern - tworzenie setup'u
        GameSetup setup = new GameSetupBuilder()
                .withStatistics(true)
                .build();

        this.gameManager = new GameStateManager();
        this.heatmapVisualizer = new HeatmapVisualizer();

        // Observer Pattern - dodaj obserwatorów
        setup.getObservers().forEach(gameManager::addObserver);
        this.statisticsObserver = gameManager.getObservers().stream()
                .filter(o -> o instanceof StatisticsObserver)
                .map(o -> (StatisticsObserver) o)
                .findFirst()
                .orElse(new StatisticsObserver());

        // WAŻNE: Przekaż GameStateManager do DifficultyController
        difficultyBoxController.setGameManager(gameManager);

        createGrid(playerGrid, GridType.PLAYER);
        createGrid(enemyGrid, GridType.ENEMY);

        setupPlacementControls();
        setupStartButton();
        setupHeatmapToggle();
        setupStatsDisplay();

        updateUI();
    }

    private void setupPlacementControls() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/battleships/views/placement-view.fxml"));
        Node placementNode = loader.load();
        placementBox.getChildren().add(placementNode);

        ButtonController buttonController = loader.getController();
        buttonController.initialize(gameManager, playerGrid, enemyGrid,
                difficultyBoxController.getChosenLabel(), this::resetUI);
    }

    private void setupStartButton() {
        startButton.setOnAction(event -> {
            if (!gameManager.isGameStarted()) {
                try {
                    gameManager.startGame(gameManager.getChosenDifficulty());
                    startButton.setText("Gra w toku...");
                    startButton.setDisable(true);
                    updateHeatmapVisibility();
                } catch (IllegalStateException e) {
                    UIUtils.showConfirmationDialog("Błąd", e.getMessage());
                }
            }
        });
    }

    private void setupHeatmapToggle() {
        if (showHeatmapCheckbox == null) {
            showHeatmapCheckbox = new CheckBox("Pokaż heatmapę AI (Hard)");
        }
        showHeatmapCheckbox.setSelected(false);
        showHeatmapCheckbox.setOnAction(event -> updateHeatmapVisibility());

        if (statsBox != null && !statsBox.getChildren().contains(showHeatmapCheckbox)) {
            statsBox.getChildren().add(showHeatmapCheckbox);
        }
    }

    private void setupStatsDisplay() {
        statsLabel = new Label("Statystyki:\n-");
        statsLabel.setStyle("-fx-text-fill: #4cc9f0; -fx-font-size: 13px; -fx-font-weight: bold;");
        if (statsBox != null) {
            statsBox.getChildren().add(statsLabel);
        }
    }

    private void updateHeatmapVisibility() {
        if (showHeatmapCheckbox.isSelected() && gameManager.isGameStarted()) {
            heatmapVisualizer.updateHeatmap(playerGrid, gameManager);
        } else {
            // Dodano gameManager jako drugi parametr
            heatmapVisualizer.clearHeatmap(playerGrid, gameManager);
            UIUtils.colorGrid(playerGrid, gameManager.getPlayer().getBoard().board);
        }
    }

    private void updateStats() {
        if (statisticsObserver != null) {
            statsLabel.setText(String.format(
                    "STATYSTYKI BITWY\n" +
                            "─────────────────\n" +
                            "CELNOŚĆ:\n" +
                            "  Gracz: %.1f%%\n" +
                            "  AI:    %.1f%%\n" +
                            "ZATOPIONE:\n" +
                            "  Twoje: %d\n" +
                            "  Wroga: %d",
                    statisticsObserver.getPlayerAccuracy(),
                    statisticsObserver.getAiAccuracy(),
                    // Zakładając dodanie getterów do StatisticsObserver lub użycie pól
                    gameManager.getPlayer().getBoard().getShips().stream().filter(s -> s.isSunk()).count(),
                    gameManager.getAiPlayer() != null ?
                            gameManager.getAiPlayer().getBoard().getShips().stream().filter(s -> s.isSunk()).count() : 0
            ));
        }
    }

    private void createGrid(GridPane grid, GridType type) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTGRAY);
                cell.setStroke(Color.BLACK);

                if (type == GridType.ENEMY) {
                    int finalRow = row;
                    int finalCol = col;
                    cell.setOnMouseClicked(event ->
                            handlePlayerAttack(cell, new Coordinate(finalRow, finalCol)));
                }

                grid.add(cell, col, row);
            }
        }
    }

    private void handlePlayerAttack(Rectangle cell, Coordinate target) {
        // Command Pattern - enkapsulacja ataku
        AttackCommand command = new AttackCommand(gameManager, target);

        if (command.execute()) {
            AttackOutcome outcome = command.getOutcome();
            UIUtils.colorPlayerAttack(cell, outcome.result());

            updateStats();

            if (gameManager.getAiPlayer().getBoard().isGameOver()) {
                gameManager.endGame(true);
                showEndGameDialog(true);
            } else {
                gameManager.switchTurn();
                executeAiTurn();
            }
        }
    }

    private void executeAiTurn() {
        // Małe opóźnienie dla realizmu
        new Thread(() -> {
            try {
                Thread.sleep(battleships.config.GameConfig.getInstance().getAiThinkingDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            javafx.application.Platform.runLater(() -> {
                AttackOutcome outcome = gameManager.getAiPlayer()
                        .makeMove(gameManager.getPlayer().getBoard());

                gameManager.notifyAiAttack(outcome);
                UIUtils.colorAiAttack(playerGrid, outcome);

                if (showHeatmapCheckbox.isSelected()) {
                    heatmapVisualizer.updateHeatmap(playerGrid, gameManager);
                }

                updateStats();

                if (gameManager.getPlayer().getBoard().isGameOver()) {
                    gameManager.endGame(false);
                    showEndGameDialog(false);
                } else {
                    gameManager.switchTurn();
                }
            });
        }).start();
    }

    private void showEndGameDialog(boolean playerWon) {
        String message = playerWon ? "Gratulacje! Wygrałeś!" : "Niestety przegrałeś!";
        String fullMessage = message + "\n\n" + statisticsObserver.getReport();
        UIUtils.showEndGamePopup(fullMessage, gameManager);
    }

    public void resetUI() {
        startButton.setDisable(false);
        startButton.setText("ROZPOCZNIJ BITWĘ");
        updateStats();
        heatmapVisualizer.clearHeatmap(playerGrid, gameManager);
    }

    private void updateUI() {
        Board playerBoard = gameManager.getPlayer().getBoard();
        UIUtils.colorGrid(playerGrid, playerBoard.board);
    }

    private enum GridType {
        PLAYER, ENEMY
    }
}