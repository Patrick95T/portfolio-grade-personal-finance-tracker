package za.co.patrick.finance.debt.api;

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
import za.co.patrick.finance.debt.application.CreateDebtAccountCommand;
import za.co.patrick.finance.debt.application.DebtAccountService;
import za.co.patrick.finance.debt.domain.DebtAccount;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/debts")
@Tag(name = "Debts", description = "Manage debt accounts and simulate payoff plans")
public class DebtAccountController {
    private final DebtAccountService debtAccountService;

    public DebtAccountController(DebtAccountService debtAccountService) {
        this.debtAccountService = debtAccountService;
    }

    @PostMapping
    @Operation(summary = "Create a debt account")
    public ResponseEntity<DebtAccountResponse> createDebtAccount(@PathVariable UUID profileId, @Valid @RequestBody CreateDebtAccountRequest request) {
        var debt = debtAccountService.createDebtAccount(new CreateDebtAccountCommand(
                profileId,
                request.lenderName(),
                request.debtType(),
                request.currentBalance(),
                request.minimumMonthlyPayment(),
                request.annualInterestRate()
        ));
        return ResponseEntity.created(URI.create("/api/v1/profiles/" + profileId + "/debts/" + debt.id())).body(toResponse(debt));
    }

    @GetMapping
    @Operation(summary = "List debt accounts for a profile")
    public List<DebtAccountResponse> listDebtAccounts(@PathVariable UUID profileId) {
        return debtAccountService.listDebtAccounts(profileId).stream().map(DebtAccountController::toResponse).toList();
    }

    @PostMapping("/{debtAccountId}/simulate-payoff")
    @Operation(summary = "Simulate payoff timeline for a debt account")
    public DebtPayoffProjectionResponse simulatePayoff(
            @PathVariable UUID profileId,
            @PathVariable UUID debtAccountId,
            @Valid @RequestBody DebtPayoffSimulationRequest request
    ) {
        var projection = debtAccountService.simulatePayoff(
                profileId,
                debtAccountId,
                request.extraMonthlyPayment() == null ? java.math.BigDecimal.ZERO : request.extraMonthlyPayment()
        );
        return new DebtPayoffProjectionResponse(
                projection.debtAccountId(),
                projection.profileId(),
                projection.lenderName(),
                projection.startingBalance(),
                projection.minimumMonthlyPayment(),
                projection.extraMonthlyPayment(),
                projection.totalMonthlyPayment(),
                projection.totalInterestAccrued(),
                projection.payoffMonths(),
                projection.payoffMonth(),
                projection.monthlySchedule().stream()
                        .map(month -> new DebtPayoffMonthResponse(
                                month.month(),
                                month.openingBalance(),
                                month.interestAccrued(),
                                month.paymentApplied(),
                                month.closingBalance()
                        ))
                        .toList()
        );
    }

    private static DebtAccountResponse toResponse(DebtAccount debt) {
        return new DebtAccountResponse(
                debt.id(), debt.profileId(), debt.lenderName(), debt.debtType(),
                debt.currentBalance().amount(), debt.minimumMonthlyPayment().amount(), debt.annualInterestRate(),
                debt.createdAt(), debt.updatedAt()
        );
    }
}
