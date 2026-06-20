# Portfolio-Grade Personal Finance Tracker

Portfolio-grade personal finance tracker built with Java 26 and Spring Boot 4.

This project is intentionally designed as more than a CRUD expense tracker. It models real financial behavior across income, expenses, budgets, debt, savings goals, net worth, and deterministic forecasting scenarios.

## Why This Project Exists

Most portfolio finance apps stop at CRUD.

This one is intended to grow into a decision-support system that helps a person answer questions like:

- What happens if I pay extra toward debt every month?
- When will I reach a savings goal?
- When does my net worth become positive?
- How does a new car payment change my long-term position?

## Architecture At A Glance

```text
Profile
  |
  v
Cashflow
  |
  v
Budget
  |
  v
Debt
  |
  v
Goals
  |
  v
Net Worth
  |
  v
Forecast Engine
```

This flow reflects the way the product turns financial inputs into planning insight. Profile and cashflow establish the baseline, budgets and debts shape monthly constraints, goals and assets influence long-term intent, and the forecast engine projects future outcomes.

## Current State

This repository started in architecture-first mode and now includes the first core implementation slice plus a deterministic forecast endpoint.

## Swagger-First Usage

This project is intended to be exercised primarily through Swagger UI rather than a separate frontend.

- Swagger UI: `/swagger-ui/index.html`
- OpenAPI docs: `/v3/api-docs`
- API endpoints require HTTP Basic authentication by default
- Default local credentials come from `application.yml` and can be overridden with:
  - `APP_SECURITY_USERNAME`
  - `APP_SECURITY_PASSWORD`
  - `APP_SECURITY_ROLE`

## Core Product Themes

- personal financial profile
- income and expense tracking
- monthly budgeting
- debt management and payoff simulation
- savings goals and contribution planning
- assets, liabilities, and net worth
- forecasting and what-if scenarios
- reporting and auditability

## Architecture

The main architecture document is [architecture.md](./architecture.md).

Supporting design notes:

- [docs/api-design.md](./docs/api-design.md)
- [docs/domain-model.md](./docs/domain-model.md)
- [docs/forecasting-rules.md](./docs/forecasting-rules.md)
- [docs/swagger-test-data.md](./docs/swagger-test-data.md)

## Planned Stack

- Java 26
- Spring Boot 4.1.x
- Spring Security 7.x
- PostgreSQL
- Flyway
- Spring Data JPA
- OpenAPI
- Testcontainers
- Docker Compose

## Repository Structure

```text
portfolio-grade-personal-finance-tracker
  docs/
  src/
  architecture.md
  docker-compose.yml
  pom.xml
  README.md
```

## Implemented So Far

1. shared finance-safe primitives
2. profile, income, expense, budget, debt, goal, asset, and overview APIs
3. Flyway-managed schema for the persisted financial inputs
4. deterministic monthly forecast generation with what-if overrides
5. debt payoff simulation, goal projection, monthly cashflow reporting, and debt summary reporting
6. audit trail for financial create actions
7. integration-tested Spring Boot endpoints

## Testing Through Swagger

For a copy-paste sample dataset and recommended end-to-end test flow, use:

- [docs/swagger-test-data.md](./docs/swagger-test-data.md)
