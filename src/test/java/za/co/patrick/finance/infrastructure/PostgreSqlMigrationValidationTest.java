package za.co.patrick.finance.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("postgres-test")
@Testcontainers(disabledWithoutDocker = true)
class PostgreSqlMigrationValidationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("finance_validation")
            .withUsername("finance_user")
            .withPassword("finance_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldApplyMigrationsAndExposeCoreTables() {
        Integer profileTableCount = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.tables where table_name = 'user_profiles'",
                Integer.class
        );
        Integer incomeTableCount = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.tables where table_name = 'income_entries'",
                Integer.class
        );

        assertThat(profileTableCount).isEqualTo(1);
        assertThat(incomeTableCount).isEqualTo(1);
    }
}
