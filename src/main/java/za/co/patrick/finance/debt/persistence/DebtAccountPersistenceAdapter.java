package za.co.patrick.finance.debt.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.debt.application.DebtAccountRepository;
import za.co.patrick.finance.debt.domain.DebtAccount;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class DebtAccountPersistenceAdapter implements DebtAccountRepository {
    private final DebtAccountJpaRepository debtAccountJpaRepository;

    public DebtAccountPersistenceAdapter(DebtAccountJpaRepository debtAccountJpaRepository) {
        this.debtAccountJpaRepository = debtAccountJpaRepository;
    }

    @Override
    public DebtAccount save(DebtAccount debtAccount) {
        return toDomain(debtAccountJpaRepository.save(toEntity(debtAccount)));
    }

    @Override
    public List<DebtAccount> findByProfileId(UUID profileId) {
        return debtAccountJpaRepository.findByProfileIdOrderByCreatedAtAsc(profileId).stream().map(DebtAccountPersistenceAdapter::toDomain).toList();
    }

    @Override
    public Optional<DebtAccount> findById(UUID debtAccountId) {
        return debtAccountJpaRepository.findById(debtAccountId).map(DebtAccountPersistenceAdapter::toDomain);
    }

    private static DebtAccountEntity toEntity(DebtAccount debtAccount) {
        var entity = new DebtAccountEntity();
        entity.setId(debtAccount.id());
        entity.setProfileId(debtAccount.profileId());
        entity.setLenderName(debtAccount.lenderName());
        entity.setDebtType(debtAccount.debtType());
        entity.setCurrentBalance(debtAccount.currentBalance().amount());
        entity.setMinimumMonthlyPayment(debtAccount.minimumMonthlyPayment().amount());
        entity.setAnnualInterestRate(debtAccount.annualInterestRate());
        entity.setCurrency(debtAccount.currentBalance().currency());
        entity.setCreatedAt(debtAccount.createdAt());
        entity.setUpdatedAt(debtAccount.updatedAt());
        return entity;
    }

    private static DebtAccount toDomain(DebtAccountEntity entity) {
        return new DebtAccount(
                entity.getId(), entity.getProfileId(), entity.getLenderName(), entity.getDebtType(),
                new Money(entity.getCurrentBalance(), entity.getCurrency()),
                new Money(entity.getMinimumMonthlyPayment(), entity.getCurrency()),
                entity.getAnnualInterestRate(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}
