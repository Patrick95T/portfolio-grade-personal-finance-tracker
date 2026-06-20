package za.co.patrick.finance.cashflow.application;

import java.util.List;
import java.util.UUID;
import za.co.patrick.finance.cashflow.domain.IncomeEntry;

public interface IncomeEntryRepository {

    IncomeEntry save(IncomeEntry incomeEntry);

    List<IncomeEntry> findByProfileId(UUID profileId);
}
