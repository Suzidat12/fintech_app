package com.fintech.controller;


import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.DisbursementRequest;
import com.fintech.dto.request.UpdateLoanStatusRequest;
import com.fintech.model.Loan;
import com.fintech.model.Transactions;
import com.fintech.model.UsersAccount;
import com.fintech.service.AdminService;
import com.fintech.service.TransactionService;
import com.fintech.service.UsersAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {
    private final AdminService adminService;
    private final TransactionService transactionService;
    private final UsersAccountService usersAccountService;
    @PostMapping("/verify-user")
    public  ResponseEntity<ResponseDto<String>> verifyUserAccount(@RequestParam("userId") Long userId,@RequestParam("adminId") Long adminId){
        return adminService.verifyUserAccount(userId, adminId);
    }

    @PutMapping("/loan-status")
    public ResponseEntity<ResponseDto<Loan>> updateLoanStatus(@RequestBody UpdateLoanStatusRequest request){
        return adminService.updateLoanStatus(request);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<ResponseDto<String>> delete(@RequestParam("id") Long id){
        return usersAccountService.delete(id);
    }

    @GetMapping("/all-users")
    public  ResponseEntity<ResponseDto<List<UsersAccount>>> retrieve(){
        return usersAccountService.retrieve();
    }

    @PostMapping("/record-disbursement")
    public  ResponseEntity<ResponseDto<Transactions>> recordDisbursement(@RequestBody DisbursementRequest request) {
        return transactionService.recordDisbursement(request);
    }

    @PostMapping("/record-repayment")
    public  ResponseEntity<ResponseDto<Transactions>> recordRepayment(@RequestBody DisbursementRequest request) {
        return transactionService.recordRepayment(request);
    }


    }
