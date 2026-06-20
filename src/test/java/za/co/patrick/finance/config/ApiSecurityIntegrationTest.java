package za.co.patrick.finance.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "app.security.enabled=true",
        "app.security.username=swagger-user",
        "app.security.password=swagger-password",
        "app.security.role=API_USER"
})
class ApiSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRequireAuthenticationForApiEndpointsWhenSecurityEnabled() throws Exception {
        mockMvc.perform(get("/api/v1/profiles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowSwaggerDocsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowAuthenticatedAccessToApiEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/profiles").with(httpBasic("swagger-user", "swagger-password")))
                .andExpect(status().isOk());
    }
}
