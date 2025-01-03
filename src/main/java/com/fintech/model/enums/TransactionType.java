package com.fintech.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
  DISBURSEMENT("DISBURSEMENT"),
  WITHDRAWAL("WITHDRAWAL"),
  DEPOSIT("DEPOSIT"),
    REPAYMENT("REPAYMENT");
    private final String transactionType;
}
