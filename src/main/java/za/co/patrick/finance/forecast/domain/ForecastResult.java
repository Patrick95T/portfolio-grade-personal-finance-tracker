package za.co.patrick.finance.forecast.domain;

import java.util.List;
import java.util.UUID;

public record ForecastResult(
        UUID profileId,
        String currency,
        ForecastScenario scenario,
        ForecastSummary summary,
        List<ForecastMonthProjection> monthlyProjections
) {
}
