package com.example.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.wallet.enitites.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountNumber = :accountNumber AND ba.bankCode = :bankCode")
    Optional<BankAccount> findByAccountNumberAndBankCode(Long accountNumber, String bankCode);
    
}
