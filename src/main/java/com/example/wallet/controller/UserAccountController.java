package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.service.BankAccountService;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.service.WalletService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserAccountController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    WalletService walletService;

    @PostMapping("/userAccount")
    public ResponseEntity<String> register (@RequestBody UserAccountDTO userAccountDTO) {
        if(userAccountService.getByEmail(userAccountDTO.getEmail()).isPresent()) {
            return new ResponseEntity<>("User account already exists with email: " + userAccountDTO.getEmail(), HttpStatus.BAD_REQUEST);
        }
        if(userAccountService.getByMobile(userAccountDTO.getMobile()).isPresent()) {
            return new ResponseEntity<>("User account already exists with mobile: " + userAccountDTO.getMobile(), HttpStatus.BAD_REQUEST);
        }
        UserAccount userAccount = userAccountService.create(userAccountDTO.mapToUserAccount());
        userAccount.setWallet(walletService.activateWallet(userAccount));
        userAccountService.create(userAccount);
        return new ResponseEntity<>("User account created successfully", HttpStatus.OK);
    }

    @GetMapping("/userAccounts")
    public ResponseEntity<List<UserAccount>> getAll() {
        return new ResponseEntity<>(userAccountService.getAll(), HttpStatus.OK);
    }

    
    
}
