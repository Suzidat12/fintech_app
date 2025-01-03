package com.fintech.dto.request;

import lombok.Data;

@Data
public class UpdateLoanStatusRequest {
    private Long loanId;
    private Long adminId;
    private String loanStatus;
}
