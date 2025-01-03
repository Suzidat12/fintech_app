package com.fintech.config;

import com.fintech.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanServiceHealthIndicator implements HealthIndicator {


    private final LoanRepository loanRepository;
    @Override
    public Health health() {
        try {
            // Check if the Loan repository is working
            long count = loanRepository.count();  // You can perform any other check here
            if (count >= 0) {
                return Health.up().withDetail("Loan Service", "Available").build();
            } else {
                return Health.down().withDetail("Loan Service", "Error accessing database").build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("Loan Service", "Database error").build();
        }
    }
}