package battleships.builders;

import battleships.enums.Difficulty;
import battleships.observers.GameObserver;
import battleships.observers.StatisticsObserver;

import java.util.ArrayList;
import java.util.List;

public class GameSetupBuilder {
    private Difficulty difficulty = Difficulty.MEDIUM;
    private boolean enableStatistics = true;
    private boolean enableHeatmap = false;
    private final List<GameObserver> observers = new ArrayList<>();

    public GameSetupBuilder withDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public GameSetupBuilder withStatistics(boolean enable) {
        this.enableStatistics = enable;
        return this;
    }

    public GameSetupBuilder withHeatmap(boolean enable) {
        this.enableHeatmap = enable;
        return this;
    }

    public GameSetupBuilder addObserver(GameObserver observer) {
        this.observers.add(observer);
        return this;
    }

    public GameSetup build() {
        if (enableStatistics && observers.stream().noneMatch(o -> o instanceof StatisticsObserver)) {
            observers.add(new StatisticsObserver());
        }
        return new GameSetup(difficulty, enableHeatmap, observers);
    }
}
