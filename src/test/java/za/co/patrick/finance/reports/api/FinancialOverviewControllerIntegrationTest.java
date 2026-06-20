package za.co.patrick.finance.reports.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FinancialOverviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCombinedFinancialOverview() throws Exception {
        var profileResponse = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Overview Tester",
                                  "emailAddress": "overview@example.com",
                                  "employmentType": "SELF_EMPLOYED",
                                  "preferredCurrency": "USD",
                                  "countryCode": "US",
                                  "monthlyNetIncome": 9000.00,
                                  "monthlyHousingCost": 2500.00,
                                  "monthlyEssentialCosts": 2000.00,
                                  "onboardingDate": "2026-06-20"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String profileId = profileResponse.getResponse().getHeader("Location").replaceAll(".*/", "");

        mockMvc.perform(post("/api/v1/profiles/{profileId}/incomes", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "sourceName": "Consulting",
                                  "amount": 9000.00,
                                  "currency": "USD",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-06-01",
                                  "taxable": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/expenses", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "expenseName": "Rent",
                                  "amount": 2500.00,
                                  "currency": "USD",
                                  "category": "HOUSING",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-06-01",
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
                                  "currentBalance": 12000.00,
                                  "minimumMonthlyPayment": 450.00,
                                  "annualInterestRate": 11.50
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/goals", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "goalName": "Emergency Fund",
                                  "goalType": "EMERGENCY_FUND",
                                  "targetAmount": 10000.00,
                                  "currentSaved": 3500.00,
                                  "targetDate": "2027-06-01"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/assets", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "assetName": "Savings Account",
                                  "assetType": "CASH",
                                  "currentValue": 5000.00
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/profiles/{profileId}/overview", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(9000.00))
                .andExpect(jsonPath("$.totalExpenses").value(2500.00))
                .andExpect(jsonPath("$.monthlySurplus").value(6500.00))
                .andExpect(jsonPath("$.totalDebt").value(12000.00))
                .andExpect(jsonPath("$.totalAssets").value(5000.00))
                .andExpect(jsonPath("$.netWorth").value(-7000.00))
                .andExpect(jsonPath("$.totalGoalTarget").value(10000.00))
                .andExpect(jsonPath("$.totalGoalSaved").value(3500.00));
    }
}
