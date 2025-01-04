package com.fintech.service;

import com.fintech.dto.JwtAuthenticationResponse;
import com.fintech.dto.LoginRequest;
import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.AdminAccountRequest;
import com.fintech.dto.request.UpdateLoanStatusRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Admin;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.model.enums.LoanStatus;
import com.fintech.model.enums.Role;
import com.fintech.repository.AdminRepository;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static com.fintech.dto.ApiResponse.ok;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserAccountRepository userAccountRepository;
    private final AdminRepository adminRepository;
    private final LoanRepository loanRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseDto<Admin>> create(AdminAccountRequest request){
        Optional<Admin> adminOptional =adminRepository.findByEmail(request.getEmail());
        if(adminOptional.isPresent()){
            throw new BadRequestException("Account already exist");
        }
       Admin admin = new Admin();
       admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
        admin.setRole(Role.ADMIN);
        admin.setCreatedDate(new Date());
        adminRepository.save(admin);
        return ok(admin,"Admin created successfully");
    }
    public JwtAuthenticationResponse loginAdmin(LoginRequest request) {
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

    public ResponseEntity<ResponseDto<String>> verifyUserAccount(Long userId, Long adminId) { //before verifying the account we will need to validate the bvn (using rest template or feign client when connected to third party
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
        usersAccount.setAccountStatus(AppStatus.COMPLETED);
        usersAccount.setVerifiedBy(admin);
        userAccountRepository.save(usersAccount);
        return ok(null, "User verified successfully");
    }

    public ResponseEntity<ResponseDto<Loan>> updateLoanStatus(@RequestBody UpdateLoanStatusRequest request) {
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
        if (LoanStatus.APPROVED.name().equals(request.getLoanStatus())) {
            loan.setStatus(LoanStatus.APPROVED);
        } else if (LoanStatus.REJECTED.name().equals(request.getLoanStatus())) {
            loan.setStatus(LoanStatus.REJECTED);
        } else if (LoanStatus.REPAID.name().equals(request.getLoanStatus())) {
                loan.setStatus(LoanStatus.REPAID);
        }else if (LoanStatus.OUTSTANDING.name().equals(request.getLoanStatus())) {
            loan.setStatus(LoanStatus.OUTSTANDING);
        } else {
            throw new BadRequestException("Invalid loan status");
        }
        loan.setVerifiedBy(admin);
        loanRepository.save(loan);
        return ok(null, "Loan status updated successfully");
    }


}
