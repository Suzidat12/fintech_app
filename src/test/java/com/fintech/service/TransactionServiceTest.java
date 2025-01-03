package com.fintech.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.ApplyTransactionRequest;
import com.fintech.dto.request.DisbursementRequest;
import com.fintech.dto.response.TransactionStatement;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Admin;
import com.fintech.model.Loan;
import com.fintech.model.Transactions;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.LoanStatus;
import com.fintech.model.enums.TransactionType;
import com.fintech.repository.AdminRepository;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.TransactionRepository;
import com.fintech.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private DisbursementRequest disbursementRequest;
    private ApplyTransactionRequest applyTransactionRequest;
    private UsersAccount usersAccount;
    private Loan loan;
    private Admin admin;
    private Transactions transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock data
        disbursementRequest = new DisbursementRequest();
        disbursementRequest.setLoanId(1L);
        disbursementRequest.setAdminId(1L);
        disbursementRequest.setAmount(BigDecimal.valueOf(1000));

        applyTransactionRequest = new ApplyTransactionRequest();
        applyTransactionRequest.setUserId(1L);
        applyTransactionRequest.setAmount(BigDecimal.valueOf(500));
        applyTransactionRequest.setTransactionType("DEPOSIT");

        usersAccount = new UsersAccount();
        usersAccount.setId(1L);
        usersAccount.setAccountBalance(BigDecimal.valueOf(2000));

        loan = new Loan();
        loan.setId(1L);
        loan.setStatus(LoanStatus.APPLIED);

        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@example.com");

        transaction = new Transactions();
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.DISBURSEMENT);
    }

    @Test
    void recordDisbursement_Success() {
        // Mock repository behavior
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        ResponseEntity<ResponseDto<Transactions>> response = transactionService.recordDisbursement(disbursementRequest);

        verify(transactionRepository, times(1)).save(any(Transactions.class));
        verify(loanRepository, times(1)).save(any(Loan.class));

        assertNotNull(response);
        assertEquals("Loan disbursed successfully", response.getBody().getStatusMessage());
        assertEquals(BigDecimal.valueOf(1000), response.getBody().getData().getAmount());
    }

    @Test
    void recordDisbursement_LoanNotFound() {
        // Mock loan not found
        when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            transactionService.recordDisbursement(disbursementRequest);
        });

        assertEquals("Loan not found", exception.getMessage());
    }

    @Test
    void recordRepayment_Success() {
        // Mock repository behavior
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));

        ResponseEntity<ResponseDto<Transactions>> response = transactionService.recordRepayment(disbursementRequest);

        verify(transactionRepository, times(1)).save(any(Transactions.class));
        verify(loanRepository, times(1)).save(any(Loan.class));

        assertNotNull(response);
        assertEquals("Loan repayment successfully done", response.getBody().getStatusMessage());
        assertEquals(BigDecimal.valueOf(1000), response.getBody().getData().getAmount());
    }

    @Test
    void applyTransaction_Success_Deposit() {
        // Mock user account and transaction
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(usersAccount));

        ResponseEntity<ResponseDto<Transactions>> response = transactionService.applyTransaction(applyTransactionRequest);

        verify(userAccountRepository, times(1)).save(any(UsersAccount.class));
        verify(transactionRepository, times(1)).save(any(Transactions.class));

        assertNotNull(response);
        assertEquals("Transaction applied successfully", response.getBody().getStatusMessage());
        assertEquals(BigDecimal.valueOf(2500), usersAccount.getAccountBalance());  // Balance after deposit
    }

    @Test
    void applyTransaction_InsufficientFunds_Withdrawal() {
        // Modify request to be a withdrawal that exceeds balance
        applyTransactionRequest.setTransactionType("WITHDRAWAL");

        // Mock user account and insufficient funds
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(usersAccount));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            transactionService.applyTransaction(applyTransactionRequest);
        });

        assertEquals("Insufficient funds for this withdrawal", exception.getMessage());
    }

    @Test
    void applyTransaction_AccountNotFound() {
        // Mock user account not found
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            transactionService.applyTransaction(applyTransactionRequest);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void generateTransactionStatementForUser_Success() {
        // Mock repository to return user and transactions
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(usersAccount));
        when(transactionRepository.findAllByUser_Id(anyLong())).thenReturn(Collections.singletonList(transaction));

        ResponseEntity<ResponseDto<List<TransactionStatement>>> response = transactionService.generateTransactionStatementForUser(1L);

        assertNotNull(response);
        assertEquals("Transaction statement generated successfully", response.getBody().getStatusMessage());
        assertEquals(1, response.getBody().getData().size());  // One transaction in the statement
    }

    @Test
    void generateTransactionStatementForUser_AccountNotFound() {
        // Mock account not found
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            transactionService.generateTransactionStatementForUser(1L);
        });

        assertEquals("Account not found", exception.getMessage());
    }
}
