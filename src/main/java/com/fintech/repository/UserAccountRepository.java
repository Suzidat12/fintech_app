package com.fintech.repository;

import com.fintech.model.UsersAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UsersAccount, Long> {
    Optional<UsersAccount> findByEmail(String email);

}
