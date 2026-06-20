package za.co.patrick.finance.profile.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import za.co.patrick.finance.profile.domain.UserProfile;

public interface UserProfileRepository {

    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findById(UUID profileId);

    List<UserProfile> findAll();
}
