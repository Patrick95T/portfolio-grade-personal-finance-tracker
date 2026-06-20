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
import za.co.patrick.finance.cashflow.application.CreateIncomeEntryCommand;
import za.co.patrick.finance.cashflow.application.IncomeEntryService;
import za.co.patrick.finance.cashflow.domain.IncomeEntry;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/incomes")
@Tag(name = "Income", description = "Track recurring and one-off income items")
public class IncomeEntryController {

    private final IncomeEntryService incomeEntryService;

    public IncomeEntryController(IncomeEntryService incomeEntryService) {
        this.incomeEntryService = incomeEntryService;
    }

    @PostMapping
    @Operation(summary = "Create an income entry")
    public ResponseEntity<IncomeEntryResponse> createIncome(
            @PathVariable UUID profileId,
            @Valid @RequestBody CreateIncomeEntryRequest request
    ) {
        var createdIncome = incomeEntryService.createIncome(new CreateIncomeEntryCommand(
                profileId,
                request.sourceName(),
                request.amount(),
                request.currency(),
                request.frequency(),
                request.startDate(),
                request.endDate(),
                request.taxable()
        ));
        return ResponseEntity
                .created(URI.create("/api/v1/profiles/" + profileId + "/incomes/" + createdIncome.id()))
                .body(toResponse(createdIncome));
    }

    @GetMapping
    @Operation(summary = "List income entries for a profile")
    public List<IncomeEntryResponse> listIncome(@PathVariable UUID profileId) {
        return incomeEntryService.listProfileIncome(profileId)
                .stream()
                .map(IncomeEntryController::toResponse)
                .toList();
    }

    private static IncomeEntryResponse toResponse(IncomeEntry incomeEntry) {
        return new IncomeEntryResponse(
                incomeEntry.id(),
                incomeEntry.profileId(),
                incomeEntry.sourceName(),
                incomeEntry.amount().amount(),
                incomeEntry.amount().currency(),
                incomeEntry.frequency(),
                incomeEntry.startDate(),
                incomeEntry.endDate(),
                incomeEntry.taxable(),
                incomeEntry.createdAt(),
                incomeEntry.updatedAt()
        );
    }
}
