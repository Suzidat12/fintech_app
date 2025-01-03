package com.fintech.repository;

import com.fintech.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions,Long> {
List<Transactions> findAllByUser_Id(Long userId);


}
