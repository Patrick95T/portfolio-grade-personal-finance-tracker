package za.co.patrick.finance.budget.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BudgetPlanResponse(
        UUID id,
        UUID profileId,
        LocalDate monthStart,
        BigDecimal housingTarget,
        BigDecimal essentialsTarget,
        BigDecimal lifestyleTarget,
        Instant createdAt,
        Instant updatedAt
) {
}
