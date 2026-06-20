package za.co.patrick.finance.planning.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class PlanningEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposePayoffProjectionGoalProjectionReportsAndAuditTrail() throws Exception {
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
                                  "expenseName": "Rent",
                                  "amount": 1500.00,
                                  "currency": "USD",
                                  "category": "HOUSING",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-07-01",
                                  "essential": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/expenses", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "expenseName": "Groceries",
                                  "amount": 700.00,
                                  "currency": "USD",
                                  "category": "ESSENTIAL",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-07-01",
                                  "essential": true
                                }
                                """))
                .andExpect(status().isCreated());

        var debtResponse = mockMvc.perform(post("/api/v1/profiles/{profileId}/debts", profileId)
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
                .andExpect(status().isCreated())
                .andReturn();

        String debtId = debtResponse.getResponse().getHeader("Location").replaceAll(".*/", "");

        var goalResponse = mockMvc.perform(post("/api/v1/profiles/{profileId}/goals", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "goalName": "Emergency Fund",
                                  "goalType": "EMERGENCY_FUND",
                                  "targetAmount": 4000.00,
                                  "currentSaved": 1000.00,
                                  "targetDate": "2026-12-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String goalId = goalResponse.getResponse().getHeader("Location").replaceAll(".*/", "");

        mockMvc.perform(post("/api/v1/profiles/{profileId}/assets", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "assetName": "Emergency Savings",
                                  "assetType": "CASH",
                                  "currentValue": 2500.00
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/debts/{debtId}/simulate-payoff", profileId, debtId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "extraMonthlyPayment": 80.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payoffMonths").value(7))
                .andExpect(jsonPath("$.totalMonthlyPayment").value(200.00))
                .andExpect(jsonPath("$.monthlySchedule[0].closingBalance").value(1012.00));

        mockMvc.perform(get("/api/v1/profiles/{profileId}/goals/{goalId}/projection", profileId, goalId)
                        .queryParam("monthlyContribution", "500.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remainingGap").value(3000.00))
                .andExpect(jsonPath("$.projectedMonthsToGoal").value(6))
                .andExpect(jsonPath("$.onTrackForTargetDate").value(true));

        mockMvc.perform(get("/api/v1/profiles/{profileId}/reports/monthly-cashflow", profileId)
                        .queryParam("month", "2026-07-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(5000.00))
                .andExpect(jsonPath("$.housingExpenses").value(1500.00))
                .andExpect(jsonPath("$.essentialExpenses").value(700.00))
                .andExpect(jsonPath("$.monthlySurplus").value(2800.00));

        mockMvc.perform(get("/api/v1/profiles/{profileId}/reports/debt-summary", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.debtCount").value(1))
                .andExpect(jsonPath("$.totalDebtBalance").value(1200.00))
                .andExpect(jsonPath("$.weightedAverageInterestRate").value(12.00));

        mockMvc.perform(get("/api/v1/profiles/{profileId}/audit-events", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventType").exists())
                .andExpect(jsonPath("$[?(@.eventType == 'ASSET_CREATED')]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.eventType == 'GOAL_CREATED')]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.eventType == 'DEBT_CREATED')]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.eventType == 'INCOME_CREATED')]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.eventType == 'EXPENSE_CREATED')]").isNotEmpty());
    }

    private String createProfile() throws Exception {
        var response = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Planning Tester",
                                  "emailAddress": "%s",
                                  "employmentType": "SALARIED",
                                  "preferredCurrency": "USD",
                                  "countryCode": "US",
                                  "monthlyNetIncome": 5000.00,
                                  "monthlyHousingCost": 1500.00,
                                  "monthlyEssentialCosts": 1000.00,
                                  "onboardingDate": "2026-06-20"
                                }
                                """.formatted("planning-" + UUID.randomUUID() + "@example.com")))
                .andExpect(status().isCreated())
                .andReturn();

        return response.getResponse().getHeader("Location").replaceAll(".*/", "");
    }
}
