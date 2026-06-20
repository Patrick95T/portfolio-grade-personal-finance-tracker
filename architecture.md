# Personal Finance Forecasting Platform Architecture

## 1. Purpose

This project is a production-grade personal finance platform focused on financial planning, forecasting, and decision support.

It is not meant to be a basic CRUD expense tracker. The platform should help a user understand current financial position and reason about future outcomes under different scenarios.

## 2. Product Vision

The product enables a user to model personal finances in one place:

- income
- expenses
- debt obligations
- savings goals
- assets and liabilities
- monthly budgets
- future forecasts

The most valuable outcome is not storage. It is insight.

The platform should answer practical questions such as:

- How much am I really saving each month?
- Which debt will be paid off first?
- What changes if I increase debt repayments?
- When can I afford a house deposit or car deposit?
- What will my net worth look like in 12, 24, or 60 months?

## 3. Target Users

### Primary User

A single individual or household managing personal finances.

Typical needs:

- capture recurring income and spending
- track debt balances and payment obligations
- define savings goals
- understand budget performance
- simulate future outcomes

### Future User Types

Later versions may support:

- financial coach or advisor views
- household collaboration
- multiple profiles under one account
- small-team support for coaching or advisory businesses

## 4. Product Scope

### MVP

The first release should support:

- user financial profile
- income and expense tracking
- recurring and once-off cashflow items
- monthly budgets
- debt account management
- savings goals
- asset and liability tracking
- net worth calculation
- deterministic forecast generation
- what-if scenarios for debt and savings
- summary reporting

### Explicitly Out of Scope for MVP

- bank account aggregation
- direct payment initiation
- live market pricing for investments
- tax optimization engine
- multi-user collaboration
- mobile apps
- event-driven microservice decomposition
- AI-generated financial advice

## 5. Architectural Style

### Recommended Architecture

Use a modular monolith.

This is the right fit because:

- the domain is rich but not yet operationally distributed
- strong domain boundaries matter more than deployment complexity
- calculations, projections, and reporting benefit from local transactional consistency
- it allows internal modularity without premature service splitting

### What Is Good

- single deployable unit
- easier local development
- simpler testing and debugging
- strong path to future extraction of forecast or reporting modules if needed

### What Is Risky

- modules can blur if boundaries are not enforced
- forecasting logic can leak into controllers or persistence if discipline is weak
- financial calculations can become inconsistent if money handling is not centralized

### What Is Missing If We Stop Here

- explicit module-boundary tests
- async integration patterns for future notifications or report generation

### Simpler Alternative

A package-by-layer CRUD application would be simpler at the start but would age badly and weaken the portfolio value. It is not recommended.

## 6. Technology Baseline

- Java 26
- Spring Boot 4.1.x
- Spring Security 7.x
- PostgreSQL
- Flyway
- Spring Data JPA
- Spring Web
- Spring Validation
- springdoc OpenAPI
- Testcontainers
- Docker Compose

## 7. Package Structure

Recommended root package:

`za.co.patrick.finance`

Recommended top-level structure:

```text
src/main/java/za/co/patrick/finance
  shared
  profile
  cashflow
  budget
  debt
  goals
  networth
  forecast
  reports
  audit
  config
```

Each module should follow an internal structure like:

```text
module
  api
  application
  domain
  persistence
```

Boundary rules:

- `api` depends on `application`
- `application` depends on `domain`
- `persistence` implements storage concerns and maps to domain
- shared value objects and errors live in `shared`
- domain models must not depend on controllers or JPA entities

## 8. Core Modules

### 8.1 Shared

Purpose:

- centralize finance-safe value objects and common exceptions

Key concepts:

- `Money`
- `CurrencyCode`
- `Percentage`
- `DateRange`
- business rule exceptions
- global API error contracts

### 8.2 Profile

Purpose:

- store the user financial identity and assumptions baseline

Responsibilities:

- personal profile details
- employment type
- monthly baseline assumptions
- default forecasting preferences

### 8.3 Cashflow

Purpose:

- track money in and money out

Responsibilities:

- income entries
- expense entries
- recurring schedules
- categorisation
- monthly cashflow summaries

### 8.4 Budget

Purpose:

- plan intended spending and compare against actuals

Responsibilities:

- monthly budgets
- per-category targets
- budget performance
- overspend and underspend analysis

### 8.5 Debt

Purpose:

- represent liabilities that accrue or require repayment over time

Responsibilities:

- debt accounts
- interest rate assumptions
- payment schedules
- payoff simulation
- extra payment what-if analysis

### 8.6 Goals

Purpose:

- model savings or milestone targets

Responsibilities:

- goal definition
- target amount
- target date
- monthly contribution guidance
- completion projection

### 8.7 Net Worth

Purpose:

- represent assets and liabilities as a financial position

Responsibilities:

- asset tracking
- liability snapshots
- net worth calculation
- historical trend reporting

### 8.8 Forecast

Purpose:

- produce deterministic projections across future months

Responsibilities:

- scenario requests
- monthly projection engine
- assumptions handling
- debt and savings simulations
- reproducible forecast runs

### 8.9 Reports

Purpose:

- produce user-readable summaries from stored data and forecast runs

Responsibilities:

- monthly summary views
- debt payoff summaries
- forecast summaries
- export-ready report models

### 8.10 Audit

Purpose:

- capture meaningful system and business actions

Responsibilities:

- changes to financial inputs
- forecast run generation
- major updates and deletes
- traceability for future support and compliance needs

## 9. Ubiquitous Language

The following terms should be defined consistently in code and docs:

- `Budget`: a planned set of category limits for a time period
- `SavingsGoal`: a target amount with optional target date and contribution strategy
- `DebtAccount`: a liability with balance, interest assumptions, and repayment behavior
- `Forecast`: a deterministic projection across a time horizon using frozen assumptions
- `NetWorth`: total assets minus total liabilities at a point in time
- `Asset`: something owned with measurable financial value
- `Liability`: something owed with measurable financial value
- `CashflowItem`: income or expense that affects period movement
- `Scenario`: an adjustment to baseline assumptions used to compare outcomes

## 10. Domain Model Overview

Recommended early aggregates:

- `UserProfile`
- `Income`
- `Expense`
- `Budget`
- `DebtAccount`
- `SavingsGoal`
- `Asset`
- `ForecastRun`

Important value objects:

- `Money`
- `Percentage`
- `RecurringRule`
- `TargetDate`
- `ForecastAssumption`
- `MonthlyAmount`

Important enums:

- `EmploymentType`
- `TransactionType`
- `TransactionCategory`
- `BudgetStatus`
- `DebtType`
- `RepaymentStrategy`
- `GoalType`
- `GoalStatus`
- `AssetType`
- `ScenarioType`

## 11. Financial Modelling Rules

### Money Handling

Use a dedicated `Money` value object backed by `BigDecimal`.

Rules:

- no floating-point money math
- explicit scale and rounding rules
- arithmetic centralized in domain value objects
- currency consistency enforced

### Forecast Determinism

Forecasts must be reproducible.

That means:

- forecast inputs are snapshotted
- assumptions are explicit
- results are tied to a run timestamp and version
- future changes to profile or debt data must not silently mutate historical forecast results

### Historical Consistency

The system must distinguish:

- current financial state
- historical recorded state
- simulated future state

## 12. API Design

Use REST with resource-oriented naming.

Example groups:

- `/api/v1/profiles`
- `/api/v1/incomes`
- `/api/v1/expenses`
- `/api/v1/budgets`
- `/api/v1/debts`
- `/api/v1/goals`
- `/api/v1/assets`
- `/api/v1/net-worth`
- `/api/v1/forecasts`
- `/api/v1/reports`

Example endpoints:

- `POST /api/v1/profiles`
- `GET /api/v1/profiles/{profileId}`
- `POST /api/v1/incomes`
- `POST /api/v1/expenses`
- `GET /api/v1/cashflow/monthly-summary`
- `POST /api/v1/budgets`
- `GET /api/v1/budgets/{budgetId}/summary`
- `POST /api/v1/debts`
- `POST /api/v1/debts/{debtId}/simulate-payoff`
- `POST /api/v1/goals`
- `GET /api/v1/goals/{goalId}/projection`
- `POST /api/v1/assets`
- `GET /api/v1/net-worth`
- `POST /api/v1/forecasts`
- `GET /api/v1/forecasts/{forecastId}`

API rules:

- request and response DTOs must be separate from persistence entities
- validation belongs on request models and application boundaries
- use Problem Details style errors where practical
- support pagination on collection-heavy endpoints

## 13. Persistence Design

Use PostgreSQL with Flyway-managed schema evolution.

Expected early tables:

- `user_profiles`
- `financial_profiles`
- `incomes`
- `expenses`
- `budgets`
- `budget_categories`
- `debt_accounts`
- `savings_goals`
- `assets`
- `liabilities`
- `forecast_runs`
- `forecast_months`
- `audit_events`

Important persistence principles:

- every primary table should have a primary key
- use `created_at`
- use `updated_at` where mutation matters
- add indexes intentionally for query patterns
- snapshot forecast inputs where reproducibility matters

## 14. Forecasting Engine Design

The forecasting engine is the most strategically important module.

### Inputs

- current balances
- recurring incomes
- recurring expenses
- budgets
- debt terms
- savings goals
- asset assumptions
- scenario adjustments

### Outputs

- projected monthly cashflow
- projected debt balances
- projected savings balances
- projected goal completion dates
- projected net worth

### Core Questions It Must Answer

- extra debt payment impact
- extra savings contribution impact
- budget reduction impact
- new liability impact
- target date feasibility

### Recommended Design

Use a deterministic monthly projection engine.

Each run should:

1. freeze source inputs
2. transform them into month-by-month events
3. apply business rules in a clear order
4. produce a projection result per month
5. persist run metadata and optionally result snapshots

## 15. Security and Privacy

MVP can start without full auth implementation, but architecture should assume:

- Spring Security 7.x
- user-level data isolation
- secure password hashing
- audited destructive changes
- no leaking sensitive financial data

Sensitive data includes:

- salaries
- debt balances
- spending patterns
- savings targets
- net worth history

## 16. Observability

Include from early phases:

- structured logs
- health checks
- request correlation where useful
- audit events for important business actions
- metrics for forecast duration and report generation

## 17. Testing Strategy

### Unit Tests

Prioritize:

- money arithmetic
- budget analysis rules
- debt payoff calculations
- goal projections
- forecast engine behavior

### Integration Tests

Use:

- Spring Boot integration tests
- PostgreSQL-backed migration validation
- repository behavior tests

### Acceptance Tests

Important business scenarios:

- debt payoff simulation with extra payment
- savings goal completion timeline
- monthly budget vs actual comparison
- forecast reproducibility

## 18. Deployment and Runtime

Runtime baseline:

- Java 26
- Spring Boot 4.1.x
- PostgreSQL 16+
- Docker Compose for local infrastructure

Deploy initially as:

- one backend application
- one database

## 19. Roadmap

### Phase 1

- profile
- cashflow
- budget
- debt
- goals

### Phase 2

- assets and net worth
- monthly reports
- first forecast engine

### Phase 3

- richer what-if scenarios
- forecast run persistence
- exports and reporting polish

### Phase 4

- security
- advisor or household features
- notifications
- external integrations

## 20. Final Recommendation

Build this as a modular monolith focused on financial modelling quality, deterministic projections, and strong domain language.

The real differentiator is not transaction storage. It is turning personal financial data into understandable, trustworthy future scenarios.
