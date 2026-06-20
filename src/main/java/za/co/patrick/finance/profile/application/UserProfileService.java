package za.co.patrick.finance.profile.application;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.profile.domain.UserProfile;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public UserProfileService(
            UserProfileRepository userProfileRepository,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.userProfileRepository = userProfileRepository;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public UserProfile createProfile(CreateUserProfileCommand command) {
        Instant now = Instant.now(clock);
        UserProfile profile = new UserProfile(
                UUID.randomUUID(),
                command.fullName().trim(),
                command.emailAddress().trim().toLowerCase(),
                command.employmentType(),
                command.preferredCurrency().trim().toUpperCase(),
                command.countryCode().trim().toUpperCase(),
                new Money(command.monthlyNetIncome(), command.preferredCurrency()),
                new Money(command.monthlyHousingCost(), command.preferredCurrency()),
                new Money(command.monthlyEssentialCosts(), command.preferredCurrency()),
                command.onboardingDate(),
                now,
                now
        );
        var savedProfile = userProfileRepository.save(profile);
        auditEventService.record(
                savedProfile.id(),
                AuditEventType.PROFILE_CREATED,
                "UserProfile",
                savedProfile.id(),
                "Created profile for " + savedProfile.fullName()
        );
        return savedProfile;
    }

    @Transactional(readOnly = true)
    public UserProfile getProfile(UUID profileId) {
        return userProfileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));
    }

    @Transactional(readOnly = true)
    public List<UserProfile> listProfiles() {
        return userProfileRepository.findAll();
    }
}
