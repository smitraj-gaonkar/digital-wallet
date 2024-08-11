package com.example.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.Transaction;
import com.example.wallet.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public void create(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getByUserId(Long accountId) {
        return transactionRepository.findByUserId(accountId);
    }
    
}
