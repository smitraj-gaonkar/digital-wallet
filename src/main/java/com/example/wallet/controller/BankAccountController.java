package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.service.BankAccountService;
import com.example.wallet.service.UserAccountService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BankAccountController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    BankAccountService bankAccountService;
    
    @GetMapping("/bankAccounts")
    public ResponseEntity<List<BankAccount>> getAll() {
        return new ResponseEntity<>(bankAccountService.getAll(), HttpStatus.OK);
    }
    
    @PutMapping("/userAccount/{userAccountId}/addBankAccount")
    public ResponseEntity<String> addBankAccount (
        @PathVariable Long userAccountId,
        @RequestParam Long accountNumber, 
        @RequestParam String bankCode,
        @RequestParam Integer verificationPin) {
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        Optional<BankAccount> bankAccount = bankAccountService.getByAccountNumberAndBankCode(accountNumber, bankCode);
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        if(!bankAccount.isPresent()) {
            return new ResponseEntity<>("Invalid Bank Details! Bank account not found with accountNumber: " + accountNumber + " bankCode: " + bankCode, HttpStatus.NOT_FOUND);
        }
        if(!bankAccount.get().getVerificationPin().equals(verificationPin)) {
            return new ResponseEntity<>("Verification Failed! Incorrect verification pin", HttpStatus.BAD_REQUEST);
        }
        
        bankAccount.get().setUserId(userAccount.get().getId());
        bankAccountService.create(bankAccount.get());
        userAccount.get().getBankAccounts().add(bankAccount.get());
        userAccountService.create(userAccount.get());
        return new ResponseEntity<>("User & Bank account found/verified. Bank account added successfully.", HttpStatus.OK);
    }

    @PutMapping("/userAccount/{userAccountId}/removeBankAccount")
    public ResponseEntity<String> removeBankAccount (
        @PathVariable Long userAccountId,
        @RequestParam Long accountNumber, 
        @RequestParam String bankCode,
        @RequestParam Integer verificationPin) {
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        Optional<BankAccount> bankAccount = bankAccountService.getByAccountNumberAndBankCode(accountNumber, bankCode);
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        if(!bankAccount.isPresent()) {
            return new ResponseEntity<>("Invalid Bank Details! Bank account not found with accountNumber: " + accountNumber + " bankCode: " + bankCode, HttpStatus.NOT_FOUND);
        }
        if(!bankAccount.get().getVerificationPin().equals(verificationPin)) {
            return new ResponseEntity<>("Verification Failed! Incorrect verification pin", HttpStatus.BAD_REQUEST);
        }
        
        bankAccount.get().setUserId(null);
        bankAccountService.create(bankAccount.get());
        userAccount.get().getBankAccounts().remove(bankAccount.get());
        userAccountService.create(userAccount.get());
        return new ResponseEntity<>("User & Bank account found/verified. Bank account removed successfully. ", HttpStatus.OK);     
    }
}
