package com.fintech.service;

import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.AdminAccountRequest;
import com.fintech.dto.request.UpdateLoanStatusRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Admin;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.LoanStatus;
import com.fintech.repository.AdminRepository;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.UserAccountRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AdminService adminService;
    private AdminAccountRequest adminAccountRequest;
    private UsersAccount usersAccount;
    private Admin admin;
    @BeforeEach()
    public void setUp() {
        MockitoAnnotations.openMocks(this); 
        adminAccountRequest = new AdminAccountRequest();
        adminAccountRequest.setEmail("test@example.com");
        adminAccountRequest.setPassword("password123");
        adminAccountRequest.setPhoneNumber("1234567890");
        adminAccountRequest.setFirstName("John");
        adminAccountRequest.setLastName("Doe");

        usersAccount = new UsersAccount();
        usersAccount.setId(1L);
        usersAccount.setEmail("user@example.com");

        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@example.com");
    }



    @Test
    void create_AccountAlreadyExists() {
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(new Admin()));
        try {
            adminService.create(adminAccountRequest);
            fail("Expected BadRequestException");
        } catch (BadRequestException e) {
            assertEquals("Account already exist", e.getMessage());
        }
        verify(adminRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void create_SuccessfulCreation() {
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        ResponseEntity<ResponseDto<Admin>> response = adminService.create(adminAccountRequest);
        verify(adminRepository, times(1)).save(any(Admin.class));
        assertNotNull(response);
        assertEquals("Admin created successfully", response.getBody().getStatusMessage());
        assertEquals("test@example.com", response.getBody().getData().getEmail());
        assertEquals("encodedPassword", response.getBody().getData().getPassword());  // Check if password is encoded
    }

    @Test
    void create_PasswordIsEncoded() {
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        adminService.create(adminAccountRequest);
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void verifyUserAccount_Success() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(usersAccount));
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        ResponseEntity<ResponseDto<String>> response = adminService.verifyUserAccount(1L, 1L);
        verify(userAccountRepository, times(1)).save(usersAccount);
        verify(adminRepository, times(1)).findById(1L);
        assertNotNull(response);
        assertEquals("User verified successfully", response.getBody().getStatusMessage());
        assertTrue(usersAccount.isVerified());
        assertEquals(admin, usersAccount.getVerifiedBy());
    }

    @Test
    void verifyUserAccount_UserNotFound() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.empty());
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            adminService.verifyUserAccount(1L, 1L);
        });
        assertEquals("Account not found", thrown.getMessage());
    }

    @Test
    void verifyUserAccount_AdminNotFound() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(usersAccount));
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            adminService.verifyUserAccount(1L, 1L);
        });
        assertEquals("Admin not found", thrown.getMessage());
    }

    @Test
    void updateLoanStatus_SuccessfulUpdate() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(LoanStatus.OUTSTANDING);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Admin admin = new Admin();
        admin.setId(1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setLoanId(1L);
        request.setAdminId(1L);
        request.setLoanStatus(LoanStatus.APPROVED.name());
        ResponseEntity<ResponseDto<Loan>> response = adminService.updateLoanStatus(request);

        assertEquals("Loan status updated successfully", response.getBody().getStatusMessage());
        assertEquals(LoanStatus.APPROVED, loan.getStatus());
        verify(loanRepository).save(loan);
    }

    @Test
    void updateLoanStatus_LoanNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateLoanStatusRequest request = new UpdateLoanStatusRequest();
        request.setLoanId(1L);
        request.setAdminId(1L);
        request.setLoanStatus(LoanStatus.APPROVED.name());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> adminService.updateLoanStatus(request));
        assertEquals("Loan not found", exception.getMessage());
    }
}