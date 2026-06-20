package za.co.patrick.finance.budget.api;

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
class BudgetPlanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateBudgetPlanAndReturnBudgetSummary() throws Exception {
        var profileResponse = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Budget Tester",
                                  "emailAddress": "budget@example.com",
                                  "employmentType": "SALARIED",
                                  "preferredCurrency": "ZAR",
                                  "countryCode": "ZA",
                                  "monthlyNetIncome": 40000.00,
                                  "monthlyHousingCost": 12000.00,
                                  "monthlyEssentialCosts": 8000.00,
                                  "onboardingDate": "2026-06-20"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String profileId = profileResponse.getResponse().getHeader("Location").replaceAll(".*/", "");

        mockMvc.perform(post("/api/v1/profiles/{profileId}/expenses", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "expenseName": "Rent",
                                  "amount": 10000.00,
                                  "currency": "ZAR",
                                  "category": "HOUSING",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-06-01",
                                  "essential": true
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles/{profileId}/expenses", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "expenseName": "Groceries",
                                  "amount": 5000.00,
                                  "currency": "ZAR",
                                  "category": "ESSENTIAL",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-06-05",
                                  "essential": true
                                }
                                """))
                .andExpect(status().isCreated());

        var budgetResponse = mockMvc.perform(post("/api/v1/profiles/{profileId}/budgets", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "monthStart": "2026-06-01",
                                  "housingTarget": 11000.00,
                                  "essentialsTarget": 7000.00,
                                  "lifestyleTarget": 4000.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String budgetId = budgetResponse.getResponse().getHeader("Location").replaceAll(".*/", "");

        mockMvc.perform(get("/api/v1/profiles/{profileId}/budgets/{budgetId}/summary", profileId, budgetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actualHousing").value(10000.00))
                .andExpect(jsonPath("$.actualEssentials").value(5000.00))
                .andExpect(jsonPath("$.plannedLifestyle").value(4000.00));
    }
}
