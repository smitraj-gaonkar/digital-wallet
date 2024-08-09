package com.example.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet.enitites.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>  {


    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByMobile(Long mobile);

}
