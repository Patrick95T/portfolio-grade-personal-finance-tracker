package za.co.patrick.finance.forecast.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import za.co.patrick.finance.forecast.application.ForecastService;
import za.co.patrick.finance.forecast.application.GenerateForecastCommand;

@RestController
@RequestMapping("/api/v1/profiles/{profileId}/forecasts")
@Tag(name = "Forecasts", description = "Generate deterministic on-the-fly financial forecasts")
public class ForecastController {

    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Generate a forecast with optional what-if overrides")
    public ForecastResponse generateForecast(
            @PathVariable UUID profileId,
            @Valid @RequestBody CreateForecastRequest request
    ) {
        var forecast = forecastService.generateForecast(new GenerateForecastCommand(
                profileId,
                request.startMonth(),
                request.months(),
                defaultValue(request.extraDebtPayment()),
                defaultValue(request.monthlySavingsContribution()),
                defaultValue(request.monthlyIncomeAdjustment()),
                defaultValue(request.monthlyExpenseAdjustment())
        ));

        return new ForecastResponse(
                forecast.profileId(),
                forecast.currency(),
                forecast.scenario().startMonth(),
                forecast.scenario().months(),
                forecast.scenario().extraDebtPayment(),
                forecast.scenario().monthlySavingsContribution(),
                forecast.scenario().monthlyIncomeAdjustment(),
                forecast.scenario().monthlyExpenseAdjustment(),
                new ForecastSummaryResponse(
                        forecast.summary().startingNetWorth(),
                        forecast.summary().endingAssets(),
                        forecast.summary().endingDebt(),
                        forecast.summary().endingNetWorth(),
                        forecast.summary().totalInterestAccrued(),
                        forecast.summary().endingGoalSaved(),
                        forecast.summary().projectedDebtFreeMonth(),
                        forecast.summary().projectedGoalCompletionMonth()
                ),
                forecast.monthlyProjections().stream()
                        .map(month -> new ForecastMonthResponse(
                                month.month(),
                                month.income(),
                                month.expenses(),
                                month.debtPayment(),
                                month.debtInterestAccrued(),
                                month.savingsContribution(),
                                month.netCashflowAfterDebt(),
                                month.projectedAssets(),
                                month.projectedDebt(),
                                month.projectedNetWorth(),
                                month.totalGoalSaved()
                        ))
                        .toList()
        );
    }

    private BigDecimal defaultValue(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
