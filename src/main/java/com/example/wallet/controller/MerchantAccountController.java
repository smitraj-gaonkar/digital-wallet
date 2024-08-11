package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.service.MerchantAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class MerchantAccountController {

    @Autowired
    MerchantAccountService merchantAccountService;

    @GetMapping("/merchantAccounts")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(merchantAccountService.getAll(), HttpStatus.OK);
    }
    
    
}
