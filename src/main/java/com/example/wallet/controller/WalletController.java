package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.enitites.Wallet;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.service.WalletService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class WalletController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    WalletService walletService;

    @PutMapping("/userAccount/{userAccountId}/loadWallet")
    public ResponseEntity<String> loadWallet(
        @PathVariable Long userAccountId, 
        @RequestParam Long accountNumber, 
        @RequestParam Integer verificationPin, 
        @RequestParam Double amount) {

        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        Optional<BankAccount> bankAccount = userAccount.get().getBankAccounts().stream().filter(bankAcc -> bankAcc.getAccountNumber().equals(accountNumber)).findFirst();
        if(!bankAccount.isPresent()) {
            return new ResponseEntity<>("Invalid Bank Details! Bank account not found with accountNumber: " + accountNumber, HttpStatus.NOT_FOUND);
        }
        if(!bankAccount.get().getVerificationPin().equals(verificationPin)) {
            return new ResponseEntity<>("Verification Failed! Incorrect verification pin", HttpStatus.BAD_REQUEST);
        }
        if(bankAccount.get().getBalance()<=0 ||  bankAccount.get().getBalance()<amount) {
            return new ResponseEntity<>("Insufficient Balance in bank account: " + accountNumber + ". Available balance: " + bankAccount.get().getBalance(), HttpStatus.OK);
        }
        
        Double balance = walletService.loadWallet(userAccount.get(), bankAccount.get(), amount);
        
        return new ResponseEntity<>("Wallet loaded with amount Rs." + amount +". Available Balance: " + balance, HttpStatus.OK);
    }

    @PutMapping("/userAccount/{userAccountId}/transferToBank")
    public ResponseEntity<String> transferToBank(
        @PathVariable Long userAccountId, 
        @RequestParam Long accountNumber, 
        @RequestParam Double amount) {
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + userAccountId, HttpStatus.NOT_FOUND);
        }
        Optional<BankAccount> bankAccount = userAccount.get().getBankAccounts().stream().filter(bankAcc -> bankAcc.getAccountNumber().equals(accountNumber)).findFirst();
        if(!bankAccount.isPresent()) {
            return new ResponseEntity<>("Invalid Bank Details! Bank account not found with accountNumber: " + accountNumber, HttpStatus.NOT_FOUND);
        }
        Wallet wallet = userAccount.get().getWallet();
        if(wallet.getBalance()<=0 ||  wallet.getBalance()<amount) {
            return new ResponseEntity<>("Insufficient Balance in wallet. Available balance: " + wallet.getBalance(), HttpStatus.OK);
        }

        Double balance = walletService.transferToBank(userAccount.get(), bankAccount.get(), amount);

        return new ResponseEntity<>("Amount Rs." + amount + " transfered to bank account no.: " + accountNumber + ". Available Balance: " + balance, HttpStatus.OK);
    }

    @PutMapping("/userAccount/{userAccountId}/transferToWallet")
    public ResponseEntity<String> transferToWallet(
        @PathVariable("userAccountId") Long payerAccountId, 
        @RequestParam Long payeeMobile, 
        @RequestParam Double amount) {
        
        Optional<UserAccount> payerAccount = userAccountService.getByUserAccountId(payerAccountId);
        if(!payerAccount.isPresent()) {
            return new ResponseEntity<>("Invalid User! User account not found with id: " + payerAccountId, HttpStatus.NOT_FOUND);
        }
        if(payerAccount.get().getMobile().equals(payeeMobile)) {
            return new ResponseEntity<>("Invalid Payee! Payer and Payee cannot be same user", HttpStatus.NOT_FOUND);
        }
        Optional<UserAccount> payeeAccount = userAccountService.getByMobile(payeeMobile);
        if(!payeeAccount.isPresent()) {
            return new ResponseEntity<>("Invalid Payee! No user account found associated with mobile no.: " + payeeMobile, HttpStatus.NOT_FOUND);
        }
        Wallet wallet = payerAccount.get().getWallet();
        if(wallet.getBalance()<=0 ||  wallet.getBalance()<amount) {
            return new ResponseEntity<>("Insufficient Balance in wallet. Available balance: " + wallet.getBalance(), HttpStatus.OK);
        }

        Double balance = walletService.transferToWallet(payerAccount.get(), payeeAccount.get(), amount);

        return new ResponseEntity<>("Amount Rs." + amount + " transfered to " + payeeAccount.get().getName() + ". Available Balance: " + balance, HttpStatus.OK);
    }
    
}
