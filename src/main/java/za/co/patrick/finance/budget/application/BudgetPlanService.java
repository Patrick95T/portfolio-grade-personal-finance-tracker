package za.co.patrick.finance.budget.application;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.patrick.finance.audit.application.AuditEventService;
import za.co.patrick.finance.audit.application.AuditEventType;
import za.co.patrick.finance.cashflow.application.ExpenseEntryService;
import za.co.patrick.finance.cashflow.domain.ExpenseCategory;
import za.co.patrick.finance.profile.application.UserProfileService;
import za.co.patrick.finance.shared.domain.Money;

@Service
@Transactional
public class BudgetPlanService {

    private final BudgetPlanRepository budgetPlanRepository;
    private final UserProfileService userProfileService;
    private final ExpenseEntryService expenseEntryService;
    private final AuditEventService auditEventService;
    private final Clock clock;

    public BudgetPlanService(
            BudgetPlanRepository budgetPlanRepository,
            UserProfileService userProfileService,
            ExpenseEntryService expenseEntryService,
            AuditEventService auditEventService,
            Clock clock
    ) {
        this.budgetPlanRepository = budgetPlanRepository;
        this.userProfileService = userProfileService;
        this.expenseEntryService = expenseEntryService;
        this.auditEventService = auditEventService;
        this.clock = clock;
    }

    public za.co.patrick.finance.budget.domain.BudgetPlan createBudgetPlan(CreateBudgetPlanCommand command) {
        var profile = userProfileService.getProfile(command.profileId());
        Instant now = Instant.now(clock);
        var budgetPlan = new za.co.patrick.finance.budget.domain.BudgetPlan(
                UUID.randomUUID(),
                command.profileId(),
                command.monthStart(),
                new Money(command.housingTarget(), profile.preferredCurrency()),
                new Money(command.essentialsTarget(), profile.preferredCurrency()),
                new Money(command.lifestyleTarget(), profile.preferredCurrency()),
                now,
                now
        );
        var savedBudgetPlan = budgetPlanRepository.save(budgetPlan);
        auditEventService.record(
                savedBudgetPlan.profileId(),
                AuditEventType.BUDGET_CREATED,
                "BudgetPlan",
                savedBudgetPlan.id(),
                "Created budget plan for " + savedBudgetPlan.monthStart()
        );
        return savedBudgetPlan;
    }

    @Transactional(readOnly = true)
    public List<za.co.patrick.finance.budget.domain.BudgetPlan> listBudgetPlans(UUID profileId) {
        userProfileService.getProfile(profileId);
        return budgetPlanRepository.findByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public BudgetSummaryView getBudgetSummary(UUID budgetPlanId) {
        var budgetPlan = budgetPlanRepository.findById(budgetPlanId)
                .orElseThrow(() -> new BudgetPlanNotFoundException(budgetPlanId));
        var expenses = expenseEntryService.listProfileExpensesForMonth(budgetPlan.profileId(), budgetPlan.monthStart());

        BigDecimal housing = sumForCategory(expenses, ExpenseCategory.HOUSING);
        BigDecimal essentials = sumForCategory(expenses, ExpenseCategory.ESSENTIAL);
        BigDecimal lifestyle = sumForCategory(expenses, ExpenseCategory.LIFESTYLE);

        return new BudgetSummaryView(
                budgetPlan.id(),
                budgetPlan.monthStart(),
                budgetPlan.housingTarget().amount(),
                housing,
                budgetPlan.essentialsTarget().amount(),
                essentials,
                budgetPlan.lifestyleTarget().amount(),
                lifestyle
        );
    }

    private BigDecimal sumForCategory(List<za.co.patrick.finance.cashflow.domain.ExpenseEntry> expenses, ExpenseCategory category) {
        return expenses.stream()
                .filter(expense -> expense.category() == category)
                .map(expense -> expense.amount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
