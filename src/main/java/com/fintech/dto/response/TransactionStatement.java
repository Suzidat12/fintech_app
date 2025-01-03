package com.fintech.dto.response;

import com.fintech.model.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionStatement {
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
}
