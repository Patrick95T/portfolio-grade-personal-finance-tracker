package za.co.patrick.finance.cashflow.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ExpenseEntryJpaRepository extends JpaRepository<ExpenseEntryEntity, UUID> {

    List<ExpenseEntryEntity> findByProfileIdOrderByStartDateAsc(UUID profileId);

    List<ExpenseEntryEntity> findByProfileIdAndStartDateBetweenOrderByStartDateAsc(
            UUID profileId,
            LocalDate monthStart,
            LocalDate monthEnd
    );
}
