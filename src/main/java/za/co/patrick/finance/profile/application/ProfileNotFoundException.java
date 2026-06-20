package za.co.patrick.finance.profile.application;

import java.util.UUID;

public final class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(UUID profileId) {
        super("Profile with id '%s' was not found".formatted(profileId));
    }
}
