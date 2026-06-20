package za.co.patrick.finance.reports.application;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.cashflow.application.ExpenseEntryService;
import za.co.patrick.finance.cashflow.application.IncomeEntryService;
import za.co.patrick.finance.debt.application.DebtAccountService;
import za.co.patrick.finance.goals.application.SavingsGoalService;
import za.co.patrick.finance.networth.application.AssetRecordService;
import za.co.patrick.finance.profile.application.UserProfileService;

@Service
@Transactional(readOnly = true)
public class FinancialOverviewService {
    private final UserProfileService userProfileService;
    private final IncomeEntryService incomeEntryService;
    private final ExpenseEntryService expenseEntryService;
    private final DebtAccountService debtAccountService;
    private final SavingsGoalService savingsGoalService;
    private final AssetRecordService assetRecordService;

    public FinancialOverviewService(
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

    public FinancialOverviewResponse getOverview(UUID profileId) {
        userProfileService.getProfile(profileId);
        var goals = savingsGoalService.listSavingsGoals(profileId);
        BigDecimal income = incomeEntryService.listProfileIncome(profileId).stream().map(entry -> entry.amount().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expenses = expenseEntryService.listProfileExpenses(profileId).stream().map(entry -> entry.amount().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debt = debtAccountService.listDebtAccounts(profileId).stream().map(entry -> entry.currentBalance().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal assets = assetRecordService.listAssetRecords(profileId).stream().map(entry -> entry.currentValue().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal goalTarget = goals.stream().map(entry -> entry.targetAmount().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal goalSaved = goals.stream().map(entry -> entry.currentSaved().amount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new FinancialOverviewResponse(
                profileId,
                income,
                expenses,
                income.subtract(expenses),
                debt,
                assets,
                assets.subtract(debt),
                goalTarget,
                goalSaved
        );
    }
}
