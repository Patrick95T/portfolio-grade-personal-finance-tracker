package za.co.patrick.finance.goals.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.patrick.finance.goals.application.CreateSavingsGoalCommand;
import za.co.patrick.finance.goals.application.SavingsGoalService;
import za.co.patrick.finance.goals.domain.SavingsGoal;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/goals")
@Tag(name = "Goals", description = "Manage savings goals and project completion timing")
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping
    @Operation(summary = "Create a savings goal")
    public ResponseEntity<SavingsGoalResponse> createSavingsGoal(@PathVariable UUID profileId, @Valid @RequestBody CreateSavingsGoalRequest request) {
        var goal = savingsGoalService.createSavingsGoal(new CreateSavingsGoalCommand(
                profileId, request.goalName(), request.goalType(), request.targetAmount(), request.currentSaved(), request.targetDate()
        ));
        return ResponseEntity.created(URI.create("/api/v1/profiles/" + profileId + "/goals/" + goal.id())).body(toResponse(goal));
    }

    @GetMapping
    @Operation(summary = "List savings goals for a profile")
    public List<SavingsGoalResponse> listSavingsGoals(@PathVariable UUID profileId) {
        return savingsGoalService.listSavingsGoals(profileId).stream().map(SavingsGoalController::toResponse).toList();
    }

    @GetMapping("/{goalId}/projection")
    @Operation(summary = "Project when a savings goal will be completed")
    public SavingsGoalProjectionResponse projectGoal(
            @PathVariable UUID profileId,
            @PathVariable UUID goalId,
            @RequestParam(required = false) java.math.BigDecimal monthlyContribution
    ) {
        var projection = savingsGoalService.projectGoal(profileId, goalId, monthlyContribution);
        return new SavingsGoalProjectionResponse(
                projection.goalId(),
                projection.profileId(),
                projection.goalName(),
                projection.currentSaved(),
                projection.targetAmount(),
                projection.remainingGap(),
                projection.monthlyContribution(),
                projection.requiredMonthlyContribution(),
                projection.projectedMonthsToGoal(),
                projection.projectedCompletionMonth(),
                projection.onTrackForTargetDate()
        );
    }

    private static SavingsGoalResponse toResponse(SavingsGoal goal) {
        return new SavingsGoalResponse(goal.id(), goal.profileId(), goal.goalName(), goal.goalType(),
                goal.targetAmount().amount(), goal.currentSaved().amount(), goal.targetDate(), goal.createdAt(), goal.updatedAt());
    }
}
