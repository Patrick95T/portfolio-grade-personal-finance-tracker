package za.co.patrick.finance.forecast.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ForecastControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGenerateDeterministicMonthlyForecast() throws Exception {
        String profileId = createProfile();

        mockMvc.perform(post("/api/v1/profiles/{profileId}/incomes", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "sourceName": "Salary",
                                  "amount": 5000.00,
                                  "currency": "USD",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-07-01",
                                  "taxable": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/expenses", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "expenseName": "Living Costs",
                                  "amount": 2000.00,
                                  "currency": "USD",
                                  "category": "ESSENTIAL",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-07-01",
                                  "essential": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/debts", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "lenderName": "Bank Loan",
                                  "debtType": "PERSONAL_LOAN",
                                  "currentBalance": 1200.00,
                                  "minimumMonthlyPayment": 120.00,
                                  "annualInterestRate": 12.00
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/goals", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "goalName": "Emergency Fund",
                                  "goalType": "EMERGENCY_FUND",
                                  "targetAmount": 4000.00,
                                  "currentSaved": 1000.00,
                                  "targetDate": "2027-12-01"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/assets", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "assetName": "Cash Reserve",
                                  "assetType": "CASH",
                                  "currentValue": 3000.00
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/forecasts", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "startMonth": "2026-07-01",
                                  "months": 2,
                                  "extraDebtPayment": 80.00,
                                  "monthlySavingsContribution": 500.00,
                                  "monthlyIncomeAdjustment": 0.00,
                                  "monthlyExpenseAdjustment": 0.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.summary.startingNetWorth").value(1800.00))
                .andExpect(jsonPath("$.summary.endingAssets").value(8600.00))
                .andExpect(jsonPath("$.summary.endingDebt").value(822.12))
                .andExpect(jsonPath("$.summary.endingNetWorth").value(7777.88))
                .andExpect(jsonPath("$.summary.totalInterestAccrued").value(22.12))
                .andExpect(jsonPath("$.summary.endingGoalSaved").value(2000.00))
                .andExpect(jsonPath("$.monthlyProjections[0].month").value("2026-07-01"))
                .andExpect(jsonPath("$.monthlyProjections[0].debtPayment").value(200.00))
                .andExpect(jsonPath("$.monthlyProjections[0].projectedDebt").value(1012.00))
                .andExpect(jsonPath("$.monthlyProjections[0].projectedNetWorth").value(4788.00))
                .andExpect(jsonPath("$.monthlyProjections[1].projectedDebt").value(822.12))
                .andExpect(jsonPath("$.monthlyProjections[1].totalGoalSaved").value(2000.00));
    }

    @Test
    void shouldRejectForecastThatDoesNotStartOnFirstDayOfMonth() throws Exception {
        String profileId = createProfile();

        mockMvc.perform(post("/api/v1/profiles/{profileId}/forecasts", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "startMonth": "2026-07-15",
                                  "months": 6
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Forecast start month must be the first day of a month"));
    }

    private String createProfile() throws Exception {
        var response = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Forecast Tester",
                                  "emailAddress": "%s",
                                  "employmentType": "SALARIED",
                                  "preferredCurrency": "USD",
                                  "countryCode": "US",
                                  "monthlyNetIncome": 5000.00,
                                  "monthlyHousingCost": 1500.00,
                                  "monthlyEssentialCosts": 1000.00,
                                  "onboardingDate": "2026-06-20"
                                }
                                """.formatted("forecast-" + UUID.randomUUID() + "@example.com")))
                .andExpect(status().isCreated())
                .andReturn();

        return response.getResponse().getHeader("Location").replaceAll(".*/", "");
    }
}
