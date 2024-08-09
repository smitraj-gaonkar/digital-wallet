package com.example.wallet.initializer;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.repository.BankAccountRepository;

@Component
public class DatabaseInitializer {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PostConstruct
    public void init() {
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000001"), "Smith John", "AXIS110011", Double.valueOf(1000.50), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000002"), "John Doe", "HDFC2200022", Double.valueOf(2000.25), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000003"), "Alex Chase", "HDFC2200021", Double.valueOf(1540.25), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000004"), "David Pinkman", "AXIS1100012", Double.valueOf(2120.25), Integer.valueOf(1234), null));
        bankAccountRepository.save(new BankAccount(Long.valueOf("9000005"), "Smith John", "ICIC3300030", Double.valueOf(5000.25), Integer.valueOf(1234), null));
    }
}