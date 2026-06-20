package za.co.patrick.finance.forecast.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.cashflow.application.ExpenseEntryService;
import za.co.patrick.finance.cashflow.application.IncomeEntryService;
import za.co.patrick.finance.cashflow.domain.ExpenseEntry;
import za.co.patrick.finance.cashflow.domain.ExpenseFrequency;
import za.co.patrick.finance.cashflow.domain.IncomeEntry;
import za.co.patrick.finance.cashflow.domain.IncomeFrequency;
import za.co.patrick.finance.debt.application.DebtAccountService;
import za.co.patrick.finance.debt.domain.DebtAccount;
import za.co.patrick.finance.forecast.domain.ForecastMonthProjection;
import za.co.patrick.finance.forecast.domain.ForecastResult;
import za.co.patrick.finance.forecast.domain.ForecastScenario;
import za.co.patrick.finance.forecast.domain.ForecastSummary;
import za.co.patrick.finance.goals.application.SavingsGoalService;
import za.co.patrick.finance.goals.domain.SavingsGoal;
import za.co.patrick.finance.networth.application.AssetRecordService;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;

@Service
@Transactional(readOnly = true)
public class ForecastService {

    private static final int MAX_FORECAST_MONTHS = 60;
    private static final BigDecimal TWELVE = BigDecimal.valueOf(12);
    private static final BigDecimal TWENTY_SIX = BigDecimal.valueOf(26);
    private static final BigDecimal FIFTY_TWO = BigDecimal.valueOf(52);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final UserProfileService userProfileService;
    private final IncomeEntryService incomeEntryService;
    private final ExpenseEntryService expenseEntryService;
    private final DebtAccountService debtAccountService;
    private final SavingsGoalService savingsGoalService;
    private final AssetRecordService assetRecordService;

    public ForecastService(
            UserProfileService userProfileService,
            IncomeEntryService incomeEntryService,
            ExpenseEntryService expenseEntryService,
            DebtAccountService debtAccountService,
            SavingsGoalService savingsGoalService,
            AssetRecordService assetRecordService
    ) {
        this.userProfileService = userProfileService;
        this.incomeEntryService = incomeEntryService;
        this.expenseEntryService = expenseEntryService;
        this.debtAccountService = debtAccountService;
        this.savingsGoalService = savingsGoalService;
        this.assetRecordService = assetRecordService;
    }

    public ForecastResult generateForecast(GenerateForecastCommand command) {
        validate(command);

        var profile = userProfileService.getProfile(command.profileId());
        var incomeEntries = incomeEntryService.listProfileIncome(command.profileId());
        var expenseEntries = expenseEntryService.listProfileExpenses(command.profileId());
        var debtStates = debtAccountService.listDebtAccounts(command.profileId()).stream()
                .map(DebtState::from)
                .toList();
        var goalStates = savingsGoalService.listSavingsGoals(command.profileId()).stream()
                .map(GoalState::from)
                .toList();

        BigDecimal projectedAssets = assetRecordService.listAssetRecords(command.profileId()).stream()
                .map(asset -> asset.currentValue().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal projectedDebt = debtStates.stream()
                .map(DebtState::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal startingNetWorth = scale(projectedAssets.subtract(projectedDebt));
        BigDecimal totalGoalTarget = goalStates.stream()
                .map(GoalState::targetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ForecastMonthProjection> monthlyProjections = new ArrayList<>();
        BigDecimal totalInterestAccrued = BigDecimal.ZERO;
        LocalDate projectedDebtFreeMonth = projectedDebt.compareTo(BigDecimal.ZERO) == 0 ? command.startMonth() : null;
        LocalDate projectedGoalCompletionMonth = totalGoalTarget.compareTo(BigDecimal.ZERO) == 0 ? command.startMonth() : null;

        for (int monthOffset = 0; monthOffset < command.months(); monthOffset++) {
            LocalDate month = command.startMonth().plusMonths(monthOffset);

            BigDecimal income = scale(monthlyIncomeFor(month, incomeEntries).add(command.monthlyIncomeAdjustment()).max(BigDecimal.ZERO));
            BigDecimal expenses = scale(monthlyExpensesFor(month, expenseEntries).add(command.monthlyExpenseAdjustment()).max(BigDecimal.ZERO));

            BigDecimal interestAccrued = accrueDebtInterest(debtStates);
            BigDecimal debtPayment = applyDebtPayments(debtStates, command.extraDebtPayment());
            BigDecimal netCashflowAfterDebt = scale(income.subtract(expenses).subtract(debtPayment));

            projectedAssets = scale(projectedAssets.add(netCashflowAfterDebt).max(BigDecimal.ZERO));
            BigDecimal savingsContribution = allocateSavings(goalStates, command.monthlySavingsContribution(), netCashflowAfterDebt);
            projectedDebt = currentDebtBalance(debtStates);
            BigDecimal goalSaved = currentGoalSaved(goalStates);
            BigDecimal netWorth = scale(projectedAssets.subtract(projectedDebt));

            totalInterestAccrued = scale(totalInterestAccrued.add(interestAccrued));
            if (projectedDebtFreeMonth == null && projectedDebt.compareTo(BigDecimal.ZERO) == 0) {
                projectedDebtFreeMonth = month;
            }
            if (projectedGoalCompletionMonth == null
                    && totalGoalTarget.compareTo(BigDecimal.ZERO) > 0
                    && goalSaved.compareTo(totalGoalTarget) >= 0) {
                projectedGoalCompletionMonth = month;
            }

            monthlyProjections.add(new ForecastMonthProjection(
                    month,
                    income,
                    expenses,
                    debtPayment,
                    interestAccrued,
                    savingsContribution,
                    netCashflowAfterDebt,
                    projectedAssets,
                    projectedDebt,
                    netWorth,
                    goalSaved
            ));
        }

        ForecastSummary summary = new ForecastSummary(
                startingNetWorth,
                projectedAssets,
                projectedDebt,
                scale(projectedAssets.subtract(projectedDebt)),
                totalInterestAccrued,
                currentGoalSaved(goalStates),
                projectedDebtFreeMonth,
                projectedGoalCompletionMonth
        );

        ForecastScenario scenario = new ForecastScenario(
                command.startMonth(),
                command.months(),
                scale(command.extraDebtPayment()),
                scale(command.monthlySavingsContribution()),
                scale(command.monthlyIncomeAdjustment()),
                scale(command.monthlyExpenseAdjustment())
        );

        return new ForecastResult(
                command.profileId(),
                profile.preferredCurrency(),
                scenario,
                summary,
                monthlyProjections
        );
    }

    private void validate(GenerateForecastCommand command) {
        if (command.startMonth() == null) {
            throw new IllegalArgumentException("startMonth is required");
        }
        if (command.startMonth().getDayOfMonth() != 1) {
            throw new BusinessRuleViolationException("Forecast start month must be the first day of a month");
        }
        if (command.months() < 1 || command.months() > MAX_FORECAST_MONTHS) {
            throw new BusinessRuleViolationException("Forecast horizon must be between 1 and 60 months");
        }
        if (command.extraDebtPayment().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException("Extra debt payment cannot be negative");
        }
        if (command.monthlySavingsContribution().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolationException("Monthly savings contribution cannot be negative");
        }
    }

    private BigDecimal monthlyIncomeFor(LocalDate month, List<IncomeEntry> incomeEntries) {
        return incomeEntries.stream()
                .map(income -> projectedMonthlyAmount(month, income.startDate(), income.endDate(), income.frequency(), income.amount().amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal monthlyExpensesFor(LocalDate month, List<ExpenseEntry> expenseEntries) {
        return expenseEntries.stream()
                .map(expense -> projectedMonthlyAmount(month, expense.startDate(), expense.endDate(), expense.frequency(), expense.amount().amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal projectedMonthlyAmount(
            LocalDate month,
            LocalDate startDate,
            LocalDate endDate,
            IncomeFrequency frequency,
            BigDecimal amount
    ) {
        if (!isActive(month, startDate, endDate)) {
            return BigDecimal.ZERO;
        }
        return switch (frequency) {
            case WEEKLY -> scale(amount.multiply(FIFTY_TWO).divide(TWELVE, 2, RoundingMode.HALF_UP));
            case BIWEEKLY -> scale(amount.multiply(TWENTY_SIX).divide(TWELVE, 2, RoundingMode.HALF_UP));
            case MONTHLY -> scale(amount);
            case ANNUAL -> scale(amount.divide(TWELVE, 2, RoundingMode.HALF_UP));
            case ONE_OFF -> month.getYear() == startDate.getYear() && month.getMonth() == startDate.getMonth()
                    ? scale(amount)
                    : BigDecimal.ZERO;
        };
    }

    private BigDecimal projectedMonthlyAmount(
            LocalDate month,
            LocalDate startDate,
            LocalDate endDate,
            ExpenseFrequency frequency,
            BigDecimal amount
    ) {
        if (!isActive(month, startDate, endDate)) {
            return BigDecimal.ZERO;
        }
        return switch (frequency) {
            case WEEKLY -> scale(amount.multiply(FIFTY_TWO).divide(TWELVE, 2, RoundingMode.HALF_UP));
            case BIWEEKLY -> scale(amount.multiply(TWENTY_SIX).divide(TWELVE, 2, RoundingMode.HALF_UP));
            case MONTHLY -> scale(amount);
            case ANNUAL -> scale(amount.divide(TWELVE, 2, RoundingMode.HALF_UP));
            case ONE_OFF -> month.getYear() == startDate.getYear() && month.getMonth() == startDate.getMonth()
                    ? scale(amount)
                    : BigDecimal.ZERO;
        };
    }

    private boolean isActive(LocalDate month, LocalDate startDate, LocalDate endDate) {
        LocalDate monthEnd = month.withDayOfMonth(month.lengthOfMonth());
        if (startDate.isAfter(monthEnd)) {
            return false;
        }
        return endDate == null || !endDate.isBefore(month);
    }

    private BigDecimal accrueDebtInterest(List<DebtState> debtStates) {
        BigDecimal total = BigDecimal.ZERO;
        for (DebtState debtState : debtStates) {
            if (debtState.balance().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal interest = scale(
                    debtState.balance()
                            .multiply(debtState.annualInterestRate())
                            .divide(ONE_HUNDRED, 8, RoundingMode.HALF_UP)
                            .divide(TWELVE, 2, RoundingMode.HALF_UP)
            );
            debtState.addToBalance(interest);
            total = total.add(interest);
        }
        return scale(total);
    }

    private BigDecimal applyDebtPayments(List<DebtState> debtStates, BigDecimal extraDebtPayment) {
        BigDecimal minimumPayments = BigDecimal.ZERO;
        for (DebtState debtState : debtStates) {
            if (debtState.balance().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal payment = scale(debtState.minimumMonthlyPayment().min(debtState.balance()));
            debtState.subtractFromBalance(payment);
            minimumPayments = minimumPayments.add(payment);
        }

        BigDecimal remainingExtra = extraDebtPayment;
        for (DebtState debtState : debtStates.stream()
                .sorted(Comparator.comparing(DebtState::annualInterestRate).reversed().thenComparing(DebtState::createdAt))
                .toList()) {
            if (remainingExtra.compareTo(BigDecimal.ZERO) <= 0 || debtState.balance().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal payment = scale(remainingExtra.min(debtState.balance()));
            debtState.subtractFromBalance(payment);
            remainingExtra = remainingExtra.subtract(payment);
        }

        return scale(minimumPayments.add(extraDebtPayment.subtract(remainingExtra)));
    }

    private BigDecimal allocateSavings(List<GoalState> goalStates, BigDecimal monthlySavingsContribution, BigDecimal netCashflowAfterDebt) {
        if (monthlySavingsContribution.compareTo(BigDecimal.ZERO) <= 0 || netCashflowAfterDebt.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal remainingGoalGap = goalStates.stream()
                .map(goal -> goal.targetAmount().subtract(goal.currentSaved()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (remainingGoalGap.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal remainingContribution = scale(monthlySavingsContribution.min(netCashflowAfterDebt).min(remainingGoalGap));

        for (GoalState goalState : goalStates.stream()
                .sorted(Comparator.comparing(GoalState::targetDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(GoalState::createdAt))
                .toList()) {
            if (remainingContribution.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            BigDecimal gap = goalState.targetAmount().subtract(goalState.currentSaved());
            if (gap.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal allocation = scale(remainingContribution.min(gap));
            goalState.addToSaved(allocation);
            remainingContribution = remainingContribution.subtract(allocation);
        }

        return scale(monthlySavingsContribution.min(netCashflowAfterDebt).min(remainingGoalGap));
    }

    private BigDecimal currentDebtBalance(List<DebtState> debtStates) {
        return scale(debtStates.stream()
                .map(DebtState::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal currentGoalSaved(List<GoalState> goalStates) {
        return scale(goalStates.stream()
                .map(GoalState::currentSaved)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal scale(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private static final class DebtState {
        private final BigDecimal minimumMonthlyPayment;
        private final BigDecimal annualInterestRate;
        private final java.time.Instant createdAt;
        private BigDecimal balance;

        private DebtState(
                BigDecimal balance,
                BigDecimal minimumMonthlyPayment,
                BigDecimal annualInterestRate,
                java.time.Instant createdAt
        ) {
            this.balance = balance;
            this.minimumMonthlyPayment = minimumMonthlyPayment;
            this.annualInterestRate = annualInterestRate;
            this.createdAt = createdAt;
        }

        static DebtState from(DebtAccount debtAccount) {
            return new DebtState(
                    debtAccount.currentBalance().amount(),
                    debtAccount.minimumMonthlyPayment().amount(),
                    debtAccount.annualInterestRate(),
                    debtAccount.createdAt()
            );
        }

        BigDecimal balance() {
            return balance;
        }

        BigDecimal minimumMonthlyPayment() {
            return minimumMonthlyPayment;
        }

        BigDecimal annualInterestRate() {
            return annualInterestRate;
        }

        java.time.Instant createdAt() {
            return createdAt;
        }

        void addToBalance(BigDecimal amount) {
            balance = balance.add(amount);
        }

        void subtractFromBalance(BigDecimal amount) {
            balance = balance.subtract(amount).max(BigDecimal.ZERO);
        }
    }

    private static final class GoalState {
        private final BigDecimal targetAmount;
        private final LocalDate targetDate;
        private final java.time.Instant createdAt;
        private BigDecimal currentSaved;

        private GoalState(
                BigDecimal targetAmount,
                BigDecimal currentSaved,
                LocalDate targetDate,
                java.time.Instant createdAt
        ) {
            this.targetAmount = targetAmount;
            this.currentSaved = currentSaved;
            this.targetDate = targetDate;
            this.createdAt = createdAt;
        }

        static GoalState from(SavingsGoal savingsGoal) {
            return new GoalState(
                    savingsGoal.targetAmount().amount(),
                    savingsGoal.currentSaved().amount(),
                    savingsGoal.targetDate(),
                    savingsGoal.createdAt()
            );
        }

        BigDecimal targetAmount() {
            return targetAmount;
        }

        BigDecimal currentSaved() {
            return currentSaved;
        }

        LocalDate targetDate() {
            return targetDate;
        }

        java.time.Instant createdAt() {
            return createdAt;
        }

        void addToSaved(BigDecimal amount) {
            currentSaved = currentSaved.add(amount).min(targetAmount);
        }
    }
}
