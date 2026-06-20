# Domain Model Notes

This document captures the domain language and modelling assumptions that shape the system.

It is intended to keep the codebase aligned across:

- API contracts
- application services
- persistence models
- reports
- forecasting logic

## Ubiquitous Language

### Profile

The financial identity and planning baseline for a single user.

It carries:

- personal identity fields relevant to the domain
- preferred currency
- employment type
- baseline monthly assumptions

The `Profile` is the anchor for nearly every other module.

### Income

Money expected to flow into a profile according to a defined schedule.

Examples:

- salary
- freelance earnings
- rental income
- once-off side income

Important qualities:

- amount
- frequency
- taxability
- active date range

### Expense

Money expected to flow out of a profile according to a defined schedule.

Examples:

- rent
- groceries
- transport
- subscriptions

Important qualities:

- amount
- category
- essential vs discretionary classification
- active date range

### Budget

A planning target for how much a profile intends to spend in a given month across broad spending buckets.

In this project the budget is not just storage. It is a decision boundary that helps compare intended spending against recurring financial reality.

### Debt Account

A liability with an outstanding balance, interest assumptions, and expected repayment behavior.

Examples:

- credit card
- personal loan
- student loan
- mortgage

It is used both for current financial position and payoff simulation.

### Savings Goal

A target outcome defined by a desired amount and optionally a desired date.

Examples:

- emergency fund
- house deposit
- travel fund
- investment contribution target

The system uses this concept for progress reporting and projection.

### Asset

Something owned by the profile with measurable financial value.

Examples:

- savings account
- brokerage account
- property
- retirement account

Assets contribute positively to net worth.

### Liability

Something owed by the profile with measurable financial value.

In the current implementation, debt accounts are the primary operational liabilities.

Liabilities reduce net worth and affect forecast outcomes.

### Net Worth

The profile's financial position at a point in time.

Formula:

- total assets minus total liabilities

Net worth is not just a report value. It is one of the main outcomes the user is trying to improve over time.

### Forecast

A deterministic projection of future financial outcomes over a defined horizon.

A forecast uses known financial inputs plus explicit scenario assumptions to produce month-by-month outputs.

Important property:

- the result should be explainable and reproducible from the inputs used

### Scenario

A set of explicit adjustments applied to the baseline financial picture when generating a forecast or simulation.

Examples:

- extra debt repayment
- increased savings contribution
- one-time expense shock
- income reduction

Scenarios exist to answer "what changes if..." questions without mutating the source records.

## Modelling Focus

This document will capture:

- aggregates
- value objects
- invariants
- entity lifecycles
- relationships across profile, cashflow, budget, debt, goals, net worth, and forecast modules

It should stay aligned with the ubiquitous language defined in [architecture.md](../architecture.md).
