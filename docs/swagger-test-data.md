# Swagger Test Data

This guide gives a complete sample scenario for testing the application through Swagger UI.

Use it when you want to:

- seed the app quickly with realistic financial data
- test the main product flows end to end
- avoid confusion between resource `id` values and `profileId` values

## Before You Start

Open Swagger UI:

- `http://localhost:8080/swagger-ui/index.html`

Authorize with the default local credentials unless you overrode them:

- username: `finance-admin`
- password: `finance-admin-password`

Important:

- Swagger often pre-fills example UUIDs like `3fa85f64-5717-4562-b3fc-2c963f66afa6`
- always replace those example values with real IDs returned by the API
- any endpoint under `/api/v1/profiles/{profileId}/...` must use the real `profileId`

## Recommended Test Order

1. Create the profile
2. Create incomes
3. Create expenses
4. Create a budget
5. Create a debt
6. Run a debt payoff simulation
7. Create a savings goal
8. Create assets
9. Run a forecast
10. Review overview, reports, and audit events

## 1. Create Profile

Endpoint:

- `POST /api/v1/profiles`

Request body:

```json
{
  "fullName": "Patrick Tester",
  "emailAddress": "patrick.tester@example.com",
  "employmentType": "SALARIED",
  "preferredCurrency": "USD",
  "countryCode": "US",
  "monthlyNetIncome": 6200.00,
  "monthlyHousingCost": 1800.00,
  "monthlyEssentialCosts": 950.00,
  "onboardingDate": "2026-06-01"
}
```

Save the returned `id` as your `profileId`.

## 2. Create Incomes

Endpoint:

- `POST /api/v1/profiles/{profileId}/incomes`

Income 1:

```json
{
  "sourceName": "Primary Salary",
  "amount": 6200.00,
  "currency": "USD",
  "frequency": "MONTHLY",
  "startDate": "2026-06-01",
  "endDate": null,
  "taxable": true
}
```

Income 2:

```json
{
  "sourceName": "Freelance Design",
  "amount": 800.00,
  "currency": "USD",
  "frequency": "MONTHLY",
  "startDate": "2026-06-01",
  "endDate": null,
  "taxable": true
}
```

Verify:

- `GET /api/v1/profiles/{profileId}/incomes`

## 3. Create Expenses

Endpoint:

- `POST /api/v1/profiles/{profileId}/expenses`

Expense 1:

```json
{
  "expenseName": "Apartment Rent",
  "amount": 1800.00,
  "currency": "USD",
  "category": "HOUSING",
  "frequency": "MONTHLY",
  "startDate": "2026-06-01",
  "endDate": null,
  "essential": true
}
```

Expense 2:

```json
{
  "expenseName": "Groceries",
  "amount": 650.00,
  "currency": "USD",
  "category": "ESSENTIAL",
  "frequency": "MONTHLY",
  "startDate": "2026-06-01",
  "endDate": null,
  "essential": true
}
```

Expense 3:

```json
{
  "expenseName": "Gym and Streaming",
  "amount": 180.00,
  "currency": "USD",
  "category": "LIFESTYLE",
  "frequency": "MONTHLY",
  "startDate": "2026-06-01",
  "endDate": null,
  "essential": false
}
```

Verify:

- `GET /api/v1/profiles/{profileId}/expenses`

## 4. Create Budget

Endpoint:

- `POST /api/v1/profiles/{profileId}/budgets`

Request body:

```json
{
  "monthStart": "2026-06-01",
  "housingTarget": 1900.00,
  "essentialsTarget": 1100.00,
  "lifestyleTarget": 400.00,
  "currency": "USD"
}
```

Save the returned budget `id` as `budgetPlanId`.

Verify:

- `GET /api/v1/profiles/{profileId}/budgets`
- `GET /api/v1/profiles/{profileId}/budgets/{budgetPlanId}/summary`

## 5. Create Debt

Endpoint:

- `POST /api/v1/profiles/{profileId}/debts`

Request body:

```json
{
  "lenderName": "City Bank Credit Card",
  "debtType": "CREDIT_CARD",
  "currentBalance": 5400.00,
  "annualInterestRate": 18.90,
  "minimumMonthlyPayment": 180.00,
  "currency": "USD"
}
```

Save the returned debt `id` as `debtAccountId`.

Verify:

- `GET /api/v1/profiles/{profileId}/debts`

## 6. Run Debt Payoff Simulation

Endpoint:

- `POST /api/v1/profiles/{profileId}/debts/{debtAccountId}/simulate-payoff`

Request body:

```json
{
  "extraMonthlyPayment": 220.00
}
```

What to look for:

- the payoff plan should show the debt shrinking over time
- using an extra payment should improve the payoff timeline relative to the minimum payment only case

## 7. Create Savings Goal

Endpoint:

- `POST /api/v1/profiles/{profileId}/goals`

Request body:

```json
{
  "goalName": "Emergency Fund",
  "goalType": "EMERGENCY_FUND",
  "targetAmount": 15000.00,
  "currentSaved": 3500.00,
  "currency": "USD",
  "targetDate": "2027-12-01"
}
```

Save the returned goal `id` as `goalId`.

Verify:

- `GET /api/v1/profiles/{profileId}/goals`
- `GET /api/v1/profiles/{profileId}/goals/{goalId}/projection`

## 8. Create Assets

Endpoint:

- `POST /api/v1/profiles/{profileId}/assets`

Asset 1:

```json
{
  "assetName": "High Yield Savings",
  "assetType": "CASH",
  "currentValue": 3500.00,
  "currency": "USD"
}
```

Asset 2:

```json
{
  "assetName": "Brokerage Account",
  "assetType": "INVESTMENT",
  "currentValue": 8200.00,
  "currency": "USD"
}
```

Verify:

- `GET /api/v1/profiles/{profileId}/assets`

## 9. Run Forecast

Endpoint:

- `POST /api/v1/profiles/{profileId}/forecasts`

Request body:

```json
{
  "months": 12,
  "extraDebtPayment": 220.00,
  "monthlyGoalContribution": 500.00,
  "expectedAnnualReturnRate": 6.00,
  "incomeShockMonth": null,
  "incomeShockAmount": null,
  "expenseShockMonth": 4,
  "expenseShockAmount": 1200.00
}
```

What to look for:

- the forecast should project month-by-month financial movement
- the month 4 expense shock should affect the result
- extra debt payment and goal contribution should influence the final summary

## 10. Review Summary Endpoints

Use these reads after the seed data is in place.

Overview:

- `GET /api/v1/profiles/{profileId}/overview`

Reports:

- `GET /api/v1/profiles/{profileId}/reports/monthly-cashflow?month=2026-06-01`
- `GET /api/v1/profiles/{profileId}/reports/debt-summary`

Audit trail:

- `GET /api/v1/profiles/{profileId}/audit-events`

## Expected High-Level Outcomes

With the sample data above, you should see:

- multiple income and expense records tied to the same profile
- a stored budget for June 2026
- one debt account with a valid payoff simulation
- one emergency fund goal with a projection
- two assets contributing to net worth
- a forecast that reacts to the configured shock month
- audit events showing the create actions

## Common Mistakes

### Wrong `profileId` in the URL

If you see a `404 Profile not found`, check whether you accidentally used:

- a Swagger example UUID
- an income `id`
- a debt `id`
- a goal `id`

instead of the real profile `id`

### Confusing resource `id` with `profileId`

Example:

- `/api/v1/profiles/{profileId}/incomes` needs the profile ID in the path
- the returned income `id` identifies the income record itself, not the profile

### Forgetting to re-use returned IDs

Keep track of:

- `profileId`
- `budgetPlanId`
- `debtAccountId`
- `goalId`

Those are needed for later steps.

