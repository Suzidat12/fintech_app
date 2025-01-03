package com.fintech.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private Long userId;
    private BigDecimal loanAmount;
    private int tenure;
}
