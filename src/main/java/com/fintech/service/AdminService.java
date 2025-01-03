package com.fintech.service;

import com.fintech.dto.JwtAuthenticationResponse;
import com.fintech.dto.LoginRequest;
import com.fintech.dto.ResponseDto;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Admin;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.LoanStatus;
import com.fintech.repository.AdminRepository;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.TransactionRepository;
import com.fintech.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.fintech.dto.ApiResponse.ok;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TransactionRepository transactionRepository;
    private final UserAccountRepository userAccountRepository;
    private final AdminRepository adminRepository;
    private final LoanRepository loanRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    public JwtAuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password ..."));
        String jwt = jwtService.generateToken(admin);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), admin);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setAdmin(admin);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    public ResponseEntity<ResponseDto<String>> verifyUserAccount(Long userId, Long adminId) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(userId);
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account not found");
        }
        if (adminOptional.isEmpty()) {
            throw new BadRequestException("Admin not found");
        }
        UsersAccount usersAccount = usersAccountOptional.get();
        Admin admin = adminOptional.get();
        usersAccount.setVerified(true);
        usersAccount.setVerifiedBy(admin);
        userAccountRepository.save(usersAccount);
        return ok(null, "User verified successfully");
    }

    public ResponseEntity<ResponseDto<Loan>> updateLoanStatus(Long loanId, Long adminId, String loanStatus) {
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
        if (LoanStatus.APPROVED.name().equals(loanStatus)) {
            loan.setStatus(LoanStatus.APPROVED);
        } else if (LoanStatus.REJECTED.name().equals(loanStatus)) {
            loan.setStatus(LoanStatus.REJECTED);
        } else if (LoanStatus.REPAID.name().equals(loanStatus)) {
                loan.setStatus(LoanStatus.REPAID);
        }else if (LoanStatus.OUTSTANDING.name().equals(loanStatus)) {
            loan.setStatus(LoanStatus.OUTSTANDING);
        } else {
            throw new BadRequestException("Invalid loan status");
        }
        loan.setVerifiedBy(admin);
        loanRepository.save(loan);
        return ok(null, "Loan status updated successfully");
    }


}
