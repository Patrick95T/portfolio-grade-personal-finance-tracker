package za.co.patrick.finance.reports.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.cashflow.application.ExpenseEntryService;
import za.co.patrick.finance.cashflow.application.IncomeEntryService;
import za.co.patrick.finance.cashflow.domain.ExpenseCategory;
import za.co.patrick.finance.cashflow.domain.ExpenseEntry;
import za.co.patrick.finance.cashflow.domain.ExpenseFrequency;
import za.co.patrick.finance.cashflow.domain.IncomeEntry;
import za.co.patrick.finance.cashflow.domain.IncomeFrequency;
import za.co.patrick.finance.debt.application.DebtAccountService;
import za.co.patrick.finance.profile.application.UserProfileService;

@Service
@Transactional(readOnly = true)
public class FinancialReportsService {

    private static final BigDecimal TWELVE = BigDecimal.valueOf(12);
    private static final BigDecimal TWENTY_SIX = BigDecimal.valueOf(26);
    private static final BigDecimal FIFTY_TWO = BigDecimal.valueOf(52);

    private final UserProfileService userProfileService;
    private final IncomeEntryService incomeEntryService;
    private final ExpenseEntryService expenseEntryService;
    private final DebtAccountService debtAccountService;

    public FinancialReportsService(
            UserProfileService userProfileService,
            IncomeEntryService incomeEntryService,
            ExpenseEntryService expenseEntryService,
            DebtAccountService debtAccountService
    ) {
        this.userProfileService = userProfileService;
        this.incomeEntryService = incomeEntryService;
        this.expenseEntryService = expenseEntryService;
        this.debtAccountService = debtAccountService;
    }

    public MonthlyCashflowReportResponse getMonthlyCashflow(UUID profileId, LocalDate month) {
        userProfileService.getProfile(profileId);

        BigDecimal totalIncome = incomeEntryService.listProfileIncome(profileId).stream()
                .map(income -> projectedMonthlyAmount(month, income.startDate(), income.endDate(), income.frequency(), income.amount().amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var expenses = expenseEntryService.listProfileExpenses(profileId);
        BigDecimal housingExpenses = sumExpenseCategory(expenses, month, ExpenseCategory.HOUSING);
        BigDecimal essentialExpenses = sumExpenseCategory(expenses, month, ExpenseCategory.ESSENTIAL);
        BigDecimal lifestyleExpenses = sumExpenseCategory(expenses, month, ExpenseCategory.LIFESTYLE);
        BigDecimal totalExpenses = housingExpenses.add(essentialExpenses).add(lifestyleExpenses).setScale(2, RoundingMode.HALF_UP);

        return new MonthlyCashflowReportResponse(
                profileId,
                month,
                totalIncome.setScale(2, RoundingMode.HALF_UP),
                housingExpenses,
                essentialExpenses,
                lifestyleExpenses,
                totalExpenses,
                totalIncome.subtract(totalExpenses).setScale(2, RoundingMode.HALF_UP)
        );
    }

    public DebtSummaryReportResponse getDebtSummary(UUID profileId) {
        userProfileService.getProfile(profileId);
        var debts = debtAccountService.listDebtAccounts(profileId);
        BigDecimal totalDebt = debts.stream().map(debt -> debt.currentBalance().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMinimumPayments = debts.stream().map(debt -> debt.minimumMonthlyPayment().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal weightedInterest = totalDebt.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : debts.stream()
                        .map(debt -> debt.currentBalance().amount().multiply(debt.annualInterestRate()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(totalDebt, 2, RoundingMode.HALF_UP);

        return new DebtSummaryReportResponse(
                profileId,
                debts.size(),
                totalDebt.setScale(2, RoundingMode.HALF_UP),
                totalMinimumPayments.setScale(2, RoundingMode.HALF_UP),
                weightedInterest
        );
    }

    private BigDecimal sumExpenseCategory(java.util.List<ExpenseEntry> expenses, LocalDate month, ExpenseCategory category) {
        return expenses.stream()
                .filter(expense -> expense.category() == category)
                .map(expense -> projectedMonthlyAmount(month, expense.startDate(), expense.endDate(), expense.frequency(), expense.amount().amount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
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
            case WEEKLY -> amount.multiply(FIFTY_TWO).divide(TWELVE, 2, RoundingMode.HALF_UP);
            case BIWEEKLY -> amount.multiply(TWENTY_SIX).divide(TWELVE, 2, RoundingMode.HALF_UP);
            case MONTHLY -> amount.setScale(2, RoundingMode.HALF_UP);
            case ANNUAL -> amount.divide(TWELVE, 2, RoundingMode.HALF_UP);
            case ONE_OFF -> month.getYear() == startDate.getYear() && month.getMonth() == startDate.getMonth()
                    ? amount.setScale(2, RoundingMode.HALF_UP)
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
            case WEEKLY -> amount.multiply(FIFTY_TWO).divide(TWELVE, 2, RoundingMode.HALF_UP);
            case BIWEEKLY -> amount.multiply(TWENTY_SIX).divide(TWELVE, 2, RoundingMode.HALF_UP);
            case MONTHLY -> amount.setScale(2, RoundingMode.HALF_UP);
            case ANNUAL -> amount.divide(TWELVE, 2, RoundingMode.HALF_UP);
            case ONE_OFF -> month.getYear() == startDate.getYear() && month.getMonth() == startDate.getMonth()
                    ? amount.setScale(2, RoundingMode.HALF_UP)
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
}
