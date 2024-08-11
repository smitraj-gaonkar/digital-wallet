package com.example.wallet.enitites;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.wallet.enums.Status;
import com.example.wallet.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Date date;
    TransactionType type;
    Double amount;
    Status status;
    String info;
    String message;
    Long userId;
    Long walletId;

    public Transaction(Date date, TransactionType type, Double amount, Status status, String info, String message, Long userId, Long walletId) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.info = info;
        this.message = message;
        this.userId = userId;
        this.walletId = walletId;
    }

    
}
