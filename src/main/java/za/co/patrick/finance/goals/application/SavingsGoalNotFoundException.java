package za.co.patrick.finance.goals.application;

import java.util.UUID;

public class SavingsGoalNotFoundException extends RuntimeException {
    public SavingsGoalNotFoundException(UUID goalId) {
        super("Savings goal with id " + goalId + " was not found");
    }
}
