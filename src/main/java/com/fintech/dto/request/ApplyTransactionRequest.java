package com.fintech.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplyTransactionRequest {
   private Long userId;
   private String transactionType;
   private BigDecimal amount;
}
