package com.example.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.enitites.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{

    @Transactional
    @Modifying
    @Query("UPDATE Wallet w SET w.balance = :balance WHERE id = :id" )
    void updateWalletBalance(Long id, Double balance);
    
}
