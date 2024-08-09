package com.example.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wallet.enitites.BankAccount;
import com.example.wallet.enitites.UserAccount;
import com.example.wallet.enitites.Wallet;
import com.example.wallet.repository.BankAccountRepository;
import com.example.wallet.repository.UserAccountRepository;
import com.example.wallet.repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    WalletRepository walletRepository;

    public Wallet activateWallet(UserAccount userAccount) {
        return walletRepository.save(new Wallet(Double.valueOf(0.0), userAccount.getId()));
    }
    

    //Load wallet : From own bank account to own wallet
    public Double loadWallet(UserAccount userAccount, BankAccount bankAccount, Double amount) {
        walletRepository.updateWalletBalance(
            userAccount.getWallet().getId(),
            userAccount.getWallet().credit(amount));
        bankAccountRepository.updateBankBalance(
            bankAccount.getAccountNumber(),
            bankAccount.debit(amount));
        return userAccount.getWallet().getBalance();
    }

    //Transfer to bank: From own wallet to own bank account
    public Double transferToBank(UserAccount userAccount, BankAccount bankAccount, Double amount) {
        walletRepository.updateWalletBalance(
            userAccount.getWallet().getId(),
            userAccount.getWallet().debit(amount));
        bankAccountRepository.updateBankBalance(
            bankAccount.getAccountNumber(),
            bankAccount.credit(amount));
        return bankAccount.getBalance();
    }

    //Transfer to wallet: From own wallet to other's wallet
    public Double transferToWallet(UserAccount payerAccount, UserAccount payeeAccount, Double amount) {
        walletRepository.updateWalletBalance(
            payerAccount.getWallet().getId(),
            payerAccount.getWallet().debit(amount));
        walletRepository.updateWalletBalance(
            payeeAccount.getWallet().getId(),
            payeeAccount.getWallet().credit(amount));
        return payerAccount.getWallet().getBalance();
    }
}
