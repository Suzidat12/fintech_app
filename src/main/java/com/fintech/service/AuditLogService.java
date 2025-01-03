package com.fintech.service;

import com.fintech.model.AuditLog;
import com.fintech.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public void logAction(String actionType, String details, Long userId, String adminId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setActionType(actionType);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setUserId(userId);
        auditLog.setAdminId(adminId);
        auditLogRepository.save(auditLog);
    }
}
