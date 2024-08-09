package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.dto.UserAccountDTO;
import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.service.BankAccountService;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.service.WalletService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /* Bank Account Operations */
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
