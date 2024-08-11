package com.example.wallet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.Transaction;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.enitites.Wallet;
import com.example.wallet.enums.Status;
import com.example.wallet.enums.TransactionType;
import com.example.wallet.service.TransactionService;
import com.example.wallet.service.UserAccountService;
import com.example.wallet.service.WalletService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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

    @Autowired
    TransactionService transactionService;

    @PutMapping("/userAccount/{userAccountId}/loadWallet")
    public ResponseEntity<String> loadWallet(
        @PathVariable Long userAccountId, 
        @RequestParam Long accountNumber, 
        @RequestParam Integer verificationPin, 
        @RequestParam Double amount) {

        Status status = Status.FAILED;
        String info = "LOAD WALLET";
        String message = "";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
             
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            message = "Invalid User! User account not found with id: " + userAccountId;
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            Optional<BankAccount> bankAccount = userAccount.get().getBankAccounts().stream().filter(bankAcc -> bankAcc.getAccountNumber().equals(accountNumber)).findFirst();
            if(!bankAccount.isPresent()) {
                message = "Invalid Bank Details! Bank account not found with accountNumber: " + accountNumber;
                httpStatus = HttpStatus.NOT_FOUND;
            } else if(!bankAccount.get().getVerificationPin().equals(verificationPin)) {
                message = "Verification Failed! Incorrect verification pin";
                httpStatus = HttpStatus.UNAUTHORIZED;
            } else if(bankAccount.get().getBalance()<=0 ||  bankAccount.get().getBalance()<amount) {
                message = "Insufficient Balance in bank account: " + accountNumber + ". Available balance: " + bankAccount.get().getBalance();
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                Double balance = walletService.loadWallet(userAccount.get(), bankAccount.get(), amount);
                status = Status.SUCCESS;
                message = "Wallet loaded with amount Rs." + amount +". Available Balance: " + balance;
                httpStatus = HttpStatus.OK;
            }

            transactionService.create(
                new Transaction(
                    new Date(),
                    TransactionType.CREDIT,
                    amount,
                    status,
                    info,
                    message,
                    userAccount.get().getId(),
                    userAccount.get().getWallet().getId()
                )
            );
        }
        return new ResponseEntity<>(message, httpStatus);
    }

    @PutMapping("/userAccount/{userAccountId}/transferToBank")
    public ResponseEntity<String> transferToBank(
        @PathVariable Long userAccountId, 
        @RequestParam Long accountNumber, 
        @RequestParam Double amount) {

        Status status = Status.FAILED;
        String info = "SELF TRANSFER TO BANK";
        String message = "";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        
        Optional<UserAccount> userAccount = userAccountService.getByUserAccountId(userAccountId);
        if(!userAccount.isPresent()) {
            message = "Invalid User! User account not found with id: " + userAccountId;
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            Optional<BankAccount> bankAccount = userAccount.get().getBankAccounts().stream().filter(bankAcc -> bankAcc.getAccountNumber().equals(accountNumber)).findFirst();
            if(!bankAccount.isPresent()) {
                message = "Invalid Bank Details! Bank account not found with accountNumber: " + accountNumber;
                httpStatus = HttpStatus.NOT_FOUND;
            } else {
                Wallet wallet = userAccount.get().getWallet();
                if(wallet.getBalance()<=0 ||  wallet.getBalance()<amount) {
                    message = "Insufficient Balance in wallet. Available balance: " + wallet.getBalance();
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    Double balance = walletService.transferToBank(userAccount.get(), bankAccount.get(), amount);
                    status = Status.SUCCESS;
                    message = "Amount Rs." + amount + " transfered to bank account no.: " + accountNumber + ". Available Balance: " + balance;
                    httpStatus = HttpStatus.OK;
                }
            }
            transactionService.create(
                new Transaction(
                    new Date(),
                    TransactionType.DEBIT,
                    amount,
                    status,
                    info,
                    message,
                    userAccount.get().getId(),
                    userAccount.get().getWallet().getId()
                )
            );
        }
        return new ResponseEntity<>(message, httpStatus);
    }

    @PutMapping("/userAccount/{userAccountId}/transferToWallet")
    public ResponseEntity<String> transferToWallet(
        @PathVariable("userAccountId") Long payerAccountId, 
        @RequestParam Long payeeMobile, 
        @RequestParam Double amount) {
        
        Status status = Status.FAILED;
        String info = "TRANSFER TO WALLET";
        String payerMessage = "";
        String payeeMessage = "";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        Optional<UserAccount> payerAccount = userAccountService.getByUserAccountId(payerAccountId);
        Optional<UserAccount> payeeAccount = userAccountService.getByMobile(payeeMobile);
        if(!payerAccount.isPresent()) {
            payerMessage = "Invalid User! User account not found with id: " + payerAccountId;
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            if(payerAccount.get().getMobile().equals(payeeMobile)) {
                payerMessage = "Invalid Payee! Payer and Payee cannot be same user";
                httpStatus = HttpStatus.NOT_FOUND;
            } else if(!payeeAccount.isPresent()) {
                payerMessage = "Invalid Payee! No user account found associated with mobile no.: " + payeeMobile;
                httpStatus = HttpStatus.NOT_FOUND;
            } else if(payerAccount.get().getWallet().getBalance()<=0 ||  payerAccount.get().getWallet().getBalance()<amount) {
                payerMessage = "Insufficient Balance in wallet. Available balance: " + payerAccount.get().getWallet().getBalance();
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                Double balance = walletService.transferToWallet(payerAccount.get(), payeeAccount.get(), amount);
                status = Status.SUCCESS;
                payerMessage = "Amount Rs." + amount + " transfered to " + payeeAccount.get().getName() + ". Available Balance: " + balance;
                payeeMessage = "Amount Rs." + amount + " received from " + payerAccount.get().getName() + ".";
                httpStatus = HttpStatus.OK;

                transactionService.create( new Transaction(
                    new Date(),
                    TransactionType.CREDIT,
                    amount,
                    status,
                    info,
                    payeeMessage,
                    payeeAccount.get().getId(),
                    payeeAccount.get().getWallet().getId()
                ));
            } 

            transactionService.create(new Transaction(
                new Date(),
                TransactionType.DEBIT,
                amount,
                status,
                info,
                payerMessage,
                payerAccount.get().getId(),
                payerAccount.get().getWallet().getId()
            ));
        }
        return new ResponseEntity<>(payerMessage, httpStatus);
    }
}
