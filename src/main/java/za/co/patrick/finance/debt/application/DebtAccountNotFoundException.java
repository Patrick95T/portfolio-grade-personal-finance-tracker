package za.co.patrick.finance.debt.application;

import java.util.UUID;

public class DebtAccountNotFoundException extends RuntimeException {
    public DebtAccountNotFoundException(UUID debtAccountId) {
        super("Debt account with id " + debtAccountId + " was not found");
    }
}
