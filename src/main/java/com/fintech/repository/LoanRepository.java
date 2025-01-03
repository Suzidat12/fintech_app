package com.fintech.repository;

import com.fintech.model.Loan;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByUserAndStatusIn(UsersAccount user, List<LoanStatus> statuses);
    List<Loan> findAllByUser_Id(Long userId);
}
