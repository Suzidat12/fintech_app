package com.fintech.service;

import com.fintech.dto.ResponseDto;
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



    public ResponseEntity<ResponseDto<Transactions>> recordDisbursement(Long loanId, Long adminId, BigDecimal amount) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (loanOptional.isEmpty()) {
            throw new BadRequestException("Loan not found");
        }
        if (adminOptional.isEmpty()) {
            throw new BadRequestException("Admin not found");
        }
        Loan loan = loanOptional.get();
        Admin admin = adminOptional.get();
        Transactions transaction = new Transactions();
        transaction.setLoan(loan);
        transaction.setVerifiedBy(admin);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DISBURSEMENT);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(AppStatus.COMPLETED);
        loan.setStatus(LoanStatus.DISBURSED);
        loanRepository.save(loan);
        transactionRepository.save(transaction);
        return ok(transaction,"Loan disbursed successfully");
    }

    public ResponseEntity<ResponseDto<Transactions>> recordRepayment(Long loanId, Long adminId, BigDecimal amount) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (loanOptional.isEmpty()) {
            throw new BadRequestException("Loan not found");
        }
        if (adminOptional.isEmpty()) {
            throw new BadRequestException("Admin not found");
        }
        Loan loan = loanOptional.get();
        Admin admin = adminOptional.get();
        Transactions transaction = new Transactions();
        transaction.setLoan(loan);
        transaction.setVerifiedBy(admin);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.REPAYMENT);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(AppStatus.COMPLETED);
        loan.setStatus(LoanStatus.REPAID);
        loanRepository.save(loan);
        transactionRepository.save(transaction);
        return ok(transaction,"Loan repayment successfully done");
    }
    public ResponseEntity<ResponseDto<Transactions>> applyTransaction(Long accountId, BigDecimal amount, String transactionType) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(accountId);
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account not found");
        }

        UsersAccount usersAccount = usersAccountOptional.get();
        BigDecimal currentBalance = usersAccount.getAccountBalance();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Transaction amount must be positive");
        }
        if (TransactionType.WITHDRAWAL.name().equals(transactionType)) {
            if (currentBalance.compareTo(amount) < 0) {
                throw new BadRequestException("Insufficient funds for this withdrawal");
            }
        }
        if (TransactionType.DEPOSIT.name().equals(transactionType)) {
            usersAccount.setAccountBalance(currentBalance.add(amount));
        } else if (TransactionType.WITHDRAWAL.name().equals(transactionType)) {
            usersAccount.setAccountBalance(currentBalance.subtract(amount));
        }
        userAccountRepository.save(usersAccount);
        Transactions transaction = new Transactions();
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setUser(usersAccount);
        transaction.setTransactionType(TransactionType.valueOf(transactionType));
        transactionRepository.save(transaction);
        return ok(transaction,"Transaction applied successfully");
    }

    public ResponseEntity<ResponseDto<List<TransactionStatement>>> generateTransactionStatementForUser(Long userId, String startDate, String endDate) {
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
