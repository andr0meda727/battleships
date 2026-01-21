package battleships.enums;

public enum AttackResult {
    MISS("Pudło"),
    HIT("Trafienie"),
    SUNK("Zatopiony"),
    ALREADY_SHOT("Już strzelano");

    private final String description;

    AttackResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}