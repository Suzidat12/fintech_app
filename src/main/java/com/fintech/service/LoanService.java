package com.fintech.service;


import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.LoanRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.model.enums.LoanStatus;
import com.fintech.repository.LoanRepository;
import com.fintech.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.fintech.dto.ApiResponse.ok;


@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserAccountRepository userAccountRepository;

    public ResponseEntity<ResponseDto<Loan>> applyForLoan(LoanRequest loanRequest) {
        Optional<UsersAccount> usersAccountOptional = userAccountRepository.findById(loanRequest.getUserId());
        if (usersAccountOptional.isEmpty()) {
            throw new BadRequestException("Account does not exist");
        }
        BigDecimal interestRate = calculateInterestRate(loanRequest.getLoanAmount(), loanRequest.getTenure());
        BigDecimal totalAmount = loanRequest.getLoanAmount().add(loanRequest.getLoanAmount()
                .multiply(interestRate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
        UsersAccount usersAccount = usersAccountOptional.get();
        if (usersAccount.getAccountStatus() == AppStatus.INACTIVE) {
            throw new BadRequestException("User account is inactive. Loan application cannot proceed.");
        }
        boolean isUserVerified = usersAccount.isVerified();
        if (!isUserVerified) {
            throw new BadRequestException("User is not verified. Loan application cannot proceed.");
        }
        boolean hasOutstandingLoan = loanRepository.existsByUserAndStatusIn(usersAccount,
                List.of(LoanStatus.OUTSTANDING));
        if (hasOutstandingLoan) {
            throw new BadRequestException("User has an outstanding loan and cannot apply for a new one.");
        }
        Loan loan = new Loan();
        loan.setUser(usersAccount);
        loan.setLoanAmount(loanRequest.getLoanAmount());
        loan.setTenure(loanRequest.getTenure());
        loan.setInterestRate(interestRate);
        loan.setTotalAmount(totalAmount);
        loan.setStatus(LoanStatus.APPLIED);
        loan.setCreatedDate(LocalDateTime.now());
        loanRepository.save(loan);
        return ok(loan,"Loan applied successfully");
    }

    public BigDecimal calculateInterestRate(BigDecimal loanAmount, int tenure) {
        if (loanAmount.compareTo(BigDecimal.valueOf(10000)) < 0) {
            return BigDecimal.valueOf(5);
        } else if (tenure < 12) {
            return BigDecimal.valueOf(7);
        } else {
            return BigDecimal.valueOf(10);
        }
    }

    public ResponseEntity<ResponseDto<List<Loan>>> getLoanByUserId(Long userId) {
        List<Loan> loanList = loanRepository.findAllByUser_Id(userId);
        return ok(loanList,"User loans details fetched successfully");
    }

    public ResponseEntity<ResponseDto<List<Loan>>> getLoanByStatus(String status) {
        List<Loan> loanList = loanRepository.findAllByStatus(LoanStatus.valueOf(status));
        return ok(loanList,"User loans details with status fetched successfully");
    }



}
