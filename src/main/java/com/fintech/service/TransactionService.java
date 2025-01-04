package com.fintech.service;

import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.ApplyTransactionRequest;
import com.fintech.dto.request.DisbursementRequest;
import com.fintech.dto.request.RepaymentRequest;
import com.fintech.dto.response.TransactionStatement;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Admin;
import com.fintech.model.Loan;
import com.fintech.model.Transactions;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.model.enums.LoanStatus;
import com.fintech.model.enums.TransactionType;
import com.fintech.repository.AdminRepository;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.TransactionRepository;
import com.fintech.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.fintech.dto.ApiResponse.ok;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final AdminRepository adminRepository;
    private final UserAccountRepository userAccountRepository;



    public ResponseEntity<ResponseDto<Transactions>> recordDisbursement(@RequestBody DisbursementRequest request) {
        Optional<Loan> loanOptional = loanRepository.findById(request.getLoanId());
        Optional<Admin> adminOptional = adminRepository.findById(request.getAdminId());
        if (loanOptional.isEmpty()) {
            throw new BadRequestException("Loan not found");
        }
        if (adminOptional.isEmpty()) {
            throw new BadRequestException("Admin not found");
        }
        Loan loan = loanOptional.get();
        Admin admin = adminOptional.get();
        UsersAccount userAccount = loan.getUser();
        BigDecimal newBalance = userAccount.getAccountBalance().add(request.getAmount());
        userAccount.setAccountBalance(newBalance);
        Transactions transaction = new Transactions();
        transaction.setLoan(loan);
        transaction.setVerifiedBy(admin);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.DISBURSEMENT);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(AppStatus.COMPLETED);
        loan.setStatus(LoanStatus.DISBURSED);
        loan.setNarration("Total amount of "+transaction.getAmount()+" "+ "has been disbursed into your account");
        loanRepository.save(loan);
        userAccountRepository.save(userAccount);
        transactionRepository.save(transaction);
        return ok(transaction,"Loan disbursed successfully");
    }

    public ResponseEntity<ResponseDto<Transactions>> recordRepayment(RepaymentRequest request) {
        Optional<Loan> loanOptional = loanRepository.findById(request.getLoanId());
        Optional<Admin> adminOptional = adminRepository.findById(request.getAdminId());
        if (loanOptional.isEmpty()) {
            throw new BadRequestException("Loan not found");
        }
        if (adminOptional.isEmpty()) {
            throw new BadRequestException("Admin not found");
        }
        Loan loan = loanOptional.get();
        Admin admin = adminOptional.get();
        UsersAccount userAccount = loan.getUser();
        BigDecimal newBalance = userAccount.getAccountBalance().subtract(request.getAmount());
        userAccount.setAccountBalance(newBalance);
        Transactions transaction = new Transactions();
        transaction.setLoan(loan);
        transaction.setVerifiedBy(admin);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.REPAYMENT);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(AppStatus.COMPLETED);
        loan.setStatus(LoanStatus.REPAID);
        loan.setNarration("Total amount of "+transaction.getAmount()+" "+ "has been recorded for your repayment");
        loanRepository.save(loan);
        transactionRepository.save(transaction);
        return ok(transaction,"Loan repayment successfully done");
    }
    public ResponseEntity<ResponseDto<Transactions>> applyTransaction(@RequestBody ApplyTransactionRequest request) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(request.getUserId());
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account not found");
        }

        UsersAccount usersAccount = usersAccountOptional.get();
        BigDecimal currentBalance = usersAccount.getAccountBalance();
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transaction amount must be positive");
        }
        if (TransactionType.WITHDRAWAL.name().equals(request.getTransactionType())) {
            if (currentBalance.compareTo(request.getAmount()) < 0) {
                throw new BadRequestException("Insufficient funds for this withdrawal");
            }
        }
        if (TransactionType.DEPOSIT.name().equals(request.getTransactionType())) {
            usersAccount.setAccountBalance(currentBalance.add(request.getAmount()));
        } else if (TransactionType.WITHDRAWAL.name().equals(request.getTransactionType())) {
            usersAccount.setAccountBalance(currentBalance.subtract(request.getAmount()));
        }
        userAccountRepository.save(usersAccount);
        Transactions transaction = new Transactions();
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setUser(usersAccount);
        transaction.setStatus(AppStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.valueOf(request.getTransactionType()));
        transactionRepository.save(transaction);
        return ok(transaction,"Transaction applied successfully");
    }

    public ResponseEntity<ResponseDto<List<TransactionStatement>>> generateTransactionStatementForUser(Long userId) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(userId);
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account not found");
        }
        UsersAccount usersAccount = usersAccountOptional.get();
        List<Transactions> transactions = transactionRepository.findAllByUser_Id(userId);
        transactions.sort(Comparator.comparing(Transactions::getTransactionDate));
        List<TransactionStatement> statement = new ArrayList<>();
        for (Transactions transaction : transactions) {
            TransactionStatement transactionStatement = new TransactionStatement();
            transactionStatement.setTransactionDate(transaction.getTransactionDate());
            transactionStatement.setTransactionType(transaction.getTransactionType());
            transactionStatement.setAmount(transaction.getAmount());
            transactionStatement.setBalanceAfterTransaction(usersAccount.getAccountBalance());
            statement.add(transactionStatement);
        }
        return ok(statement, "Transaction statement generated successfully");
    }
}
