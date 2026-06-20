package za.co.patrick.finance.profile.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.patrick.finance.profile.application.CreateUserProfileCommand;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.profile.domain.UserProfile;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "Create and inspect financial profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    @Operation(summary = "Create a financial profile")
    public ResponseEntity<UserProfileResponse> createProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        var createdProfile = userProfileService.createProfile(new CreateUserProfileCommand(
                request.fullName(),
                request.emailAddress(),
                request.employmentType(),
                request.preferredCurrency(),
                request.countryCode(),
                request.monthlyNetIncome(),
                request.monthlyHousingCost(),
                request.monthlyEssentialCosts(),
                request.onboardingDate()
        ));
        return ResponseEntity
                .created(URI.create("/api/v1/profiles/" + createdProfile.id()))
                .body(toResponse(createdProfile));
    }

    @GetMapping("/{profileId}")
    @Operation(summary = "Get a profile by id")
    public UserProfileResponse getProfile(@PathVariable UUID profileId) {
        return toResponse(userProfileService.getProfile(profileId));
    }

    @GetMapping
    @Operation(summary = "List all profiles")
    public List<UserProfileResponse> listProfiles() {
        return userProfileService.listProfiles()
                .stream()
                .map(UserProfileController::toResponse)
                .toList();
    }

    private static UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.id(),
                profile.fullName(),
                profile.emailAddress(),
                profile.employmentType(),
                profile.preferredCurrency(),
                profile.countryCode(),
                profile.monthlyNetIncome().amount(),
                profile.monthlyHousingCost().amount(),
                profile.monthlyEssentialCosts().amount(),
                profile.monthlyAvailableForPlanning().amount(),
                profile.onboardingDate(),
                profile.createdAt(),
                profile.updatedAt()
        );
    }
}
