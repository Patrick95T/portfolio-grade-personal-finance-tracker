package za.co.patrick.finance.shared.api;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import za.co.patrick.finance.budget.application.BudgetPlanNotFoundException;
import za.co.patrick.finance.debt.application.DebtAccountNotFoundException;
import za.co.patrick.finance.goals.application.SavingsGoalNotFoundException;
import za.co.patrick.finance.profile.application.ProfileNotFoundException;
import za.co.patrick.finance.shared.domain.BusinessRuleViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ProfileNotFoundException.class)
    ProblemDetail handleProfileNotFound(ProfileNotFoundException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.NOT_FOUND, "Profile not found", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BudgetPlanNotFoundException.class)
    ProblemDetail handleBudgetPlanNotFound(BudgetPlanNotFoundException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.NOT_FOUND, "Budget plan not found", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DebtAccountNotFoundException.class)
    ProblemDetail handleDebtAccountNotFound(DebtAccountNotFoundException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.NOT_FOUND, "Debt account not found", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(SavingsGoalNotFoundException.class)
    ProblemDetail handleSavingsGoalNotFound(SavingsGoalNotFoundException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.NOT_FOUND, "Savings goal not found", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    ProblemDetail handleBusinessRuleViolation(BusinessRuleViolationException exception, HttpServletRequest request) {
        return problemDetail(HttpStatus.BAD_REQUEST, "Business rule violation", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        var problem = problemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more request fields are invalid",
                request.getRequestURI()
        );
        var errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (left, right) -> left
                ));
        problem.setProperty("errors", errors);
        return problem;
    }

    private ProblemDetail problemDetail(HttpStatus status, String title, String detail, String path) {
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("https://api.portfoliofinancetracker.local/problems/" + status.value()));
        problem.setProperty("timestamp", OffsetDateTime.now());
        problem.setProperty("path", path);
        return problem;
    }
}
