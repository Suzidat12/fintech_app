package com.fintech.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentRequest {
   private Long loanId;
   private Long adminId;
   private BigDecimal amount;
}
