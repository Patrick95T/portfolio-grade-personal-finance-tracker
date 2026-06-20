package za.co.patrick.finance.reports.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.patrick.finance.reports.application.DebtSummaryReportResponse;
import za.co.patrick.finance.reports.application.FinancialReportsService;
import za.co.patrick.finance.reports.application.MonthlyCashflowReportResponse;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/reports")
@Tag(name = "Reports", description = "Reporting endpoints designed for Swagger-driven use")
public class FinancialReportsController {

    private final FinancialReportsService financialReportsService;

    public FinancialReportsController(FinancialReportsService financialReportsService) {
        this.financialReportsService = financialReportsService;
    }

    @GetMapping("/monthly-cashflow")
    @Operation(summary = "Get normalized monthly cashflow report for a given month")
    public MonthlyCashflowReportResponse getMonthlyCashflow(
            @PathVariable UUID profileId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month
    ) {
        return financialReportsService.getMonthlyCashflow(profileId, month);
    }

    @GetMapping("/debt-summary")
    @Operation(summary = "Get debt summary report for a profile")
    public DebtSummaryReportResponse getDebtSummary(@PathVariable UUID profileId) {
        return financialReportsService.getDebtSummary(profileId);
    }
}
