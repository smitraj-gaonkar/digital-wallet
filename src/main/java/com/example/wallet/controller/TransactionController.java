package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/account/{accountId}/transaction")
    public ResponseEntity<?> getByUserId(@PathVariable Long accountId) {
        return new ResponseEntity<>(transactionService.getByUserId(accountId),HttpStatus.OK);
    }

    @GetMapping("/accounts/transaction")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(transactionService.getAll(),HttpStatus.OK);
    }
    
    
}
