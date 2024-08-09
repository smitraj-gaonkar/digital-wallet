package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.service.BankAccountService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class BankAccountController {

    @Autowired
    BankAccountService bankAccountService;
    
    @GetMapping("/bankAccounts")
    public ResponseEntity<List<BankAccount>> getAll() {
        return new ResponseEntity<>(bankAccountService.getAll(), HttpStatus.OK);
    }
    
}
