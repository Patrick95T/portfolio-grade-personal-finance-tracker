package za.co.patrick.finance.budget.api;

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
import za.co.patrick.finance.budget.application.BudgetPlanService;
import za.co.patrick.finance.budget.application.CreateBudgetPlanCommand;
import za.co.patrick.finance.budget.domain.BudgetPlan;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/budgets")
@Tag(name = "Budgets", description = "Plan monthly category targets and compare against actuals")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;

    public BudgetPlanController(BudgetPlanService budgetPlanService) {
        this.budgetPlanService = budgetPlanService;
    }

    @PostMapping
    @Operation(summary = "Create a monthly budget plan")
    public ResponseEntity<BudgetPlanResponse> createBudgetPlan(
            @PathVariable UUID profileId,
            @Valid @RequestBody CreateBudgetPlanRequest request
    ) {
        var budgetPlan = budgetPlanService.createBudgetPlan(new CreateBudgetPlanCommand(
                profileId,
                request.monthStart(),
                request.housingTarget(),
                request.essentialsTarget(),
                request.lifestyleTarget()
        ));
        return ResponseEntity
                .created(URI.create("/api/v1/profiles/" + profileId + "/budgets/" + budgetPlan.id()))
                .body(toResponse(budgetPlan));
    }

    @GetMapping
    @Operation(summary = "List budget plans for a profile")
    public List<BudgetPlanResponse> listBudgetPlans(@PathVariable UUID profileId) {
        return budgetPlanService.listBudgetPlans(profileId)
                .stream()
                .map(BudgetPlanController::toResponse)
                .toList();
    }

    @GetMapping("/{budgetPlanId}/summary")
    @Operation(summary = "Get actual-versus-target budget summary")
    public BudgetSummaryResponse getBudgetSummary(@PathVariable UUID budgetPlanId) {
        var summary = budgetPlanService.getBudgetSummary(budgetPlanId);
        return new BudgetSummaryResponse(
                summary.budgetPlanId(),
                summary.monthStart(),
                summary.plannedHousing(),
                summary.actualHousing(),
                summary.plannedEssentials(),
                summary.actualEssentials(),
                summary.plannedLifestyle(),
                summary.actualLifestyle()
        );
    }

    private static BudgetPlanResponse toResponse(BudgetPlan budgetPlan) {
        return new BudgetPlanResponse(
                budgetPlan.id(),
                budgetPlan.profileId(),
                budgetPlan.monthStart(),
                budgetPlan.housingTarget().amount(),
                budgetPlan.essentialsTarget().amount(),
                budgetPlan.lifestyleTarget().amount(),
                budgetPlan.createdAt(),
                budgetPlan.updatedAt()
        );
    }
}
