package za.co.patrick.finance.cashflow.api;

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
import za.co.patrick.finance.cashflow.application.CreateExpenseEntryCommand;
import za.co.patrick.finance.cashflow.application.ExpenseEntryService;
import za.co.patrick.finance.cashflow.domain.ExpenseEntry;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/expenses")
@Tag(name = "Expenses", description = "Track recurring and one-off expenses")
public class ExpenseEntryController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseEntryController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @PostMapping
    @Operation(summary = "Create an expense entry")
    public ResponseEntity<ExpenseEntryResponse> createExpense(
            @PathVariable UUID profileId,
            @Valid @RequestBody CreateExpenseEntryRequest request
    ) {
        var createdExpense = expenseEntryService.createExpense(new CreateExpenseEntryCommand(
                profileId,
                request.expenseName(),
                request.amount(),
                request.currency(),
                request.category(),
                request.frequency(),
                request.startDate(),
                request.endDate(),
                request.essential()
        ));
        return ResponseEntity
                .created(URI.create("/api/v1/profiles/" + profileId + "/expenses/" + createdExpense.id()))
                .body(toResponse(createdExpense));
    }

    @GetMapping
    @Operation(summary = "List expense entries for a profile")
    public List<ExpenseEntryResponse> listExpenses(@PathVariable UUID profileId) {
        return expenseEntryService.listProfileExpenses(profileId)
                .stream()
                .map(ExpenseEntryController::toResponse)
                .toList();
    }

    private static ExpenseEntryResponse toResponse(ExpenseEntry expenseEntry) {
        return new ExpenseEntryResponse(
                expenseEntry.id(),
                expenseEntry.profileId(),
                expenseEntry.expenseName(),
                expenseEntry.amount().amount(),
                expenseEntry.amount().currency(),
                expenseEntry.category(),
                expenseEntry.frequency(),
                expenseEntry.startDate(),
                expenseEntry.endDate(),
                expenseEntry.essential(),
                expenseEntry.createdAt(),
                expenseEntry.updatedAt()
        );
    }
}
