package com.example.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.MerchantAccount;
import com.example.wallet.repository.MerchantAccountRepository;

@Service
public class MerchantAccountService {

    @Autowired
    MerchantAccountRepository merchantAccountRepository;

    public MerchantAccount getByName(String name) {
        return merchantAccountRepository.findByName(name);
    }

    public List<MerchantAccount> getAll() {
        return merchantAccountRepository.findAll();
    }
    
}
