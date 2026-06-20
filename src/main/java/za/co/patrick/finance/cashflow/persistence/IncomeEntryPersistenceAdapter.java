package za.co.patrick.finance.cashflow.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.cashflow.application.IncomeEntryRepository;
import za.co.patrick.finance.cashflow.domain.IncomeEntry;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class IncomeEntryPersistenceAdapter implements IncomeEntryRepository {

    private final IncomeEntryJpaRepository incomeEntryJpaRepository;

    public IncomeEntryPersistenceAdapter(IncomeEntryJpaRepository incomeEntryJpaRepository) {
        this.incomeEntryJpaRepository = incomeEntryJpaRepository;
    }

    @Override
    public IncomeEntry save(IncomeEntry incomeEntry) {
        return toDomain(incomeEntryJpaRepository.save(toEntity(incomeEntry)));
    }

    @Override
    public List<IncomeEntry> findByProfileId(UUID profileId) {
        return incomeEntryJpaRepository.findByProfileIdOrderByStartDateAsc(profileId)
                .stream()
                .map(IncomeEntryPersistenceAdapter::toDomain)
                .toList();
    }

    private static IncomeEntryEntity toEntity(IncomeEntry incomeEntry) {
        IncomeEntryEntity entity = new IncomeEntryEntity();
        entity.setId(incomeEntry.id());
        entity.setProfileId(incomeEntry.profileId());
        entity.setSourceName(incomeEntry.sourceName());
        entity.setAmount(incomeEntry.amount().amount());
        entity.setCurrency(incomeEntry.amount().currency());
        entity.setFrequency(incomeEntry.frequency());
        entity.setStartDate(incomeEntry.startDate());
        entity.setEndDate(incomeEntry.endDate());
        entity.setTaxable(incomeEntry.taxable());
        entity.setCreatedAt(incomeEntry.createdAt());
        entity.setUpdatedAt(incomeEntry.updatedAt());
        return entity;
    }

    private static IncomeEntry toDomain(IncomeEntryEntity entity) {
        return new IncomeEntry(
                entity.getId(),
                entity.getProfileId(),
                entity.getSourceName(),
                new Money(entity.getAmount(), entity.getCurrency()),
                entity.getFrequency(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isTaxable(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
