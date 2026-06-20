package za.co.patrick.finance.cashflow.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.cashflow.application.ExpenseEntryRepository;
import za.co.patrick.finance.cashflow.domain.ExpenseEntry;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class ExpenseEntryPersistenceAdapter implements ExpenseEntryRepository {

    private final ExpenseEntryJpaRepository expenseEntryJpaRepository;

    public ExpenseEntryPersistenceAdapter(ExpenseEntryJpaRepository expenseEntryJpaRepository) {
        this.expenseEntryJpaRepository = expenseEntryJpaRepository;
    }

    @Override
    public ExpenseEntry save(ExpenseEntry expenseEntry) {
        return toDomain(expenseEntryJpaRepository.save(toEntity(expenseEntry)));
    }

    @Override
    public List<ExpenseEntry> findByProfileId(UUID profileId) {
        return expenseEntryJpaRepository.findByProfileIdOrderByStartDateAsc(profileId)
                .stream()
                .map(ExpenseEntryPersistenceAdapter::toDomain)
                .toList();
    }

    @Override
    public List<ExpenseEntry> findByProfileIdAndMonth(UUID profileId, LocalDate monthStart, LocalDate monthEnd) {
        return expenseEntryJpaRepository.findByProfileIdAndStartDateBetweenOrderByStartDateAsc(profileId, monthStart, monthEnd)
                .stream()
                .map(ExpenseEntryPersistenceAdapter::toDomain)
                .toList();
    }

    private static ExpenseEntryEntity toEntity(ExpenseEntry expenseEntry) {
        ExpenseEntryEntity entity = new ExpenseEntryEntity();
        entity.setId(expenseEntry.id());
        entity.setProfileId(expenseEntry.profileId());
        entity.setExpenseName(expenseEntry.expenseName());
        entity.setAmount(expenseEntry.amount().amount());
        entity.setCurrency(expenseEntry.amount().currency());
        entity.setCategory(expenseEntry.category());
        entity.setFrequency(expenseEntry.frequency());
        entity.setStartDate(expenseEntry.startDate());
        entity.setEndDate(expenseEntry.endDate());
        entity.setEssential(expenseEntry.essential());
        entity.setCreatedAt(expenseEntry.createdAt());
        entity.setUpdatedAt(expenseEntry.updatedAt());
        return entity;
    }

    private static ExpenseEntry toDomain(ExpenseEntryEntity entity) {
        return new ExpenseEntry(
                entity.getId(),
                entity.getProfileId(),
                entity.getExpenseName(),
                new Money(entity.getAmount(), entity.getCurrency()),
                entity.getCategory(),
                entity.getFrequency(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isEssential(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
