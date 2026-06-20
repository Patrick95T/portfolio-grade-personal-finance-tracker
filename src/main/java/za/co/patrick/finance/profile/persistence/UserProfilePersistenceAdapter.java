package za.co.patrick.finance.profile.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import za.co.patrick.finance.profile.application.UserProfileRepository;
import za.co.patrick.finance.profile.domain.UserProfile;
import za.co.patrick.finance.shared.domain.Money;

@Repository
public class UserProfilePersistenceAdapter implements UserProfileRepository {

    private final UserProfileJpaRepository userProfileJpaRepository;

    public UserProfilePersistenceAdapter(UserProfileJpaRepository userProfileJpaRepository) {
        this.userProfileJpaRepository = userProfileJpaRepository;
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return toDomain(userProfileJpaRepository.save(toEntity(userProfile)));
    }

    @Override
    public Optional<UserProfile> findById(UUID profileId) {
        return userProfileJpaRepository.findById(profileId).map(UserProfilePersistenceAdapter::toDomain);
    }

    @Override
    public List<UserProfile> findAll() {
        return userProfileJpaRepository.findAll()
                .stream()
                .map(UserProfilePersistenceAdapter::toDomain)
                .toList();
    }

    private static UserProfileEntity toEntity(UserProfile userProfile) {
        UserProfileEntity entity = new UserProfileEntity();
        entity.setId(userProfile.id());
        entity.setFullName(userProfile.fullName());
        entity.setEmailAddress(userProfile.emailAddress());
        entity.setEmploymentType(userProfile.employmentType());
        entity.setPreferredCurrency(userProfile.preferredCurrency());
        entity.setCountryCode(userProfile.countryCode());
        entity.setMonthlyNetIncome(userProfile.monthlyNetIncome().amount());
        entity.setMonthlyHousingCost(userProfile.monthlyHousingCost().amount());
        entity.setMonthlyEssentialCosts(userProfile.monthlyEssentialCosts().amount());
        entity.setOnboardingDate(userProfile.onboardingDate());
        entity.setCreatedAt(userProfile.createdAt());
        entity.setUpdatedAt(userProfile.updatedAt());
        return entity;
    }

    private static UserProfile toDomain(UserProfileEntity entity) {
        return new UserProfile(
                entity.getId(),
                entity.getFullName(),
                entity.getEmailAddress(),
                entity.getEmploymentType(),
                entity.getPreferredCurrency(),
                entity.getCountryCode(),
                new Money(entity.getMonthlyNetIncome(), entity.getPreferredCurrency()),
                new Money(entity.getMonthlyHousingCost(), entity.getPreferredCurrency()),
                new Money(entity.getMonthlyEssentialCosts(), entity.getPreferredCurrency()),
                entity.getOnboardingDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
