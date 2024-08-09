package com.example.wallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>  {


    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByMobile(Long mobile);

    @Transactional
    @Modifying
    @Query("UPDATE UserAccount u SET u.bankAccounts = :bankAccounts WHERE u.id = :id")
    void updateBankAccounts(Long id, List<BankAccount> bankAccounts);

}
