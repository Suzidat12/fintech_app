package com.fintech.service;


import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.UserAccountRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.fintech.dto.ApiResponse.ok;


@Service
@RequiredArgsConstructor
public class UsersAccountService {
    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final PaymentMethodRepository paymentMethodRepository;
//    private final TransactionRepository transactionRepository;


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
    public ResponseEntity<ResponseDto<String>> delete(Long id) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(id);
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account does not exist");
        }
        userAccountRepository.deleteById(id);
        return ok(null,"User account deleted successfully");
    }
    public ResponseEntity<ResponseDto<List<UsersAccount>>> retrieve(){
       List<UsersAccount> usersAccountList = userAccountRepository.findAll();
      return ok(usersAccountList,"Users data retrieve successfully");
    }

//
//    public Transaction createTransaction(Long userId, Transaction transaction) {
//        return userRepository.findById(userId)
//                .map(user -> {
//                    transaction.setUser(user);
//                    return transactionRepository.save(transaction);
//                }).orElseThrow(() -> new RuntimeException("User not found"));
//    }
}
