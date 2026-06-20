package za.co.patrick.finance.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfiguration {

    @Bean
    OpenAPI personalFinanceOpenApi(SecurityProperties securityProperties) {
        var openApi = new OpenAPI()
                .info(new Info()
                        .title("Portfolio-Grade Personal Finance Tracker API")
                        .description("API-first personal finance tracker for planning, simulation, reporting, and auditability.")
                        .version("v1")
                        .contact(new Contact().name("Patrick95T")));

        if (securityProperties.enabled()) {
            openApi.components(new Components().addSecuritySchemes("basicAuth",
                    new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("basic")
                            .description("Use the configured local API username and password.")))
                    .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
        }

        return openApi;
    }
}
