# Forecasting Rules

This document captures the current deterministic forecast behavior implemented in the application.

## Input Snapshot Rules

- forecasts read the current stored profile, income, expense, debt, goal, and asset records at request time
- the endpoint does not yet persist forecast runs, so reproducibility currently depends on stable stored inputs and the explicit request body
- the forecast request must provide a `startMonth` on the first day of a month and a horizon between 1 and 60 months

## Monthly Processing Order

For each projected month the engine applies rules in this order:

1. determine active income entries and normalize recurring amounts to monthly values
2. determine active expense entries and normalize recurring amounts to monthly values
3. apply scenario income and expense adjustments
4. accrue debt interest
5. apply minimum debt payments
6. apply extra debt payment using an avalanche-style priority
7. update assets using net cashflow after expenses and debt payments
8. allocate requested savings contribution across goals
9. calculate debt balance, goal progress, and net worth for the month

## Recurring Amount Normalization

- `MONTHLY`: full amount each active month
- `WEEKLY`: `amount * 52 / 12`
- `BIWEEKLY`: `amount * 26 / 12`
- `ANNUAL`: `amount / 12`
- `ONE_OFF`: full amount only in the month of the start date

## Debt Rules

- interest accrues monthly as `balance * annualInterestRate / 100 / 12`
- minimum monthly payments are applied to every debt first
- any extra debt payment is then applied to the highest-interest debt first
- ties are broken by debt creation time

## Savings Goal Rules

- savings contributions cannot exceed positive net cashflow after debt payments
- contributions cannot exceed the remaining total goal gap
- contributions are allocated to goals by earliest target date first, then creation time
- goal progress is tracked separately from total assets; it represents labelled progress within overall assets

## Net Worth Logic

- projected assets start from the stored asset total
- each month, assets move by net cashflow after expenses and debt payments
- projected net worth is `projectedAssets - projectedDebt`
- the engine does not currently model separate cash shortfall tracking once assets are depleted

## Scenario Override Precedence

- scenario adjustments are applied after the baseline recurring monthly amount is derived from stored records
- request values override nothing in storage; they only affect the generated projection response

## Determinism Limits

- the engine is deterministic for a given set of persisted financial inputs and the same request body
- historical forecast snapshots are not yet stored, so the same request can produce different future results if the underlying financial records are changed later
