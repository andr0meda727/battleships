package battleships.models;

import battleships.enums.AttackResult;

public record AttackOutcome(int row, int column, AttackResult result) { }

