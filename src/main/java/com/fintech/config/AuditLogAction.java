package com.fintech.config;

import com.fintech.service.AuditLogService;
import com.fintech.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAction {
    private final AuditLogService auditLogService;

    @AfterReturning(value = "execution(* com.fintech.model.Loan.*.updateLoanStatus(..))", returning = "result")
    public void logLoanStatusChange(JoinPoint joinPoint, Object result) {
        // Get method arguments (loan id, new status, etc.)
        Object[] args = joinPoint.getArgs();
        Long loanId = (Long) args[0];
        String newStatus = (String) args[1];

        // Assuming you have access to the userId and adminId
        Long userId = SecurityUtil.getCurrentUserId();  // Extract from user context
        String adminId = SecurityUtil.getAdminIdFromContext();  // Extract from admin context
        String actionDetails = "Loan ID: " + loanId + " updated to status: " + newStatus;
        auditLogService.logAction("STATUS_CHANGE", actionDetails, userId, adminId);
    }
}
