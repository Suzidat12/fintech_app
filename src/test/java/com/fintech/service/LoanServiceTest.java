package com.fintech.service;

import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.LoanRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
@Mock
private  UserAccountRepository userAccountRepository;
@Mock
private LoanRepository loanRepository;
@Mock
private LoanService loanService;
    @Test
    void applyForLoan_SuccessfulApplication() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setUserId(1L);
        loanRequest.setLoanAmount(new BigDecimal("1000"));
        loanRequest.setTenure(12);
        UsersAccount usersAccount = new UsersAccount();
        usersAccount.setId(1L);
        usersAccount.setEmail("user@example.com");
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(usersAccount));
        when(loanRepository.existsByUserAndStatusIn(any(), any())).thenReturn(false);
        when(loanService.calculateInterestRate(any(), any())).thenReturn(new BigDecimal("10"));
        ResponseEntity<ResponseDto<Loan>> response = loanService.applyForLoan(loanRequest);
        assertNotNull(response);
        assertEquals("Loan applied successfully", response.getBody().getStatusMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(new BigDecimal("1100.00"), response.getBody().getData().getTotalAmount()); // Loan + 10% interest
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void getLoanByUserId_UserFound() {
        Long userId = 1L;
        List<Loan> loans = Arrays.asList(new Loan(), new Loan());
        when(loanRepository.findAllByUser_Id(userId)).thenReturn(loans);
        ResponseEntity<ResponseDto<List<Loan>>> response = loanService.getLoanByUserId(userId);

        assertNotNull(response);
        assertEquals("User loans details fetched successfully", response.getBody().getStatusMessage());
        assertEquals(2, response.getBody().getData().size());
        verify(loanRepository, times(1)).findAllByUser_Id(userId);
    }

    @Test
    void getLoanByUserId_NoLoansFound() {
        Long userId = 1L;
        when(loanRepository.findAllByUser_Id(userId)).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(BadRequestException.class, () -> {
            loanService.getLoanByUserId(userId);
        });
        assertEquals("No loans found for the given user.", exception.getMessage());
    }
}