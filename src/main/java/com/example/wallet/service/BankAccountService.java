package com.example.wallet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.repository.BankAccountRepository;

@Service
public class BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    public BankAccount create(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getByAccountNumberAndBankCode(Long accountNumber, String bankCode) {
        return bankAccountRepository.findByAccountNumberAndBankCode(accountNumber, bankCode);
    }

    
    
}
