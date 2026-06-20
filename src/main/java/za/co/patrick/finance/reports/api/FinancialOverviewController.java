package za.co.patrick.finance.reports.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.patrick.finance.reports.application.FinancialOverviewResponse;
import za.co.patrick.finance.reports.application.FinancialOverviewService;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/overview")
@Tag(name = "Overview", description = "High-level financial summary endpoints")
public class FinancialOverviewController {
    private final FinancialOverviewService financialOverviewService;

    public FinancialOverviewController(FinancialOverviewService financialOverviewService) {
        this.financialOverviewService = financialOverviewService;
    }

    @GetMapping
    @Operation(summary = "Get the combined financial overview for a profile")
    public FinancialOverviewResponse getOverview(@PathVariable UUID profileId) {
        return financialOverviewService.getOverview(profileId);
    }
}
