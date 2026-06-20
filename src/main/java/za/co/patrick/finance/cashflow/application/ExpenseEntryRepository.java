package za.co.patrick.finance.cashflow.application;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import za.co.patrick.finance.cashflow.domain.ExpenseEntry;

public interface ExpenseEntryRepository {

    ExpenseEntry save(ExpenseEntry expenseEntry);

    List<ExpenseEntry> findByProfileId(UUID profileId);

    List<ExpenseEntry> findByProfileIdAndMonth(UUID profileId, LocalDate monthStart, LocalDate monthEnd);
}
