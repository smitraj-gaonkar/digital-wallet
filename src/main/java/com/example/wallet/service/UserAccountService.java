package com.example.wallet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.UserAccount;
import com.example.wallet.repository.UserAccountRepository;

@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;

    public UserAccount create(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    public Optional<UserAccount> getByUserAccountId(Long id) {
        return userAccountRepository.findById(id);
    }

    public Optional<UserAccount> getByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    public Optional<UserAccount> getByMobile(Long mobile) {
        return userAccountRepository.findByMobile(mobile);
    }

    public List<UserAccount> getAll() {
        return userAccountRepository.findAll();
    }
    
}
