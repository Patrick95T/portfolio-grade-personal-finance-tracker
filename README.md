# Personal Finance Forecasting Platform

Production-grade personal finance and forecasting platform built with Java 26 and Spring Boot 4.

This project is intentionally designed as more than an expense tracker. It models real financial behavior across income, expenses, budgets, debt, savings goals, net worth, and deterministic forecasting scenarios.

## Why This Project Exists

Most portfolio finance apps stop at CRUD.

This one is intended to grow into a decision-support system that helps a person answer questions like:

- What happens if I pay extra toward debt every month?
- When will I reach a savings goal?
- When does my net worth become positive?
- How does a new car payment change my long-term position?

## Current State

This repository is in architecture-first mode.

The initial scaffold and architecture documentation are in place so implementation can begin from a clear design instead of growing reactively.

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

## Next Step

The next implementation phase should begin with:

1. finalising ubiquitous language
2. defining MVP API contracts
3. scaffolding core modules
4. adding the first Flyway migrations
