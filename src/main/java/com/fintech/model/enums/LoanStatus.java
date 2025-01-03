package com.fintech.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoanStatus {
    APPLIED("APPLIED"),
    DISBURSED("DISBURSED"),
    PENDING("PENDING"),
    REPAID("REPAID"),
    OUTSTANDING("OUTSTANDING"),
    REJECTED("REJECTED"),
    APPROVED("APPROVED");
    private final String loanStatus;
}
