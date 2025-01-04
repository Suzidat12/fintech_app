package com.fintech.controller;


import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.ApplyTransactionRequest;
import com.fintech.dto.request.LoanRequest;
import com.fintech.dto.request.UpdateLoanStatusRequest;
import com.fintech.dto.request.UserAccountRequest;
import com.fintech.dto.response.TransactionStatement;
import com.fintech.model.Loan;
import com.fintech.model.Transactions;
import com.fintech.model.UsersAccount;
import com.fintech.service.LoanService;
import com.fintech.service.TransactionService;
import com.fintech.service.UsersAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserAccountController {
    private final UsersAccountService usersAccountService;
    private final TransactionService transactionService;
    private final LoanService loanService;
    @PutMapping("/update-user")
    public ResponseEntity<ResponseDto<UsersAccount>> update(@RequestBody UserAccountRequest request,@RequestParam("id") Long id){
        return usersAccountService.update(request, id);
    }

    @PostMapping("/apply-loan")
    public ResponseEntity<ResponseDto<Loan>> applyForLoan(@RequestBody LoanRequest loanRequest){
        return loanService.applyForLoan(loanRequest);
    }
    @GetMapping("/fetch-loan")
    public  ResponseEntity<ResponseDto<List<Loan>>> getLoanByUserId(@RequestParam("userId") Long userId){
        return loanService.getLoanByUserId(userId);
    }

    @PostMapping("/apply-transaction")
    public  ResponseEntity<ResponseDto<Transactions>> applyTransaction(@RequestBody ApplyTransactionRequest request){
        return transactionService.applyTransaction(request);
    }

    @GetMapping("/fetch-transaction-statement")
    public  ResponseEntity<ResponseDto<List<TransactionStatement>>> generateTransactionStatementForUser(@RequestParam("userId") Long userId) {
        return transactionService.generateTransactionStatementForUser(userId);
    }



}
