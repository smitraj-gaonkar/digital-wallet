package com.example.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallet.enitites.MerchantAccount;

@Repository
public interface MerchantAccountRepository extends JpaRepository<MerchantAccount, Long>{

    MerchantAccount findByName(String name);
    
}
