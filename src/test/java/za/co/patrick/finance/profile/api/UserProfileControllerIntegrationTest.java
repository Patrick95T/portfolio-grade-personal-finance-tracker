package za.co.patrick.finance.profile.api;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import za.co.patrick.finance.profile.domain.EmploymentType;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndFetchUserProfile() throws Exception {
        String request = """
                {
                  "fullName": "Patrick Tester",
                  "emailAddress": "patrick@example.com",
                  "employmentType": "SALARIED",
                  "preferredCurrency": "ZAR",
                  "countryCode": "ZA",
                  "monthlyNetIncome": 45000.00,
                  "monthlyHousingCost": 12000.00,
                  "monthlyEssentialCosts": 8000.00,
                  "onboardingDate": "2026-06-20"
                }
                """;

        var createResult = mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.fullName").value("Patrick Tester"))
                .andExpect(jsonPath("$.monthlyAvailableForPlanning").value(25000.00))
                .andReturn();

        String location = createResult.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress").value("patrick@example.com"))
                .andExpect(jsonPath("$.preferredCurrency").value("ZAR"));

        mockMvc.perform(get("/api/v1/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].emailAddress", hasItem("patrick@example.com")));
    }

    @Test
    void shouldRejectProfileWhenCommittedCostsExceedIncome() throws Exception {
        String request = """
                {
                  "fullName": "Overcommitted User",
                  "emailAddress": "overcommitted@example.com",
                  "employmentType": "CONTRACTOR",
                  "preferredCurrency": "USD",
                  "countryCode": "US",
                  "monthlyNetIncome": 5000.00,
                  "monthlyHousingCost": 3500.00,
                  "monthlyEssentialCosts": 2500.00,
                  "onboardingDate": "2026-06-20"
                }
                """;

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Business rule violation"))
                .andExpect(jsonPath("$.detail")
                        .value("Monthly housing and essential costs cannot exceed monthly net income"));
    }
}
