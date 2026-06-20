package za.co.patrick.finance.cashflow.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
class IncomeEntryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndListIncomeForProfile() throws Exception {
        var profileResponse = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Cashflow Tester",
                                  "emailAddress": "cashflow@example.com",
                                  "employmentType": "SALARIED",
                                  "preferredCurrency": "ZAR",
                                  "countryCode": "ZA",
                                  "monthlyNetIncome": 30000.00,
                                  "monthlyHousingCost": 10000.00,
                                  "monthlyEssentialCosts": 7000.00,
                                  "onboardingDate": "2026-06-20"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String profileLocation = profileResponse.getResponse().getHeader("Location");
        String profileId = profileLocation.substring(profileLocation.lastIndexOf('/') + 1);

        mockMvc.perform(post("/api/v1/profiles/{profileId}/incomes", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "sourceName": "Primary Salary",
                                  "amount": 30000.00,
                                  "currency": "ZAR",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-06-25",
                                  "taxable": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.sourceName").value("Primary Salary"))
                .andExpect(jsonPath("$.frequency").value("MONTHLY"));

        mockMvc.perform(get("/api/v1/profiles/{profileId}/incomes", profileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount").value(30000.00));
    }

    @Test
    void shouldRejectIncomeWithEndDateBeforeStartDate() throws Exception {
        var profileResponse = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Rule Tester",
                                  "emailAddress": "rules@example.com",
                                  "employmentType": "SELF_EMPLOYED",
                                  "preferredCurrency": "USD",
                                  "countryCode": "US",
                                  "monthlyNetIncome": 8000.00,
                                  "monthlyHousingCost": 2000.00,
                                  "monthlyEssentialCosts": 1500.00,
                                  "onboardingDate": "2026-06-20"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String profileLocation = profileResponse.getResponse().getHeader("Location");
        String profileId = profileLocation.substring(profileLocation.lastIndexOf('/') + 1);

        mockMvc.perform(post("/api/v1/profiles/{profileId}/incomes", profileId)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "sourceName": "Consulting Retainer",
                                  "amount": 2000.00,
                                  "currency": "USD",
                                  "frequency": "MONTHLY",
                                  "startDate": "2026-07-01",
                                  "endDate": "2026-06-01",
                                  "taxable": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Income end date cannot be before the start date"));
    }
}
