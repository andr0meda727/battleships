package battleships.models;

import battleships.enums.attackResult;

public record AttackOutcome(int row, int column, attackResult result) { }

