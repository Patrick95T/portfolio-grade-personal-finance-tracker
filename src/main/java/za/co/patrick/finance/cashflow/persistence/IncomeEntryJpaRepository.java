package za.co.patrick.finance.cashflow.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface IncomeEntryJpaRepository extends JpaRepository<IncomeEntryEntity, UUID> {

    List<IncomeEntryEntity> findByProfileIdOrderByStartDateAsc(UUID profileId);
}
