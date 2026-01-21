package battleships.builders;

import battleships.enums.Difficulty;
import battleships.observers.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class GameSetup {
    private final Difficulty difficulty;
    private final boolean heatmapEnabled;
    private final List<GameObserver> observers;

    GameSetup(Difficulty difficulty, boolean heatmapEnabled, List<GameObserver> observers) {
        this.difficulty = difficulty;
        this.heatmapEnabled = heatmapEnabled;
        this.observers = new ArrayList<>(observers);
    }

    public Difficulty getDifficulty() { return difficulty; }
    public boolean isHeatmapEnabled() { return heatmapEnabled; }
    public List<GameObserver> getObservers() { return new ArrayList<>(observers); }
}

