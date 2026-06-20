package za.co.patrick.finance.debt.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import za.co.patrick.finance.debt.domain.DebtAccount;

public interface DebtAccountRepository {
    DebtAccount save(DebtAccount debtAccount);
    List<DebtAccount> findByProfileId(UUID profileId);
    Optional<DebtAccount> findById(UUID debtAccountId);
}
