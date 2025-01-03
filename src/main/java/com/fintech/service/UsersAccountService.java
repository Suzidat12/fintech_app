package com.fintech.service;


import com.fintech.dto.JwtAuthenticationResponse;
import com.fintech.dto.LoginRequest;
import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.UserAccountRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Admin;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.repository.AdminRepository;
import com.fintech.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.fintech.dto.ApiResponse.ok;


@Service
@RequiredArgsConstructor
public class UsersAccountService {
    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


   public ResponseEntity<ResponseDto<UsersAccount>> create(UserAccountRequest request){
       Optional<UsersAccount> usersAccountOptional = userAccountRepository.findByEmail(request.getEmail());
       if(usersAccountOptional.isPresent()){
        throw new BadRequestException("Account already exist");
       }
       UsersAccount usersAccount = new UsersAccount();
       usersAccount.setEmail(request.getEmail());
       usersAccount.setGender(request.getGender());
       usersAccount.setAddress(request.getAddress());
       usersAccount.setPassword(passwordEncoder.encode(request.getPassword()));
       usersAccount.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
       usersAccount.setDateOfBirth(request.getDateOfBirth());
       usersAccount.setAccountStatus(AppStatus.PENDING);
       usersAccount.setBvn(request.getBvn());
       usersAccount.setCreatedDate(LocalDateTime.now());
       return ok(usersAccount,"User created successfully");
   }
    public ResponseEntity<ResponseDto<UsersAccount>> update(UserAccountRequest request, Long id){
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(id);
        if(usersAccountOptional.isEmpty()){
            throw new BadRequestException("Account does not exist");
        }
        UsersAccount usersAccount = usersAccountOptional.get();
        usersAccount.setEmail(request.getEmail());
        usersAccount.setGender(request.getGender());
        usersAccount.setAddress(request.getAddress());
        usersAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        usersAccount.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
        usersAccount.setDateOfBirth(request.getDateOfBirth());
        usersAccount.setAccountStatus(AppStatus.PENDING);
        usersAccount.setBvn(request.getBvn());
        usersAccount.setUpdatedDate(LocalDateTime.now());
        return ok(usersAccount,"User updated successfully");
    }
    public ResponseEntity<ResponseDto<String>> delete(Long userId) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(userId);
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account does not exist");
        }
        userAccountRepository.deleteById(userId);

        return ok(null,"User account deleted successfully");
    }
    public ResponseEntity<ResponseDto<List<UsersAccount>>> retrieve(){
       List<UsersAccount> usersAccountList = userAccountRepository.findAll();
      return ok(usersAccountList,"Users data retrieve successfully");
    }
    public JwtAuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));
        UsersAccount user = userAccountRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password ..."));
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setAccount(user);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }
}
