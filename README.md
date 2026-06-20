# Portfolio-Grade Personal Finance Tracker

Portfolio-grade personal finance and forecasting platform built with Java 26, Spring Boot 4, PostgreSQL, and Flyway.

This project is intentionally designed as more than a CRUD expense tracker. It models real financial behavior across income, expenses, budgets, debt, savings goals, net worth, and deterministic forecasting scenarios.

The goal is to show product thinking, domain modelling, and backend engineering discipline in one repository.

## Why This Project Matters

Most finance portfolio projects stop at storing transactions.

This one is built to answer planning questions that feel closer to a real decision-support product:

- What happens if I pay extra toward debt every month?
- When will I reach a savings goal?
- How does a new recurring expense affect my future position?
- What does my net worth look like over the next 12 months?
- How does a one-time financial shock change the forecast?

That shift from storage to reasoning is the main point of the project.

## What This Demonstrates

- modular monolith design with clear business boundaries
- domain language across profile, cashflow, budgeting, debt, goals, net worth, and forecasting
- Flyway-managed schema evolution on PostgreSQL
- deterministic financial forecasting and what-if scenario modelling
- Swagger-first API exploration for fast local testing
- security, validation, integration testing, and auditability

## Key Features

- financial profile management
- recurring and one-off income tracking
- recurring and one-off expense tracking
- monthly budget planning and summary views
- debt account management and payoff simulation
- savings goals and completion projections
- asset tracking and net worth reporting
- monthly cashflow and debt summary reports
- deterministic multi-month forecasting
- audit events for key financial create actions

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

Today it already supports the core financial workflow end to end:

- create a financial profile
- add incomes, expenses, budgets, debts, goals, and assets
- review overview and reporting endpoints
- simulate debt payoff scenarios
- generate deterministic forecasts
- inspect audit history

## Swagger-First Usage

This project is intended to be exercised primarily through Swagger UI rather than a separate frontend.

- Swagger UI: `/swagger-ui/index.html`
- OpenAPI docs: `/v3/api-docs`
- API endpoints require HTTP Basic authentication by default
- Default local credentials come from `application.yml` and can be overridden with:
  - `APP_SECURITY_USERNAME`
  - `APP_SECURITY_PASSWORD`
  - `APP_SECURITY_ROLE`

If you want a fast guided walkthrough with copy-paste payloads, use:

- [docs/swagger-test-data.md](./docs/swagger-test-data.md)

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
